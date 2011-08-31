package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決括弧式を表すクラス
 * 
 * @author higo
 *
 */
public class UnresolvedParenthesesExpressionInfo extends
        UnresolvedExpressionInfo<ParenthesesExpressionInfo> {

    /**
     * オブジェクトを初期化
     * 
     * @param parentheticExpression 未解決な括弧内の式
     */
    public UnresolvedParenthesesExpressionInfo(
            final UnresolvedExpressionInfo<?> parentheticExpression) {

        if (null == parentheticExpression) {
            throw new IllegalArgumentException();
        }

        this.parentheticExpression = parentheticExpression;
    }

    /**
     * 未解決カッコ内の式を返す
     * 
     * @return 未解決カッコ内の式
     */
    public UnresolvedExpressionInfo<?> getParentheticExpression() {
        return this.parentheticExpression;
    }

    /**
     * 名前解決を行う
     */
    @Override
    public ParenthesesExpressionInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final UnresolvedExpressionInfo<?> unresolvedParentheticExpression = this
                .getParentheticExpression();
        final ExpressionInfo parentheticExpression = unresolvedParentheticExpression.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ParenthesesExpressionInfo(parentheticExpression, usingMethod,
                fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     *　括弧内の式を保存するための変数
     */
    final UnresolvedExpressionInfo<?> parentheticExpression;
}
