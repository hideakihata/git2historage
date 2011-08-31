package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class AssertStatementStateManager extends SingleStatementStateManager {

    public AssertStatementStateManager() {
        super();

        this.expressionCountStack = new Stack<Integer>();
    }

    protected static enum STATE implements SingleStatementState {
        ASSERT_EXPRESSION, MESSAGE_EXPRESSION
    }

    public static enum ASSERT_STATEMENT_STATE_CHANGE implements StateChangeEventType {

        ENTER, EXIT,

        ENTER_ASSERT_EXPRESSION, EXIT_ASSERT_EXPRESSION,

        ENTER_MESSAGE_EXPRESSION, EXIT_MESSAGE_EXPRESSION
    }

    @Override
    protected void fireStateChangeEnterEvent(final AstVisitEvent event) {
        super.fireStateChangeEnterEvent(event);

        final AstToken token = event.getToken();
        if (this.isHeaderToken(token)) {
            this.expressionCountStack.push(0);
        } else if (this.isInStatement() && token.isExpression()) {
            switch (this.getAnalyzedExpressionCount()) {
            case 0:
                this.setState(STATE.ASSERT_EXPRESSION);
                this.fireStateChangeEvent(ASSERT_STATEMENT_STATE_CHANGE.ENTER_ASSERT_EXPRESSION,
                        event);
                break;
            case 1:
                this.setState(STATE.MESSAGE_EXPRESSION);
                this.fireStateChangeEvent(ASSERT_STATEMENT_STATE_CHANGE.ENTER_MESSAGE_EXPRESSION,
                        event);
                break;
            default:
                assert false : "Illegal state: too many expressions";
                break;
            }
        }
    }

    @Override
    protected void fireStateChangeExitEvent(AstVisitEvent event) {
        super.fireStateChangeExitEvent(event);

        final AstToken token = event.getToken();
        if (this.isHeaderToken(token)) {
            this.expressionCountStack.pop();
        } else if (this.isInStatement() && token.isExpression()) {
            switch (this.getAnalyzedExpressionCount()) {
            case 0:
                this.fireStateChangeEvent(ASSERT_STATEMENT_STATE_CHANGE.EXIT_ASSERT_EXPRESSION,
                        event);
                this.incrementExpressionCout();
                break;
            case 1:
                this.fireStateChangeEvent(ASSERT_STATEMENT_STATE_CHANGE.EXIT_MESSAGE_EXPRESSION,
                        event);
                this.incrementExpressionCout();
                break;
            default:
                break;
            }

        }
    }

    @Override
    public StateChangeEventType getStatementEnterEventType() {
        return ASSERT_STATEMENT_STATE_CHANGE.ENTER;
    }

    @Override
    public StateChangeEventType getStatementExitEventType() {
        return ASSERT_STATEMENT_STATE_CHANGE.EXIT;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(AstVisitEvent event) {
        final AstToken token = event.getToken();
        return token.isAssertStatement() || token.isExpression();
    }

    @Override
    protected boolean isHeaderToken(AstToken token) {
        return token.isAssertStatement();
    }

    private final void incrementExpressionCout() {
        final int oldValue = this.expressionCountStack.pop();
        this.expressionCountStack.push(oldValue + 1);
    }

    private final int getAnalyzedExpressionCount() {
        return this.expressionCountStack.peek();
    }

    private final Stack<Integer> expressionCountStack;
}
