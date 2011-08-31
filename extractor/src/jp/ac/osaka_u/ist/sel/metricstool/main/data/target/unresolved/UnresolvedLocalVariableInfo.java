package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ローカル変数を表すためのクラス． 以下の情報を持つ．
 * <ul>
 * <li>変数名</li>
 * <li>未解決型名</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public final class UnresolvedLocalVariableInfo
        extends
        UnresolvedVariableInfo<LocalVariableInfo, UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo>> {

    /**
     * ローカル変数ブジェクトを初期化する．
     * 
     * @param name 変数名
     * @param type 未解決型名
     * @param definitionSpace 宣言しているローカル空間
     * @param initializer ローカル変数の初期化式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedLocalVariableInfo(final String name, final UnresolvedTypeInfo<?> type,
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> definitionSpace,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(name, type, definitionSpace, fromLine, fromColumn, toLine, toColumn);

        this.initializer = initializer;
    }

    /**
     * 未解決ローカル変数情報を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決ローカル変数の定義が行われているクラス
     * @param usingMethod 未解決ローカル変数の定義が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済みローカル変数情報
     */
    @Override
    public LocalVariableInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 修飾子，変数名，型を取得
        final Set<ModifierInfo> localModifiers = this.getModifiers();
        final String variableName = this.getName();
        final UnresolvedTypeInfo<?> unresolvedVariableType = this.getType();
        TypeInfo variableType = unresolvedVariableType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert variableType != null : "resolveTypeInfo returned null!";
        if (variableType instanceof UnknownTypeInfo) {
            if (unresolvedVariableType instanceof UnresolvedClassReferenceInfo) {

                final ExternalClassInfo externalClass = UnresolvedClassReferenceInfo
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedVariableType);
                variableType = new ClassTypeInfo(externalClass);
                for (final UnresolvedTypeInfo<?> unresolvedTypeArgument : ((UnresolvedClassReferenceInfo) unresolvedVariableType)
                        .getTypeArguments()) {
                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                            usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                    ((ClassTypeInfo) variableType).addTypeArgument(typeArgument);
                }
                classInfoManager.add(externalClass);

            } else if (unresolvedVariableType instanceof UnresolvedArrayTypeInfo) {

                // TODO 型パラメータの情報を格納する
                final UnresolvedTypeInfo<?> unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedVariableType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolve(usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager);
                variableType = ArrayTypeInfo.getType(elementType, dimension);

            } else {
                assert false : "Can't resolve method local variable type : "
                        + unresolvedVariableType.toString();
            }
        }
        final int localFromLine = this.getFromLine();
        final int localFromColumn = this.getFromColumn();
        final int localToLine = this.getToLine();
        final int localToColumn = this.getToColumn();

        final LocalSpaceInfo definitionSpace = this.getDefinitionUnit().resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // ローカル変数オブジェクトを生成し，MethodInfoに追加
        this.resolvedInfo = new LocalVariableInfo(localModifiers, variableName, variableType,
                definitionSpace, localFromLine, localFromColumn, localToLine, localToColumn);
        return this.resolvedInfo;
    }

    /**
     * 変数の初期化式を返す
     * @return 変数の初期化式．初期化されていない場合はnull
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitilizer() {
        return this.initializer;
    }

    /**
     * 変数の初期化式を表す変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer;
}
