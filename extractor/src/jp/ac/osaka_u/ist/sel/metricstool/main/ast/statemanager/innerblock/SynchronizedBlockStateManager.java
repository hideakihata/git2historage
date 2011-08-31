package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class SynchronizedBlockStateManager extends InnerBlockStateManager {

    public static enum SYNCHRONIZED_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_SYNCHRONIZED_EXPRESSION, EXIT_SYNCHRONIZED_EXPRESSION,
    }

    public static enum SYNCHRONIZED_BLOCK_STATE implements DeclaredBlockState {
        SYNCHRONIZED_EXPRESSION
    }

    @Override
    protected boolean fireStateChangeEnterEvent(final AstVisitEvent event) {
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeEnterEvent(event)) {
            return true;
        }

        if (this.isInPreDeclaration() && event.getToken().isExpression()){
            this.setState(SYNCHRONIZED_BLOCK_STATE.SYNCHRONIZED_EXPRESSION);
            this.fireStateChangeEvent(SYNCHRONIZED_BLOCK_STATE_CHANGE.ENTER_SYNCHRONIZED_EXPRESSION, event);
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

        if (this.isInPreDeclaration() && event.getToken().isExpression()){
            this.fireStateChangeEvent(SYNCHRONIZED_BLOCK_STATE_CHANGE.EXIT_SYNCHRONIZED_EXPRESSION, event);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return super.isStateChangeTriggerEvent(event)
                || event.getToken().isExpression();
    }
    
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isSynchronized() && !event.getParentToken().isModifiersDefinition();
    }

}
