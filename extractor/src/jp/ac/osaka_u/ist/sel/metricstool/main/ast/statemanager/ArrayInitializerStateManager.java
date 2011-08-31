package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager.EXPR_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class ArrayInitializerStateManager extends
        StackedAstVisitStateManager<ArrayInitializerStateManager.STATE> {

    public ArrayInitializerStateManager() {
        this.setState(STATE.OUT);
    }

    @Override
    protected boolean isStateChangeTriggerEvent(AstVisitEvent event) {
        final AstToken token = event.getToken();
        return token.isExpression() || token.isArrayInitilizer();
    }

    public static enum ARRAY_INITILIZER_STATE implements StateChangeEventType {
        ENTER_EXPR, EXIT_EXPR, ENTER_ARRAY_INIT, EXIT_ARRAY_INIT
    }

    protected static enum STATE {
        OUT, IN_INITILIZER, IN_EXPRESSION,
    }

    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        final AstToken token = event.getToken();
        if (token.isArrayInitilizer()) {
            this.setState(STATE.IN_INITILIZER);
            this.fireStateChangeEvent(ARRAY_INITILIZER_STATE.ENTER_ARRAY_INIT, event);
        } else if (token.isExpression() && this.isInInitilizer()) {
            this.setState(STATE.IN_EXPRESSION);
            this.fireStateChangeEvent(EXPR_STATE.ENTER_EXPR, event);
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        final AstToken token = event.getToken();
        if (token.isArrayInitilizer()) {
            this.fireStateChangeEvent(ARRAY_INITILIZER_STATE.EXIT_ARRAY_INIT, event);
        } else if (token.isExpression() && this.isInInitilizer()) {
            this.fireStateChangeEvent(ARRAY_INITILIZER_STATE.EXIT_EXPR, event);
        }
    }

    public boolean isInInitilizer() {
        return this.getState().equals(STATE.IN_INITILIZER);
    }

}
