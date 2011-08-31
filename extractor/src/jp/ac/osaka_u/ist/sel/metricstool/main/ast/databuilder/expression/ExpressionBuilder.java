package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public abstract class ExpressionBuilder extends StateDrivenDataBuilder<ExpressionElement> {

    public ExpressionBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {

        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;

        this.addStateManager(this.expressionStateManger);
    }

    @Override
    public final void entered(AstVisitEvent e) {
        super.entered(e);

        final AstToken token = e.getToken();

        if (this.isRelated(token) || isExpressionDelimiterToken(token)) {
            this.expressionStackCountStack.push(this.expressionManager.getExpressionStackSize());
        }
    }

    @Override
    public void exited(final AstVisitEvent e) throws ASTParseException {
        final AstToken token = e.getToken();

        final boolean isRelated = this.isRelated(token);
        if (isRelated || isExpressionDelimiterToken(token)) {
            assert (!this.expressionStackCountStack.isEmpty()) : "Illegal state: illegal stack size.";

            final int availableElementCount = this.expressionManager.getExpressionStackSize()
                    - this.expressionStackCountStack.pop();

            assert (0 <= availableElementCount) : "Illegal state: illegal stack size.";

            this.elementBuffer.clear();

            for (int i = 0; i < availableElementCount; i++) {
                this.elementBuffer.add(0, this.expressionManager.popExpressionElement());
            }
        }

        super.exited(e);

        if (isRelated) {
            afterExited(e);
        }

    }

    protected boolean isRelated(final AstToken token) {
        return this.isActive() && isInExpression() && this.isTriggerToken(token);
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {

    }

    protected abstract void afterExited(AstVisitEvent event) throws ASTParseException;

    protected boolean isInExpression() {
        return this.expressionStateManger.inExpression();
    }

    protected abstract boolean isTriggerToken(AstToken token);

    protected final ExpressionElement[] getAvailableElements() {
        return this.elementBuffer.toArray(new ExpressionElement[this.elementBuffer.size()]);
    }

    protected final void pushElement(ExpressionElement element) {
        this.expressionManager.pushExpressionElement(element);
    }

    private boolean isExpressionDelimiterToken(AstToken token) {
        return token.isBlock();
    }

    protected final ExpressionElementManager expressionManager;

    protected final BuildDataManager buildDataManager;

    private final ExpressionStateManager expressionStateManger = new ExpressionStateManager();

    private final Stack<Integer> expressionStackCountStack = new Stack<Integer>();

    private final List<ExpressionElement> elementBuffer = new LinkedList<ExpressionElement>();
}
