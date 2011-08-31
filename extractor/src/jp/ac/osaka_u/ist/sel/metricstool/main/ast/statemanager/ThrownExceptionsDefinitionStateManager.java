package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

/**
 * throws のステートマネージャ．throwsトークンに出入りするときにイベントを発行する
 * @author g-yamada
 *
 */
public class ThrownExceptionsDefinitionStateManager extends EnterExitStateManager {
    
    public static enum THROWS_DEFINITION_STATE implements StateChangeEventType {
        ENTER_THROWS_DEF, EXIT_THROWS_DEF
    }

    @Override
    public StateChangeEventType getEnterEventType() {
        return THROWS_DEFINITION_STATE.ENTER_THROWS_DEF;
    }

    @Override
    public StateChangeEventType getExitEventType() {
        return THROWS_DEFINITION_STATE.EXIT_THROWS_DEF;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(AstVisitEvent event) {
        return event.getToken().isThrows();
    }

}
