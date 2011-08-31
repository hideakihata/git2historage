package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ClassDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ClassDefinitionStateManager.CLASS_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.*;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;


/**
 * クラス情報の構築を行うビルダー．
 * 
 * @author kou-tngt
 *
 */
public class ClassBuilder extends CompoundDataBuilder<UnresolvedClassInfo> {

    /**
     * 引数で与えられた構築データ管理を行うBuildDataManagerと
     * デフォルトの修飾子ビルダー，名前ビルダーを用いて初期化する．
     * 
     * @param targetDataManager　ビルダーが利用する構築データ管理者
     */
    public ClassBuilder(final BuildDataManager targetDataManager) {
        this(targetDataManager, new ModifiersBuilder(), new NameBuilder());
    }

    /**
     * 引数で与えられた構築データ管理を行うBuildDataManager
     * 修飾子ビルダー，名前ビルダーを用いて初期化する．
     * 
     * @param targetDataManager　ビルダーが利用する構築データ管理者
     */
    public ClassBuilder(final BuildDataManager targetDataManager,
            final ModifiersBuilder modifiersBuilder, final NameBuilder nameBuilder) {

        //データ管理者はnullだと困る．
        if (null == targetDataManager) {
            throw new NullPointerException("builderManager is null.");
        }

        //名前構築もできないと困る．
        if (null == nameBuilder) {
            throw new NullPointerException("nameBuilder is null.");
        }

        //状態変化を通知して欲しいものを登録する．
        this.addStateManager(this.classStateManager);
        this.addStateManager(new ModifiersDefinitionStateManager());
        this.addStateManager(new NameStateManager());

        this.buildManager = targetDataManager;
        this.modifiersBuilder = modifiersBuilder;
        this.nameBuilder = nameBuilder;

        //内部で使うビルダーを登録して，こいつらがアクティブになった時に自動的にビジターからのイベントが届くようにする．
        //デフォルトで非アクティブ状態にセットされる
        this.addInnerBuilder(modifiersBuilder);
        this.addInnerBuilder(nameBuilder);
    }

    /**
     * 登録されたステートマネージャから状態変化を受け取るメソッド．
     * @param event 状態変化イベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChanged(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        if (type.equals(CLASS_STATE_CHANGE.ENTER_CLASS_DEF)) {
            //クラス定義に入ったら，とりあえず新しいクラス定義を登録しに行く
            final AstVisitEvent trigger = event.getTrigger();
            this.startClassDefinition(event, trigger.getStartLine(), trigger.getStartColumn(),
                    trigger.getEndLine(), trigger.getEndColumn());

        } else if (type.equals(CLASS_STATE_CHANGE.EXIT_CLASS_DEF)) {
            //クラス定義部から出たので，データ構築を終了して後始末を行う．
            this.endClassDefinition();
            this.isClassNameBuit = false;
        } else if (type.equals(CLASS_STATE_CHANGE.ENTER_CLASS_BLOCK)) {
            //クラスのブロックに入ったのでデータ管理者に通知して，名前構築フラグを降ろす
            this.isClassNameBuit = false;
            this.enterClassblock();
        } else if (this.classStateManager.isInPreDeclaration()) {
            //クラスブロックの前の，クラス宣言部に居る場合．
            if (type.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)) {
                //修飾子定義部に入ったので修飾子ビルダをアクティブに
                this.modifiersBuilder.activate();
            } else if (type
                    .equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)) {
                //修飾子定義部から出たので修飾子ビルダを非アクティブにして修飾子情報を登録する
                this.modifiersBuilder.deactivate();
                this.registModifiers(this.modifiersBuilder.popLastBuiltData());
                this.modifiersBuilder.clearBuiltData();
            } else if (type.equals(NameStateManager.NAME_STATE.ENTER_NAME) && !this.isClassNameBuit) {
                //名前情報定義部に最初に入った時にクラス名を構築する
                this.nameBuilder.activate();
            } else if (type.equals(NameStateManager.NAME_STATE.EXIT_NAME) && !this.isClassNameBuit) {
                //最初に名前情報定義部から出た時にクラス名を登録して，クラス名が構築済みであることを表すフラグを立てる
                this.nameBuilder.deactivate();
                this.registClassName(this.nameBuilder.getFirstBuiltData());
                this.nameBuilder.clearBuiltData();
                this.isClassNameBuit = true;
            }
        }
    }

    /**
     * クラス定義部から出た時に呼び出され，
     * データ管理者に {@link BuildDataManager#endClassDefinition()} メソッドを通じてそれを通知し，
     * 構築中のデータを内部スタックから取り出す．
     * 
     * このメソッドをオーバーライドすることで，
     * クラス定義部が終了したときの処理を任意に変更することができる．
     */
    protected void endClassDefinition() {
        this.buildManager.endClassDefinition();
        if (!this.buildingClassStack.isEmpty()) {
            this.buildingClassStack.pop();
        }
    }

    /**
     * クラスブロックに入った時に呼び出され，
     * データ構築部にその旨を通知する．
     * 
     * このメソッドをオーバーライドすることで，
     * クラスブロックに入った処理を任意に変更することができる．
     */
    protected void enterClassblock() {
        this.buildManager.enterClassBlock();
    }

