package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.SyntaxToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


public class BreakStatementBuilder extends JumpStatementBuilder<UnresolvedBreakStatementInfo> {

    public BreakStatementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }
    
    @Override
    protected UnresolvedBreakStatementInfo buildStatement(
            UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        return new UnresolvedBreakStatementInfo(ownerSpace, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return super.isTriggerToken(token) && token.equals(SyntaxToken.BREAK);
    }
}
