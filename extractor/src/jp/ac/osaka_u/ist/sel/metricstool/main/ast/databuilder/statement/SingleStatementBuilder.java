package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;


/**
 * 文の情報を構築するクラス
 * 複文以外（式文，return文，throw文，break文など）の情報を構築する．
 * 
 * @author t-miyake
 *
 * @param <T> 構築される文の型，UnresolvedStatementInfoのサブクラスでなければならない．
 */
public abstract class SingleStatementBuilder<T extends UnresolvedStatementInfo<? extends StatementInfo>>
        extends CompoundDataBuilder<T> {

    /**
     * 式情報マネージャー，構築済みデータマネージャーを与えて初期化
     * 
     * @param expressionManager 式情報マネージャー
     * @param buildDataManager 構築済みデータマネージャー
     */
    public SingleStatementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {

        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        
    }    

    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);
        if (this.isTriggerToken(e.getToken())) {
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this.buildDataManager
                    .getCurrentLocalSpace();

            if (null != currentLocal) {
                final T singleStatement = this.buildStatement(currentLocal, e.getStartLine(), e
                        .getStartColumn(), e.getEndLine(), e.getEndColumn());

                assert singleStatement != null : "Illegal state: a single statement was not built";

                currentLocal.addStatement(singleStatement);

                this.registBuiltData(singleStatement);
            }
        }
    }

    /**
     * 過去に構築された式情報のうち最新の式情報を返す．
     * 
     * @return 過去構築された式情報のうち最新の式情報
     */
    protected UnresolvedExpressionInfo<? extends ExpressionInfo> getLastBuiltExpression() {
        return null == this.expressionManager.getPeekExpressionElement() ? null
                : this.expressionManager.getPeekExpressionElement().getUsage();
    }

    /**
     * 文の情報を構築する．
     * 
     * @param fromLine 文の開始行
     * @param fromColumn 文の開始列
     * @param toLine 文の終了行
     * @param toColumn 文の終了列
     * @return
     */
    protected abstract T buildStatement(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn);

    /**
     * 引数で与えられたトークンが構築される文を表すノードのトークンであるかどうか返す
     * 
     * @param token トークン
     * @return 引数で与えられたトークンが構築される文を表すノードのトークンであればtrue
     */
    protected abstract boolean isTriggerToken(final AstToken token);

    /**
     * 構築済み式情報マネージャーを表すフィールド
     */
    protected final ExpressionElementManager expressionManager;

    /**
     * 構築済みデータマネージャーを表すフィールド
     */
    protected final BuildDataManager buildDataManager;
}
