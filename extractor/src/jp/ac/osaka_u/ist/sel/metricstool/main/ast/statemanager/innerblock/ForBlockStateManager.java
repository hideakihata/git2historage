package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class ForBlockStateManager extends InnerBlockStateManager {

    public static enum FOR_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_FOR_INIT, EXIT_FOR_INIT, ENTER_FOR_ITERATOR, EXIT_FOR_ITERATOR;

    }

    public static enum FOR_BLOCK_STATE implements DeclaredBlockState {
        FOR_INIT, FOR_ITERATOR,
    }

    @Override
    protected boolean fireStateChangeEnterEvent(final AstVisitEvent event) {
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeEnterEvent(event)) {
            return true;
        }

        boolean isFired = false;

        if (STATE.DECLARE == this.getState()) {
            final AstToken token = event.getToken();
            if (this.isForInitClause(token)) {
                this.setState(FOR_BLOCK_STATE.FOR_INIT);
                this.fireStateChangeEvent(FOR_BLOCK_STATE_CHANGE.ENTER_FOR_INIT, event);
                isFired = true;
            } else if (this.isForIteratorClause(token)) {
                this.setState(FOR_BLOCK_STATE.FOR_ITERATOR);
                this.fireStateChangeEvent(FOR_BLOCK_STATE_CHANGE.ENTER_FOR_ITERATOR, event);
                isFired = true;
            }
        }

        return isFired;
    }

    @Override
    protected boolean fireStateChangeExitEvent(AstVisitEvent event) {
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeExitEvent(event)) {
            return true;
        }

        boolean isFired = false;

        if (STATE.DECLARE == this.getState()) {
            final AstToken token = event.getToken();
            if (this.isForInitClause(token)) {
                this.fireStateChangeEvent(FOR_BLOCK_STATE_CHANGE.EXIT_FOR_INIT, event);
                isFired = true;
            } else if (this.isForIteratorClause(token)) {
                this.fireStateChangeEvent(FOR_BLOCK_STATE_CHANGE.EXIT_FOR_ITERATOR, event);
                isFired = true;
            } 
        }

        return isFired;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        return super.isStateChangeTriggerEvent(event) || this.isConditionalClause(token)
                || this.isForInitClause(token) || this.isForIteratorClause(token);
    }

    protected boolean isForInitClause(final AstToken token) {
        return token.isForInit();
    }

    protected boolean isForIteratorClause(final AstToken token) {
        return token.isForIterator();
    }

    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isFor();
    }

}
