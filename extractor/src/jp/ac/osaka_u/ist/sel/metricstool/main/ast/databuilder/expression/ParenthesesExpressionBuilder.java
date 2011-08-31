package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParenthesesExpressionInfo;


/**
 * 括弧式情報を構築するクラス 
 * <br>
 * 式を構成する要素ではあるが，式の構文木情報に影響を与えてはいけないため， 
 * {@link ExpressionBuilder}の子クラスにはしていない．
 * 
 * @author g-yamada
 * 
 */
public class ParenthesesExpressionBuilder extends
        StateDrivenDataBuilder<UnresolvedParenthesesExpressionInfo> {

    /**
     * オブジェクトを初期化する
     * 
     * @param expressionManager ExpressionElementManager
     * @param buildDataManager 
     */
    public ParenthesesExpressionBuilder(final ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;

        this.addStateManager(this.expressionStateManger);
    }

    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);
    }

    /**
     * exit したノードが括弧式なら，UnresolvedParenthesesExpressionInfoをつくる命令をする
     */
    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);
        final AstToken token = e.getToken();
        if (this.isActive() && this.expressionStateManger.inExpression()
                && token.isParenthesesExpression()) {
            this.buildParenthesesExpressionBuilder(e);
        }
    }

    /**
     * 命令されて実際にUnresolvedParenthesesExpressionInfoをつくる
     */
    protected void buildParenthesesExpressionBuilder(final AstVisitEvent e) {
        final ExpressionElement parentheticElement = expressionManager.popExpressionElement();
        final UnresolvedExpressionInfo<? extends ExpressionInfo> parentheticExpression = parentheticElement
                .getUsage();

        if (null != parentheticExpression) {
            // expressionAnalyzeStackの頭の要素をポップして，かわりに括弧式をプッシュする
            final UnresolvedParenthesesExpressionInfo paren = new UnresolvedParenthesesExpressionInfo(
                    parentheticExpression);
            paren.setOuterUnit(this.buildDataManager.getCurrentUnit());
            paren.setFromLine(e.getStartLine());
            paren.setFromColumn(e.getStartColumn());
            paren.setToLine(e.getEndLine());
            paren.setToColumn(e.getEndColumn());
            expressionManager.pushExpressionElement(new UsageElement(paren));
        } else if (parentheticElement instanceof IdentifierElement) {
            // (a) のように識別子のみを囲む括弧の場合，Usageが未解決であるため括弧Elementを作りpushする
            expressionManager.pushExpressionElement(new ParenthesizedIdentifierElement(
                    (IdentifierElement) parentheticElement, e.getStartLine(), e.getStartColumn(), e
                            .getEndLine(), e.getEndColumn()));
        } else {
            // here shouldn't be reached
            throw new IllegalStateException();
        }
    }

    //public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariableIfPoss

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        // nothing to do
    }

    protected final ExpressionElementManager expressionManager;

    protected final BuildDataManager buildDataManager;

    private final ExpressionStateManager expressionStateManger = new ExpressionStateManager();
}