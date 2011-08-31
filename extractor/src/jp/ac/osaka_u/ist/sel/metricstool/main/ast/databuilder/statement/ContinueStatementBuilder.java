package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.SyntaxToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


public class ContinueStatementBuilder extends JumpStatementBuilder<UnresolvedContinueStatementInfo> {

    public ContinueStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedContinueStatementInfo buildStatement(
            UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        return new UnresolvedContinueStatementInfo(outerLocalSpace, fromLine, fromColumn, toLine,
                toColumn);
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return super.isTriggerToken(token) && token.equals(SyntaxToken.CONTINUE);
    }

}
