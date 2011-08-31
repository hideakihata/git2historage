package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class ConditionStateManager extends
        StackedAstVisitStateManager<ConditionStateManager.ConditionState> {

    public ConditionStateManager(final AstToken[] triggerTokens) {
        if (null == triggerTokens) {
            throw new IllegalArgumentException("triggerTokens is null");
        }

        this.triggerTokens = new HashSet<AstToken>();
        for (final AstToken triggerToken : triggerTokens) {
            this.triggerTokens.add(triggerToken);
        }
    }

    public interface ConditionState {
    }

    protected static enum STATE implements ConditionState {
        OUT, IN, IN_EXPRESSION, IN_DECLARATION
    }

    public static enum CONDITION_STATE_CHANGE implements StateChangeEventType {
        ENTER_CONDITION, EXIT_CONDITION,

        ENTER_EXPRESSION, EXIT_EXPRESSION,

        ENTER_DECLARATION, EXIT_DECLARATION
    }

    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        if (this.isStateChangeTriggerEvent(event)) {
            final AstToken token = event.getToken();
            if (this.isTriggerToken(token)) {
                this.setState(STATE.IN);
                this.fireStateChangeEvent(CONDITION_STATE_CHANGE.ENTER_CONDITION, event);
            } else if (STATE.IN == this.getState()) {
                if (token.isExpression()) {
                    this.setState(STATE.IN_EXPRESSION);
                    this.fireStateChangeEvent(CONDITION_STATE_CHANGE.ENTER_EXPRESSION, event);
                } else if (token.isLocalVariableDefinition()) {
                    this.setState(STATE.IN_DECLARATION);
                    this.fireStateChangeEvent(CONDITION_STATE_CHANGE.ENTER_DECLARATION, event);
                }

            }
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        if (this.isStateChangeTriggerEvent(event)) {

            super.exited(event);
            final AstToken token = event.getToken();
            if (this.isTriggerToken(token)) {
                this.fireStateChangeEvent(CONDITION_STATE_CHANGE.EXIT_CONDITION, event);
            } else if (STATE.IN == this.getState()) {
                if (token.isExpression()) {
                    this.fireStateChangeEvent(CONDITION_STATE_CHANGE.EXIT_EXPRESSION, event);
                } else if (token.isLocalVariableDefinition()) {
                    this.fireStateChangeEvent(CONDITION_STATE_CHANGE.EXIT_DECLARATION, event);
                }
            }
        }
    }

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        return token.isExpression() || token.isLocalVariableDefinition()
                || token.isLocalParameterDefinition() || this.isTriggerToken(token);
    }

    protected boolean isTriggerToken(final AstToken token) {
        return this.triggerTokens.contains(token);
    }

    public void addTriggerToken(final AstToken triggerToken) {
        this.triggerTokens.add(triggerToken);
    }

    private final Set<AstToken> triggerTokens;
}
