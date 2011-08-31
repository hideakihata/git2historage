package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決キャスト使用を表すクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedCastUsageInfo extends UnresolvedExpressionInfo<CastUsageInfo> {

    /**
     * キャストされたエンティティとキャストの型を与えて初期化
     * 
     * @param castType キャストの型
     * @param castedUsage キャストされたエンティティ
     * 
     */
    public UnresolvedCastUsageInfo(final UnresolvedTypeInfo<?> castType,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> castedUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == castType || null == castedUsage) {
            throw new IllegalArgumentException();
        }

        this.castType = castType;
        this.castedUsage = castedUsage;
    }

    /**
     * キャストした型を返す
     * @return キャストした型
     */
    public UnresolvedTypeInfo<?> getCastType() {
        return this.castType;
    }

    /**
     * キャストが行われたエンティティ使用を返す
     * @return キャストが行われたエンティティ使用
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getCastedUsage() {
        return this.castedUsage;
    }

    @Override
    public CastUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 使用位置を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // キャスト型使用を解決
        final UnresolvedTypeInfo<?> unresolvedCastType = this.getCastType();
        final TypeInfo castType = unresolvedCastType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // キャストされたエンティティ使用を解決
        final ExpressionInfo castedUsage = this.getCastedUsage().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // 要素使用のオーナー要素を返す
        /*final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        // キャスト使用を解決
        this.resolvedInfo = new CastUsageInfo(castType, castedUsage, usingMethod, fromLine,
                fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/

        return this.resolvedInfo;
    }

    /**
     * キャストした型を保存する変数
     */
    private final UnresolvedTypeInfo<?> castType;

    /**
     * キャストが行われたエンティティ使用を保存すための変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> castedUsage;

}
