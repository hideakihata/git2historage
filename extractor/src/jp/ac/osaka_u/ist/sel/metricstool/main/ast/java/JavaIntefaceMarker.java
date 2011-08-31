package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaInterfaceDefinitionStateManager.INTERFACE_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class JavaIntefaceMarker extends StateDrivenDataBuilder<Object> {

    public JavaIntefaceMarker(JavaBuildManager javaBuildManager) {
        if (null == javaBuildManager) {
            throw new NullPointerException("javaBuildManager is null");
        }

        this.javaBuildManager = javaBuildManager;
        addStateManager(new JavaInterfaceDefinitionStateManager());

    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        if (type
                .equals(INTERFACE_STATE_CHANGE.ENTER_INTERFACE_BLOCK)) {
            javaBuildManager.toInterface();
        }
    }

    private final JavaBuildManager javaBuildManager;

}
