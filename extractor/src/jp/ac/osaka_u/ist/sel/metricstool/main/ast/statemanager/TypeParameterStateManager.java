package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ビジターが型パラメータ定義部へ出入りする際の状態を管理するステートマネージャ．
 * @author kou-tngt
 *
 */
public class TypeParameterStateManager extends
        StackedAstVisitStateManager<TypeParameterStateManager.STATE> {

    public TypeParameterStateManager() {
        this.setState(STATE.OUT);
    }

    /**
     * 送信する状態変化イベントの種類を表すenum
     * @author kou-tngt
     *
     */
    public enum TYPE_PARAMETER implements StateChangeEventType {
        ENTER_TYPE_PARAMETER_DEF, EXIT_TYPE_PARAMETER_DEF, ENTER_TYPE_UPPER_BOUNDS, EXIT_TYPE_UPPER_BOUNDS, ENTER_TYPE_LOWER_BOUNDS, EXIT_TYPE_LOWER_BOUNDS,
        ENTER_TYPE_ADDITIONAL_BOUNDS, EXIT_TYPE_ADDITIONAL_BOUNDS
    }

    /**
     * 型パラメータ定義部のノードに入った時に呼び出され，
     * 状態変化を引き起こして，状態変化イベントを通知する．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(AstVisitEvent event) {
        super.entered(event);

        AstToken token = event.getToken();

        if (token.isTypeParameterDefinition()) {
            this.setState(STATE.IN_PARAMETER_DEF);
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()) {
            this.setState(STATE.IN_LOWER_BOUNDS);
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()) {
            this.setState(STATE.IN_UPPER_BOUNDS);
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS, event);
        } else if (token.isTypeAdditionalBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_ADDITIONAL_BOUNDS, event);
        }
            
    }

    /**
     * 型パラメータ定義部のノードから出た時に呼び出され，
     * 状態変化を引き起こして，状態変化イベントを通知する．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(AstVisitEvent event) {
        super.exited(event);

        AstToken token = event.getToken();

        if (token.isTypeParameterDefinition()) {
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()) {
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()) {
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS, event);
        } else if (token.isTypeAdditionalBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_ADDITIONAL_BOUNDS, event);
        }
    }

    /**
     * ビジターの現在位置が型パラメータ定義部の中かどうかを返す
     * @return　ビジターの現在位置が型パラメータ定義部の中であればtrue
     */
    public boolean isEnterParameterDefinition() {
        return STATE.OUT != this.getState();
    }

    /**
     * ビジターの現在位置が型パラメータ定義部(上界宣言部に入る前)の中かどうかを返す
     * @return　ビジターの現在位置が型パラメータ定義部(上界宣言部に入る前)の中であればtrue
     */
    public boolean isInTypeParameterDefinition(){
        return STATE.IN_PARAMETER_DEF == this.getState();
    }
    

    /**
     * 型パラメータ定義部に関連するノードかどうかを判定する
     *  
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerEvent(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isTypeParameterDefinition() || token.isTypeLowerBoundsDescription()
                || token.isTypeUpperBoundsDescription();
    }

    /**
     * 状態を表すenum
     * 
     * @author kou-tngt
     *
     */
    protected enum STATE {
        OUT, IN_PARAMETER_DEF, IN_UPPER_BOUNDS, IN_LOWER_BOUNDS
    }
    

}
