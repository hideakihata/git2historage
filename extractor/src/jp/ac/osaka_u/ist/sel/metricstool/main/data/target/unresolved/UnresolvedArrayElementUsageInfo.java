package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列に対する要素の参照を表すためのクラス．以下の情報を持つ．
 * 
 * @author kou-tngt, higo
 * @see UnresolvedExpressionInfo
 */
public final class UnresolvedArrayElementUsageInfo extends
        UnresolvedExpressionInfo<ArrayElementUsageInfo> {

    /**
     * 要素が参照された配列の型を与える.
     * 
     * @param qualifierArrayType 要素が参照された配列の型
     * @param indexExpression 参照された要素のインデックス
     */
    public UnresolvedArrayElementUsageInfo(final UnresolvedExpressionInfo<?> qualifierArrayType,
            final UnresolvedExpressionInfo<?> indexExpression) {

        if (null == qualifierArrayType) {
            throw new NullPointerException("ownerArrayType is null.");
        }
        this.qualifierArrayType = qualifierArrayType;
        this.indexExpression = indexExpression;
        this.resolvedInfo = null;
    }

    /**
     * 未解決配列要素の参照を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決配列要素参照が行われているクラス
     * @param usingMethod 未解決配列要素参照が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み参照
     */
    @Override
    public ArrayElementUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 要素使用のインデックスを名前解決
        final UnresolvedExpressionInfo<?> unresolvedIndexExpression = this.getIndexExpression();
        final ExpressionInfo indexExpression = unresolvedIndexExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert indexExpression != null : "method \"resolve\" returned null!";

        // 要素使用がくっついている("."の前のこと)未定義型を取得
        final UnresolvedExpressionInfo<?> unresolvedQualifierUsage = this.getQualifierArrayType();
        ExpressionInfo qualifierUsage = unresolvedQualifierUsage.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert qualifierUsage != null : "method \"resolve\" returned null!";

        // 親が特定できない場合も配列の要素使用を作成して返す
        // もしかすると，UnknownEntityUsageInfoを返す方が適切かもしれない
        this.resolvedInfo = new ArrayElementUsageInfo(indexExpression, qualifierUsage, usingMethod,
                fromLine, fromColumn, toLine, toColumn);

        return this.resolvedInfo;
    }

    /**
     * 要素が参照された配列の型を返す
     * 
     * @return 要素が参照された配列の型
     */
    public UnresolvedExpressionInfo<?> getQualifierArrayType() {
        return this.qualifierArrayType;
    }

    /**
     * 参照された要素のインデックスを返す
     * 
     * @return　参照された要素のインデックス
     */
    public UnresolvedExpressionInfo<?> getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * 要素が参照された配列の型
     */
    private final UnresolvedExpressionInfo<?> qualifierArrayType;

    /**
     * 配列要素使用のインデックスを格納する変数
     */
    private final UnresolvedExpressionInfo<?> indexExpression;

}
