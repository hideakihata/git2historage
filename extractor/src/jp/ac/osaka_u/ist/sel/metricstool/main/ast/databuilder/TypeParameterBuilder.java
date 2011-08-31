package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.*;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


/**
 * 型パラメータ情報を構築するビルダー
 * 
 * @author kou-tngt, t-miyake
 *
 */
public class TypeParameterBuilder extends CompoundDataBuilder<UnresolvedTypeParameterInfo> {

    /**
     * 引数に与えられた構築データの管理者とデフォルトの名前情報ビルダー，型情報ビルダーを用いて初期化する
     * @param buildDataManager　構築データの管理者
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager) {
        this(buildDataManager, new NameBuilder(), new TypeBuilder(buildDataManager));
    }

    /**
     * 引数に与えられた構築データの管理者，名前情報ビルダー，型情報ビルダーを用いて初期化する
     * @param buildDataManager　構築データの管理者
     * @param nameBuilder　名前情報ビルダー
     * @param typeBuilder　型情報ビルダー
     */
    public TypeParameterBuilder(final BuildDataManager buildDataManager,
            final NameBuilder nameBuilder, final TypeBuilder typeBuilder) {
        if (null == buildDataManager) {
            throw new NullPointerException("buildDataManager is null.");
        }

        if (null == nameBuilder) {
            throw new NullPointerException("nameBuilder is null.");
        }

        if (null == typeBuilder) {
            throw new NullPointerException("typeBuilder is null.");
        }

        this.buildDataManager = buildDataManager;
        this.nameBuilder = nameBuilder;
        this.typeBuilder = typeBuilder;

        //内部ビルダーの登録
        this.addInnerBuilder(nameBuilder);
        this.addInnerBuilder(typeBuilder);

        //状態通知を受け取りたいものを登録
        this.addStateManager(typeParameterStateManager);
    }

