package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class SwitchBlockStateManager extends InnerBlockStateManager {

    public static enum SWITCH_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_ENTRY_DEF, EXIT_ENTRY_DEF, ENTER_CASE_ENTRY, EXIT_CASE_ENTYR, ENTER_DEFAULT_ENTRY, EXIT_DEFAULT_ENTRY
    }

/*    @Override
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.entered(event);

            if (this.isEntryDefinitionToken(token)) {

                final SWITCH_BLOCK_STATE_CHANGE stateChangeType = getEntryEnterEvent(token);
                if (null != stateChangeType) {
                    this.fireStateChangeEvent(stateChangeType, event);
                }
            }
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.exited(event);

            if (this.isEntryDefinitionToken(token)) {

                final SWITCH_BLOCK_STATE_CHANGE stateChangeType = getEntryExitEvent(token);
                if (null != stateChangeType) {
                    this.fireStateChangeEvent(stateChangeType, event);
                }
            }
        }
    }*/

    @Override
    protected boolean fireStateChangeEnterEvent(final AstVisitEvent event) {
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeEnterEvent(event)) {
            return true;
        }

        final AstToken token = event.getToken();
        if (this.isEntryDefinitionToken(token)){
            final SWITCH_BLOCK_STATE_CHANGE stateChangeType = getEntryEnterEvent(token);
            if (null != stateChangeType) {
                this.fireStateChangeEvent(stateChangeType, event);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean fireStateChangeExitEvent(AstVisitEvent event) {
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeExitEvent(event)) {
            return true;
        }
        
        final AstToken token = event.getToken();
        if (this.isEntryDefinitionToken(token)) {
            final SWITCH_BLOCK_STATE_CHANGE stateChangeType = getEntryExitEvent(token);
            if (null != stateChangeType) {
                this.fireStateChangeEvent(stateChangeType, event);
            }
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return super.isStateChangeTriggerEvent(event) || isEntryDefinitionToken(event.getToken());
    }

    private SWITCH_BLOCK_STATE_CHANGE getEntryEnterEvent(final AstToken token) {
        if (this.isCaseEntryToken(token)) {
            return SWITCH_BLOCK_STATE_CHANGE.ENTER_CASE_ENTRY;
        } else if (this.isDefaultEntryToken(token)) {
            return SWITCH_BLOCK_STATE_CHANGE.ENTER_DEFAULT_ENTRY;
        } else {
            return null;
        }
    }

    private SWITCH_BLOCK_STATE_CHANGE getEntryExitEvent(final AstToken token) {
        if (this.isCaseEntryToken(token)) {
            return SWITCH_BLOCK_STATE_CHANGE.EXIT_CASE_ENTYR;
        } else if (this.isDefaultEntryToken(token)) {
            return SWITCH_BLOCK_STATE_CHANGE.EXIT_DEFAULT_ENTRY;
        } else {
            return null;
        }
    }

    private boolean isEntryDefinitionToken(final AstToken token) {
        return this.isDefaultEntryToken(token) || this.isCaseEntryToken(token);
    }

    private final boolean isDefaultEntryToken(final AstToken token) {
        return token.isDefault();
    }

    private final boolean isCaseEntryToken(final AstToken token) {
        return token.isCase();
    }

    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isSwitch();
    }

}
