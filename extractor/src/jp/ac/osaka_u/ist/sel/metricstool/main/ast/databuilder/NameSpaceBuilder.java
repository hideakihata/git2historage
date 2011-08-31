package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameSpaceStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameSpaceStateManager.NAMESPACE_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class NameSpaceBuilder extends CompoundDataBuilder<Object> {
    
    public NameSpaceBuilder(BuildDataManager buildDataManager){
        this(buildDataManager,new IdentifierBuilder());
    }
    
    public NameSpaceBuilder(BuildDataManager buildDataManager,IdentifierBuilder identifierBuilder){
        
        if (null == buildDataManager){
            throw new NullPointerException("nameSpaceManager is null.");
        }
        
        if (null == identifierBuilder){
            throw new NullPointerException("identifierBuilder is null.");
        }
        

        this.identifierBuilder = identifierBuilder;
        addInnerBuilder(identifierBuilder);
        identifierBuilder.deactivate();
        
        this.buildDataManager = buildDataManager;
        addStateManager(stateManager);
    }
    
    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        
        if (type.equals(NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_DEF)){
            identifierBuilder.activate();
        } else if (type.equals(NAMESPACE_STATE_CHANGE.EXIT_NAMESPACE_DEF)||
                type.equals(NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_BLOCK)){
            
            identifierBuilder.deactivate();
            buildNameSpace();
            
        }
    }
    
    private void buildNameSpace(){
        if (identifierBuilder.hasBuiltData()){
            buildDataManager.pushNewNameSpace(identifierBuilder.popLastBuiltData());
        }
    }
    
    private final BuildDataManager buildDataManager;
    
    private final IdentifierBuilder identifierBuilder;
    
    private final NameSpaceStateManager stateManager = new NameSpaceStateManager();
    
}
