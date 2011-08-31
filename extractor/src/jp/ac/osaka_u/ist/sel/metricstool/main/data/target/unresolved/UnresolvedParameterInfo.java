package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 引数を表すためのクラス． 型を提供するのみ．
 * 
 * @author higo
 * 
 */
public class UnresolvedParameterInfo
        extends
        UnresolvedVariableInfo<TargetParameterInfo, UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> {

    /**
     * 引数オブジェクトを初期化する．名前と型が必要．
     * 
     * @param name 引数名
     * @param type 引数の型
     * @param index 何番目の引数でるかを表す
     * @param definitionMethod 引数を宣言しているメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedParameterInfo(final String name, final UnresolvedTypeInfo<?> type,
            final int index,
            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> definitionMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(name, type, definitionMethod, fromLine, fromColumn, toLine, toColumn);

        this.index = index;
    }

    /**
     * 未解決引数情報を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決引数情報の定義が行われているクラス
     * @param usingMethod 未解決引数情報の定義が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み引数情報
     */
    @Override
    public TargetParameterInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedCallableUnitInfo<?> unresolvedOwnerMethod = this.getDefinitionUnit();
        final CallableUnitInfo ownerMethod = unresolvedOwnerMethod.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final UnresolvedClassInfo unresolvedOwnerClass = unresolvedOwnerMethod.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // 修飾子，パラメータ名，型，位置情報を取得
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String name = this.getName();
        final int index = this.getIndex();
        final UnresolvedTypeInfo<?> unresolvedParameterType = this.getType();
        final TypeInfo type = unresolvedParameterType.resolve(ownerClass, ownerMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final int parameterFromLine = this.getFromLine();
        final int parameterFromColumn = this.getFromColumn();
        final int parameterToLine = this.getToLine();
        final int parameterToColumn = this.getToColumn();

        final CallableUnitInfo ownerCallableUnit = this.getDefinitionUnit().getResolved();

        // パラメータオブジェクトを生成する
        this.resolvedInfo = new TargetParameterInfo(modifiers, name, type, index,
                ownerCallableUnit, parameterFromLine, parameterFromColumn, parameterToLine,
                parameterToColumn);
        return this.resolvedInfo;
    }

    /**
     * 引数のインデックスを返す
     * 
     * @return　引数のインデックス
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 引数のインデックスを保存するための変数
     */
    private final int index;

}
