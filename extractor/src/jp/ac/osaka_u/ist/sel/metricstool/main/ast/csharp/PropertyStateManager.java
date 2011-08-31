package jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class PropertyStateManager extends CallableUnitStateManager {

    @Override
    protected boolean isDefinitionEvent(AstVisitEvent event) {
        return event.getToken().isPropertyDefinition();
    }

}
