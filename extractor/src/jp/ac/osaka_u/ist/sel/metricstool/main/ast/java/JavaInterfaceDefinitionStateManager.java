package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.DeclaredBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class JavaInterfaceDefinitionStateManager extends DeclaredBlockStateManager{
    
    public static enum INTERFACE_STATE_CHANGE implements StateChangeEventType{
        ENTER_INTERFACE_DEFINITION,
        ENTER_INTERFACE_BLOCK,
        EXIT_INTERFACE_BLOCK,
        EXIT_INTERFACE_DEFINITION,
    }

    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return INTERFACE_STATE_CHANGE.ENTER_INTERFACE_BLOCK;
    }

    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return INTERFACE_STATE_CHANGE.EXIT_INTERFACE_BLOCK;
    }

    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return INTERFACE_STATE_CHANGE.ENTER_INTERFACE_DEFINITION;
    }

    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return INTERFACE_STATE_CHANGE.EXIT_INTERFACE_DEFINITION;
    }

    @Override
    protected boolean isDefinitionEvent(AstVisitEvent event) {
        return event.getToken().equals(JavaAstToken.INTERFACE_DEFINITION);
    }

}
