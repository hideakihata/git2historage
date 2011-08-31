package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedForeachConditionInfo extends UnresolvedExpressionInfo<ForeachConditionInfo> {

    UnresolvedForeachConditionInfo(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(outerUnit, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public ForeachConditionInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

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

        // この foreach文の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 繰り返し用の式を取得
        final UnresolvedExpressionInfo<?> unresolvedIteratorExpression = this
                .getIteratorExpression();
        final ExpressionInfo iteratorExpression = unresolvedIteratorExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // 繰り返し用の変数を取得
        final UnresolvedVariableDeclarationStatementInfo unresolvedIteratorVariable = this
                .getIteratorVariable();
        final VariableDeclarationStatementInfo iteratorVariable = unresolvedIteratorVariable
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        this.resolvedInfo = new ForeachConditionInfo(usingMethod, fromLine, fromColumn, toLine,
                toColumn, iteratorVariable, iteratorExpression);
        return this.resolvedInfo;
    }

    public UnresolvedVariableDeclarationStatementInfo getIteratorVariable() {
        return this.iteratorVariable;
    }

    public void setIteratorVariable(
            final UnresolvedVariableDeclarationStatementInfo iteratorVariable) {
        this.iteratorVariable = iteratorVariable;
    }

    public UnresolvedExpressionInfo<?> getIteratorExpression() {
        return this.iteratorExpression;
    }

    public void setIteratorExpression(final UnresolvedExpressionInfo<?> iteratorExpression) {
        this.iteratorExpression = iteratorExpression;
    }

    private UnresolvedVariableDeclarationStatementInfo iteratorVariable;

    private UnresolvedExpressionInfo<?> iteratorExpression;
}
