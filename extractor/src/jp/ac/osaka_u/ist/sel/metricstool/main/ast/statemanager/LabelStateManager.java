package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class LabelStateManager extends StackedAstVisitStateManager<LabelStateManager.STATE> {

    public LabelStateManager() {
        this.setState(STATE.OUT);
    }

    @Override
    protected boolean isStateChangeTriggerEvent(AstVisitEvent event) {
        final AstToken token = event.getToken();
        return token.isLabeledStatement() || token.isIdentifier() || token.isStatement();
    }

    public static enum LABEL_STATE implements StateChangeEventType {
        ENTER_LABEL_DEF, EXIT_LABEL_DEF, ENTER_LABEL_NAME, EXIT_LABEL_NAME, ENTER_LABELED_STATEMENT, EXIT_LABELED_STATEMENT
    }

    protected static enum STATE {
        OUT, IN_DEFINITION, IN_LABEL_NAME, IN_LABELED_STATEMENT,
    }

    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        final AstToken token = event.getToken();

        if (token.isStatement() && this.isInDefinition()) {
            this.setState(STATE.IN_LABELED_STATEMENT);
            this.fireStateChangeEvent(LABEL_STATE.ENTER_LABELED_STATEMENT, event);
        }

        if (token.isLabeledStatement()) {
            this.setState(STATE.IN_DEFINITION);
            this.fireStateChangeEvent(LABEL_STATE.ENTER_LABEL_DEF, event);
        } else if (token.isIdentifier() && this.isInDefinition()) {
            this.setState(STATE.IN_LABEL_NAME);
            this.fireStateChangeEvent(LABEL_STATE.ENTER_LABEL_NAME, event);
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        final AstToken token = event.getToken();
        if (token.isLabeledStatement()) {
            this.fireStateChangeEvent(LABEL_STATE.EXIT_LABEL_DEF, event);
        } else if (token.isIdentifier() && this.isInDefinition()) {
            // FIXME ƒ‰ƒxƒ‹‚Ü‚í‚è‚Å‹““®‚ª‚¨‚©‚µ‚©‚Á‚½‚ç‚±‚±‚©‚à
            this.setState(STATE.OUT);
            this.fireStateChangeEvent(LABEL_STATE.EXIT_LABEL_NAME, event);
        }
        
        if (token.isStatement() && this.isInDefinition()) {
            this.fireStateChangeEvent(LABEL_STATE.EXIT_LABELED_STATEMENT, event);
        }
    }

    private boolean isInDefinition() {
        return this.getState().equals(STATE.IN_DEFINITION);
    }

}
