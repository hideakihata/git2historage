package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class CatchBlockStateManager extends InnerBlockStateManager {

    public static enum CATCH_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_LOCAL_PARAMETER, EXIT_LOCAL_PARAMETER,
    }

    public static enum CATCH_BLOCK_STATE implements DeclaredBlockState {
        LOCAL_PARAMETER
    }

    @Override
    protected boolean fireStateChangeEnterEvent(final AstVisitEvent event) {
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeEnterEvent(event)) {
            return true;
        }

        if (this.isInPreDeclaration() && event.getToken().isLocalVariableDefinition()){
            this.setState(CATCH_BLOCK_STATE.LOCAL_PARAMETER);
            this.fireStateChangeEvent(CATCH_BLOCK_STATE_CHANGE.ENTER_LOCAL_PARAMETER, event);
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

        if (this.isInPreDeclaration() && event.getToken().isLocalVariableDefinition()){
            this.fireStateChangeEvent(CATCH_BLOCK_STATE_CHANGE.EXIT_LOCAL_PARAMETER, event);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isCatch();
    }

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return super.isStateChangeTriggerEvent(event)
                || event.getToken().isLocalVariableDefinition();
    }

}
