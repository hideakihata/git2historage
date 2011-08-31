package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public abstract class SingleStatementStateManager extends
        StackedAstVisitStateManager<SingleStatementStateManager.SingleStatementState> {

    public interface SingleStatementState {
    }

    protected static enum STATE implements SingleStatementState {
        IN, OUT
    }

    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        if (this.isStateChangeTriggerEvent(event)) {
            //状態変化トリガなら
            fireStateChangeEnterEvent(event);
        }
    }

    protected void fireStateChangeEnterEvent(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isHeaderToken(token)) {
            //定義ノードなら状態遷移してイベントを発行
            this.setState(STATE.IN);
            this.fireStateChangeEvent(this.getStatementEnterEventType(), event);
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {

        if (this.isStateChangeTriggerEvent(event)) {
            //状態変化トリガなら

            //スタックの一番上の状態に戻す
            super.exited(event);

            fireStateChangeExitEvent(event);
        }
    }

    protected void fireStateChangeExitEvent(final AstVisitEvent event) {
        if (this.isHeaderToken(event.getToken())) {
            //定義ノードならイベントを発行
            this.fireStateChangeEvent(this.getStatementExitEventType(), event);
        }
    }
    
    protected boolean isInStatement() {
        return STATE.IN == this.getState();
    }

    public abstract StateChangeEventType getStatementEnterEventType();

    public abstract StateChangeEventType getStatementExitEventType();

    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return this.isHeaderToken(event.getToken());
    }

    protected abstract boolean isHeaderToken(final AstToken token);

}
