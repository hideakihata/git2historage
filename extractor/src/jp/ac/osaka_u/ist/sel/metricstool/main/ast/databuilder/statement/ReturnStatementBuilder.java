package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReturnStatementInfo;


/**
 * リターン文の情報を構築するクラス
 * 
 * @author t-miyake
 *
 */
public class ReturnStatementBuilder extends SingleStatementBuilder<UnresolvedReturnStatementInfo> {

    /**
     * 構築済みの式情報マネージャー，構築済みデータマネージャーを与えて初期化．
     * 
     * @param expressionManager 構築済み式情報マネージャー
     * @param buildDataManager 構築済みデータマネージャー
     */
    public ReturnStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedReturnStatementInfo buildStatement(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        return new UnresolvedReturnStatementInfo(
                outerLocalSpace, fromLine, fromColumn, toLine, toColumn);
    }
    
    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);

        if (this.isTriggerToken(e.getToken())) {
            if (null != this.getLastBuildData()) {
                final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression = this
                        .getLastBuiltExpression();
                final UnresolvedReturnStatementInfo buildingStatement = this.getLastBuildData();

                // TODO いけてない.SingleStatementBuilderをStatetDrivenDataBuilderを継承するように変更すべき
                if (null != returnedExpression
                        && (returnedExpression.getToLine() < buildingStatement.getFromLine() 
                                || returnedExpression.getToLine() == buildingStatement.getFromLine()
                                && returnedExpression.getToColumn() < buildingStatement.getFromColumn())) {
                    buildingStatement.setReturnedExpression(
                            new UnresolvedEmptyExpressionInfo(
                                    buildingStatement.getOuterUnit(), 
                                    e.getEndLine(), e.getEndColumn() - 1, e.getEndLine(), e.getEndColumn() - 1));
                } else if (null == returnedExpression){
                    buildingStatement.setReturnedExpression(
                            new UnresolvedEmptyExpressionInfo(
                                    buildingStatement.getOuterUnit(), 
                                    e.getEndLine(), e.getEndColumn() - 1, e.getEndLine(), e.getEndColumn() - 1));
                } else {
                    buildingStatement.setReturnedExpression(returnedExpression);
                }
            }
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isReturn();
    }

}
