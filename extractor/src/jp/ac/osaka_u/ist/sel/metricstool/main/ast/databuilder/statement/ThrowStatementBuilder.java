package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedThrowStatementInfo;


/**
 * Throw文の情報を構築するクラス
 * 
 * @author t-miyake
 *
 */
public class ThrowStatementBuilder extends SingleStatementBuilder<UnresolvedThrowStatementInfo> {

    /**
     * 構築済み式情報マネージャー，構築済みデータマネージャーを与えて初期化
     * 
     * @param expressionManager 構築済み式情報マネージャー
     * @param buildDataManager 構築済みデータマネージャー
     */
    public ThrowStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedThrowStatementInfo buildStatement(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        final UnresolvedThrowStatementInfo throwStatement = new UnresolvedThrowStatementInfo(
                ownerSpace);
        throwStatement.setFromLine(fromLine);
        throwStatement.setFromColumn(fromColumn);
        throwStatement.setToLine(toLine);
        throwStatement.setToColumn(toColumn);

        return throwStatement;
    }

    @Override
    public void exited(AstVisitEvent e)  throws ASTParseException  {
        super.exited(e);

        if (this.isTriggerToken(e.getToken())) {
            if (null != this.getLastBuildData()) {
                final UnresolvedExpressionInfo<? extends ExpressionInfo> thrownStatement = this
                        .getLastBuiltExpression();

                assert null != thrownStatement : "Illegal state: the thrown statement was not found.";
                this.getLastBuildData().setThrownExpresasion(thrownStatement);
            }
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isThrow();
    }

}