    /**
     * 構築中のクラス情報があればそれを，なければnullを返す．
     * 
     * @return 構築中のクラス情報があればそれを，なければnull．
     */
    protected final UnresolvedClassInfo getBuildingClassInfo() {
        if (this.hasBuildingClassInfo()) {
            return this.buildingClassStack.peek();
        } else {
            return null;
        }
    }

    /**
     * 構築中のクラス情報があるかどうかを返す．
     * 
     * @return 構築中のクラス情報があればtrue
     */
    protected boolean hasBuildingClassInfo() {
        return !this.buildingClassStack.isEmpty();
    }

    /**
     * クラス定義部に入った時に呼ばれるメソッド．
     * 新しく到達したクラスのインスタンスを生成し，登録するためのメソッド{@link #registClassInfo(UnresolvedClassInfo)}を呼ぶ．
     * 
     * このメソッドをオーバーライドすることで，クラス定義部に入った際の挙動を任意に変更することができる．
     * 
     * @param startLine クラスの開始行番号
     * @param startColumn クラスの開始列番号
     * @param endLine　クラスの終了行番号
     * @param endColumn　クラスの終了列番号
     */
    protected void startClassDefinition(final StateChangeEvent<AstVisitEvent> event,
            final int startLine, final int startColumn, final int endLine, final int endColumn) {
        final FileInfo currentFile = DataManager.getInstance().getFileInfoManager().getCurrentFile(
                Thread.currentThread());
        assert null != currentFile : "Illegal state: the file information was not registered to FileInfoManager";

        final UnresolvedClassInfo classInfo = new UnresolvedClassInfo(currentFile,
                this.buildManager.getCurrentUnit());

        //enum宣言が来た場合はenumであることを登録
        if (event.getTrigger().getToken().isEnumDefinition()) {
            classInfo.setIsEnum();
        }

        classInfo.setFromLine(startLine);
        classInfo.setFromColumn(startColumn);
        classInfo.setToLine(endLine);
        classInfo.setToColumn(endColumn);

        this.buildingClassStack.push(classInfo);
        this.registClassInfo(classInfo);
    }

    /**
     * クラス定義部に入り，新しくクラス情報を作成したときに呼び出され，
     * データ管理者に対して新しいクラス情報を登録する．
     * またその情報をこのクラスの内部スタックにも保存する．
     * 
     * このメソッドをオーバーライドすることで，クラス情報の登録処理を任意に変更することができる．
     * 
     * @param classInfo 登録する新規クラス
     */
    protected void registClassInfo(final UnresolvedClassInfo classInfo) {
        this.registBuiltData(classInfo);
        this.buildManager.startClassDefinition(classInfo);
    }

    /**
     * クラス名情報が構築された時に呼び出され，現在構築中のクラス情報に対して，名前を登録する．
     * 構築中のクラスデータが内部スタックから発見できない場合は，何もしない．
     * 
     * このメソッドを任意にオーバーライドすることで，名前情報の登録処理を任意に変更することができる．
     * 
     * @param name 構築中のクラス名．
     */
    protected void registClassName(final String[] name) {
        if (this.hasBuildingClassInfo() && 0 < name.length) {
            final int classStackSize = this.buildingClassStack.size();

            if (classStackSize > 1
                    && !(this.buildManager.getOuterUnit() instanceof UnresolvedClassInfo)) {

                // 外側がメソッドのインナークラスの場合，外側のクラスをから見て何番目のインナークラスかを示すIDを振る
                final int id = this.buildingClassStack.get(classStackSize - 2).getInnerClasses()
                        .size();
                name[0] = (id + 1) + name[0];
            }
            this.getBuildingClassInfo().setClassName(name[0]);
        }
    }

    /**
     * クラスの修飾子情報が構築された時に呼び出され，修飾子情報を登録する．
     * 
     * このメソッドをオーバーライドすることで，修飾子から判断される情報の登録処理を任意に変更することができる．
     * 
     * @param modifiers　クラスに付けられた修飾子の配列
     */
    protected void registModifiers(final ModifierInfo[] modifiers) {
        if (this.hasBuildingClassInfo()) {
            final UnresolvedClassInfo buildingclass = this.getBuildingClassInfo();
            for (final ModifierInfo modifier : modifiers) {
                buildingclass.addModifier(modifier);
            }

        }
    }

    /**
     * クラス定義部に関連するビジターの状態を管理し，状態変化イベントを通知する．
     */
    private final ClassDefinitionStateManager classStateManager = new ClassDefinitionStateManager();

    /**
     * 構築中データの管理者
     */
    private final BuildDataManager buildManager;

    /**
     * 修飾子情報を構築するビルダ
     */
    private final ModifiersBuilder modifiersBuilder;

    /**
     * 名前情報を構築するビルダ
     */
    private final NameBuilder nameBuilder;

    /**
     * 構築中のクラスデータスタック
     */
    private final Stack<UnresolvedClassInfo> buildingClassStack = new Stack<UnresolvedClassInfo>();

    /**
     * 構築中のクラスデータに対してクラス名がすでに構築されたかどうかを表す
     */
    private boolean isClassNameBuit = false;
}
