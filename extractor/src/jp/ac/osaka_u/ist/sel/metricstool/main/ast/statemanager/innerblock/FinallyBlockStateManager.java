package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class FinallyBlockStateManager extends InnerBlockStateManager {

    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isFinally();
    }

}