    /**
     * 状態変化イベントの通知を受けるメソッド．
     * @param event 状態変化イベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#stateChanged(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();

        if (this.isActive()) {
            if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF)) {
                //型パラメータ定義に入ったのでがんばる
                this.nameBuilder.activate();
                this.typeBuilder.activate();
                this.inTypeParameterDefinition = true;
            } else if (type
                    .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF)) {
                //型パラメータ定義が終わったので，データを構築して後始末
                this.buildTypeParameter();

                this.nameBuilder.deactivate();
                this.typeBuilder.deactivate();
                this.lowerBoundsType = null;
                this.upperBoundsType.clear();
                this.nameBuilder.clearBuiltData();
                this.typeBuilder.clearBuiltData();
                this.inTypeParameterDefinition = false;

            } else if (this.inTypeParameterDefinition) {
                //型パラメータ定義部ないでの出来事
                /* (型変数)Type Variableには"Super"のトークンは文法上来ないので削除
                if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS)) {
                    //型の下限宣言がきたので型構築部ががんばる
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS)) {
                    //型の下限情報を構築する
                    this.lowerBoundsType = this.builtTypeBounds(); 
                } else */
                if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS)) {
                    //型の上限宣言が来たので構築部ががんばる
                    this.nameBuilder.deactivate();
                    this.typeBuilder.activate();
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS)) {
                    //型の上限情報を構築する
                    UnresolvedTypeInfo<?> typeInfo = this.builtTypeBounds();
                    if (this.typeParameterStateManager.isInTypeParameterDefinition()) {
                        this.upperBoundsType.add(typeInfo);
                    }
                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_ADDITIONAL_BOUNDS)) {

                } else if (type
                        .equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_ADDITIONAL_BOUNDS)) {
                    UnresolvedTypeInfo<?> typeInfo = this.builtTypeBounds();
                    this.upperBoundsType.add(typeInfo);
                }
            }
        }
    }

    /**
     * 型パラメータ定義部の終了時に呼び出され，型パラメータを構築するメソッド
     * このメソッドをオーバーライドすることで，型パラメータ定義部で任意の処理を行わせることができる．
     */
    protected void buildTypeParameter() {
        //型パラメータの名前が複数合ったらおかしい
        assert (this.nameBuilder.getBuiltDataCount() == 1);

        final String[] name = this.nameBuilder.getFirstBuiltData();

        //型パラメータの名前が複数個に分かれててもおかしい
        assert (name.length == 1);

        final UnresolvedUnitInfo<? extends UnitInfo> ownerUnit = this.buildDataManager
                .getCurrentUnit();

        assert (ownerUnit instanceof UnresolvedCallableUnitInfo)
                || (ownerUnit instanceof UnresolvedClassInfo) : "Illegal state: not parametrized unit";

        //型の上限情報下限情報を取得
        final List<UnresolvedTypeInfo<? extends TypeInfo>> upperBounds = this.getUpperBounds();
        final UnresolvedTypeInfo<? extends TypeInfo> lowerBounds = this.getLowerBounds();
        final int index = buildDataManager.getCurrentTypeParameterCount();

        UnresolvedTypeParameterInfo parameter = null;

        if (null == upperBounds || upperBounds.isEmpty() || checkUpperBounds()) {
            List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> upperReferenceBounds = new ArrayList<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>>();
            for (final UnresolvedTypeInfo<? extends TypeInfo> typeInfo : this.getUpperBounds()) {
                upperReferenceBounds.add((UnresolvedReferenceTypeInfo<?>) typeInfo);
            }
            parameter = new UnresolvedTypeParameterInfo(ownerUnit, name[0], index,
                    upperReferenceBounds);
            //            else {
            //                //下限がある場合はこっちを作る
            //                parameter = new UnresolvedSuperTypeParameterInfo(ownerUnit, name[0], index,
            //                        (UnresolvedReferenceTypeInfo<?>) upperBounds,
            //                        (UnresolvedReferenceTypeInfo<?>) lowerBounds);
            //            }
        } else {
            // 少なくともJavaではここに到達してはいけない(型パラメータは参照型しか定義できない)
            // TODO C#の場合は型パラメータがプリミティブ型の場合もあるので対処が必要
            assert false : "Illegal state: type parameter is not reference type";
        }
        //最後にデータ管理者に登録する
        this.buildDataManager.addTypeParameger(parameter);
    }

    /**
     * upperBoundsに格納されているTypeInfoが全てReferenceTypeInfoかどうかを調べる
     * @return upperBoundsに格納されているTypeInfoが全てReferenceTypeInfoならtrue
     */
    private boolean checkUpperBounds() {
        for (final UnresolvedTypeInfo<?> typeInfo : this.getUpperBounds()) {
            if (!(typeInfo instanceof UnresolvedReferenceTypeInfo<?>)) {
                return false;
            }
        }
        return true;

    }

    /**
     * 型の上限情報を返す．
     * @return　型の上限情報
     */
    protected List<UnresolvedTypeInfo<? extends TypeInfo>> getUpperBounds() {
        return this.upperBoundsType;
    }

    /**
     * 型の下限情報を返す．
     * @return　型の下限情報
     */
    protected UnresolvedTypeInfo<? extends TypeInfo> getLowerBounds() {
        return this.lowerBoundsType;
    }

    /**
     * 最後に構築された型の情報を返す．
     * @return　最後に構築された型
     */
    protected UnresolvedTypeInfo<? extends TypeInfo> builtTypeBounds() {
        return this.typeBuilder.popLastBuiltData();
    }

    /**
     * 名前構築を行うビルダーを返す．
     * @return　名前構築を行うビルダー
     */
    protected NameBuilder getNameBuilder() {
        return this.nameBuilder;
    }

    /**
     * 型情報を構築するビルダーを返す
     * @return　型情報を構築するビルダー
     */
    protected TypeBuilder getTypeBuilder() {
        return this.typeBuilder;
    }

    /**
     * 名前構築を行うビルダー
     */
    private final NameBuilder nameBuilder;

    /**
     * 型情報を構築するビルダー
     */
    private final TypeBuilder typeBuilder;

    /**
     * 構築情報の管理者
     */
    private final BuildDataManager buildDataManager;

    /**
     * 型パラメータの上限
     * 上限は複数個指定できるのでリストで管理
     */
    private List<UnresolvedTypeInfo<? extends TypeInfo>> upperBoundsType = new ArrayList<UnresolvedTypeInfo<? extends TypeInfo>>();

    /**
     * 型パラメータの下限
     */
    private UnresolvedTypeInfo<? extends TypeInfo> lowerBoundsType;

    /**
     * 型パラメータ定義部にいるかどうかを表す
     */
    private boolean inTypeParameterDefinition = false;

    private TypeParameterStateManager typeParameterStateManager = new TypeParameterStateManager();
}
