package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.EnterExitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class JavaClassImportStateManager extends EnterExitStateManager{
    public static enum IMPORT_STATE_CHANGE implements StateChangeEventType{
        ENTER_CLASS_IMPORT,
        EXIT_CLASS_IMPORT
    }
    
    @Override
    public  StateChangeEventType getEnterEventType() {
        return IMPORT_STATE_CHANGE.ENTER_CLASS_IMPORT;
    }

    @Override
    public  StateChangeEventType getExitEventType() {
        return IMPORT_STATE_CHANGE.EXIT_CLASS_IMPORT;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(AstVisitEvent event) {
        return event.getToken().equals(JavaAstToken.CLASS_IMPORT);
    }
}
