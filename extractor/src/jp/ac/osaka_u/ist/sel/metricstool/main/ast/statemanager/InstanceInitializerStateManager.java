package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class InstanceInitializerStateManager extends CallableUnitStateManager{
    /**
     * 引数のイベントがスタティック・イニシャライザ定義部を表すかどうかかを返す
     */
    @Override
    protected boolean isDefinitionEvent(AstVisitEvent event) {
        return event.getToken().isInstanceInitializerDefinition();
    }
}
