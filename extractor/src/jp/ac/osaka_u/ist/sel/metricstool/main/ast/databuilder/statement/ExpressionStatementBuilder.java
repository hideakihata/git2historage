package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


/**
 * 式文情報を構築するクラス．
 * 
 * @author t-miyake
 *
 */
public class ExpressionStatementBuilder extends
        SingleStatementBuilder<UnresolvedExpressionStatementInfo> {

    /**
     * 構築済み式情報マネージャー，構築済みデータマネージャーを与えて初期化
     * 
     * @param expressionManager 構築済み式情報マネージャー
     * @param buildDataManager 構築済みデータマネージャー
     */
    public ExpressionStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }
    
    @Override
    protected UnresolvedExpressionStatementInfo buildStatement(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedExpressionInfo<? extends ExpressionInfo> expression = this
                .getLastBuiltExpression();

        final UnresolvedExpressionStatementInfo expressionStatement = new UnresolvedExpressionStatementInfo(
                ownerSpace, expression);
        expressionStatement.setFromLine(fromLine);
        expressionStatement.setFromColumn(fromColumn);
        expressionStatement.setToLine(toLine);
        expressionStatement.setToColumn(toColumn);

        return expressionStatement;
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isExpressionStatement();
    }

}
