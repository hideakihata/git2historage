package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class NameBuilder extends CompoundDataBuilder<String[]>{

    public NameBuilder(){
        this(new IdentifierBuilder());
    }
    
    public NameBuilder(IdentifierBuilder builder){
        this.builder = builder;
        this.addStateManager(stateMachine);
        
        addInnerBuilder(builder);
    }
    
    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        if (isActive()){
            StateChangeEventType type = event.getType();
            if (type.equals(NameStateManager.NAME_STATE.ENTER_NAME)){
                builder.activate();
            } else if (type.equals(NameStateManager.NAME_STATE.EXIT_NAME)){
                builder.deactivate();
                registBuiltData(buildName());
            }
        }
    }
    
    private String[] buildName(){
        return builder.popLastBuiltData();
    }
    
    private final IdentifierBuilder builder;
    
    private final NameStateManager stateMachine = new NameStateManager();
}
