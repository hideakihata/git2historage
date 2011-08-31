package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * 型参照やメソッド呼び出し時に与えられた型引数を表すノードに，
 * ASTビジターが入っていく時の状態管理を行う．
 * 
 * @author kou-tngt
 *
 */
public class TypeArgumentStateManager extends
        StackedAstVisitStateManager<TypeArgumentStateManager.STATE> {

    public TypeArgumentStateManager() {
        this.setState(STATE.OUT);
    }

    /**
     * 通知するイベントのタイプを表すenum
     * @author kou-tngt
     *
     */
    public static enum TYPE_ARGUMENT_STATE implements StateChangeEventType {
        ENTER_TYPE_ARGUMENTS, EXIT_TYPE_ARGUMENTS, ENTER_TYPE_ARGUMENT, EXIT_TYPE_ARGUMENT, ENTER_TYPE_WILDCARD, EXIT_TYPE_WILDCARD,
    }

    /**
     * ビジターがノードに入った時のイベントを受け取り，
     * そのノードが型引数に関連するものであれば状態遷移を行ってイベントを発行する．
     * @param ビジターが通知するAST訪問イベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        final AstToken token = event.getToken();

        if (token.isTypeArgument()) {
            this.setState(STATE.IN_ARGUMENT);
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.ENTER_TYPE_ARGUMENT, event);
        } else if (token.isTypeArguments()) {
            this.setState(STATE.IN_ARGUMENTS);
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.ENTER_TYPE_ARGUMENTS, event);
        } else if (token.isTypeWildcard()) {
            this.setState(STATE.IN_WILDCARD);
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.ENTER_TYPE_WILDCARD, event);
        }
    }

    /**
     * ビジターがノードから出た時のイベントを受け取り，
     * そのノードが型引数に関連するものであれば状態遷移を行ってイベントを発行する．
     * @param ビジターが通知するAST訪問イベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        final AstToken token = event.getToken();

        if (token.isTypeArgument()) {
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENT, event);
        } else if (token.isTypeArguments()) {
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.EXIT_TYPE_ARGUMENTS, event);
        } else if (token.isTypeWildcard()) {
            this.fireStateChangeEvent(TYPE_ARGUMENT_STATE.EXIT_TYPE_WILDCARD, event);
        }
    }

    /**
     * 引数eventが状態遷移の引き金になるイベントかどうかを返す．
     * @param event 状態遷移の引き金になるかどうかを調べたいイベント
     * @return 状態遷移の引き金になるトークンならtrue
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerEvent(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isTypeArgument() || token.isTypeArguments() || token.isTypeWildcard();
    }

    /**
     * 状態を表すenum
     * @author kou-tngt
     *
     */
    protected enum STATE {
        OUT, IN_ARGUMENTS, IN_ARGUMENT, IN_WILDCARD
    }

}
