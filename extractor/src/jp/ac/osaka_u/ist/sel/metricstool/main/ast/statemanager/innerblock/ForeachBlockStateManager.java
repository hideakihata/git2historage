package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * Foreach文のステートマネージャ
 * @author a-saitoh
 *
 */
public class ForeachBlockStateManager extends InnerBlockStateManager {

    public static enum FOREACH_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_FOREACH_VARIABLE, EXIT_FOREACH_VARIABLE, ENTER_FOREACH_EXPRESSION, EXIT_FOREACH_EXPRESSION;
    }

    public static enum FOREACH_BLOCK_STATE implements DeclaredBlockState {
        FOREACH_VARIABLE, FOREACH_EXPRESSION;
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
            if (this.isForeachVariable(token)) {
                this.fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.ENTER_FOREACH_VARIABLE, event);
                isFired = true;
            } else if (this.isForeachExpression(token)) {
                this.fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.ENTER_FOREACH_EXPRESSION,
                        event);
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
            if (this.isForeachVariable(token)) {
                this.fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.EXIT_FOREACH_VARIABLE, event);
                isFired = true;
            } else if (this.isForeachExpression(token)) {
                this
                        .fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.EXIT_FOREACH_EXPRESSION,
                                event);
                isFired = true;
            }
        }

        return isFired;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        return super.isStateChangeTriggerEvent(event) || this.isForeachVariable(token)
                || this.isForeachExpression(token);
    }

    protected boolean isForeachVariable(final AstToken token) {
        return token.isForeachVariable();
    }

    protected boolean isForeachExpression(final AstToken token) {
        return token.isForeachExpression();
    }

    @Override
    protected boolean isDefinitionEvent(AstVisitEvent event) {
        return event.getToken().isForeach();
    }

}
