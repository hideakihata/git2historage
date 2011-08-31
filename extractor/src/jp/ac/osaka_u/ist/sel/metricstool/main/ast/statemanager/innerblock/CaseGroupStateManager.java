package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.SyntaxToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class CaseGroupStateManager extends InnerBlockStateManager {

    public static enum CASE_GROUP_STATE_CHANGE implements StateChangeEventType {
        ENTER_ENTRY_DEF, EXIT_ENTRY_DEF,
        ENTER_BREAK_STATEMENT, EXIT_BREAK_STATEMENT
    }
    
    public static enum CASE_ENTRY_STATE implements DeclaredBlockState {
        CASE_DEF, DEFAULT_DEF
    }
    
    @Override
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.entered(event);

            if (this.isEntryDefinitionToken(token) && STATE.DECLARE == this.getState()) {
                if(token.isCase()) {
                    this.setState(CASE_ENTRY_STATE.CASE_DEF);
                } else if(token.isDefault()) {
                    this.setState(CASE_ENTRY_STATE.DEFAULT_DEF);
                }
                
                this.fireStateChangeEvent(CASE_GROUP_STATE_CHANGE.ENTER_ENTRY_DEF, event);
            } else if(this.isBreakStatement(token) && STATE.BLOCK == this.getState()) {
                this.fireStateChangeEvent(CASE_GROUP_STATE_CHANGE.ENTER_BREAK_STATEMENT, event);
            }
        }
    }
    
    @Override
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.exited(event);

            if (this.isEntryDefinitionToken(token) && STATE.DECLARE == this.getState()) {
                this.fireStateChangeEvent(CASE_GROUP_STATE_CHANGE.EXIT_ENTRY_DEF, event);
            } else if(STATE.BLOCK == this.getState() && this.isBreakStatement(token)) {
                this.fireStateChangeEvent(CASE_GROUP_STATE_CHANGE.EXIT_BREAK_STATEMENT, event);
            }
        }
    }
    
    protected boolean isEntryDefinitionToken(final AstToken token) {
        return token.isEntryDefinition();
    }
    
    protected boolean isBreakStatement(final AstToken token) {
        return token.isJump() && token.equals(SyntaxToken.BREAK);
    }
    
    @Override
    protected final boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        return super.isStateChangeTriggerEvent(event) || isEntryDefinitionToken(token) || isBreakStatement(token);
    }
    
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isCaseGroupDefinition();
    }

    @Override
    protected boolean isBlockToken(AstToken token) {
        return token.isSList();
    }
    
    
}
