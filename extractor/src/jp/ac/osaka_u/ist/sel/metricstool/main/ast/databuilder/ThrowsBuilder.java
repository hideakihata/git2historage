package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ThrownExceptionsDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

/**
 * メソッドやコンストラクタが投げる例外を登録するクラス
 * @author g-yamada
  */
public class ThrowsBuilder extends CompoundDataBuilder<UnresolvedClassTypeInfo>{

    public ThrowsBuilder(final BuildDataManager buildManager){
        this(buildManager, new TypeBuilder(buildManager));
    }
    
    public ThrowsBuilder(final BuildDataManager buildManager, final TypeBuilder typeBuilder){
        this.buildDataManager = buildManager;
        
        this.typeBuilder = typeBuilder;
        
        addInnerBuilder(typeBuilder);
        typeBuilder.deactivate();
        
        this.addStateManager(throwsStateManager);
        this.addStateManager(typeStateManager);
    }

    @Override
    public void stateChanged(StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        if (isActive() && throwsStateManager.isEntered()){
            if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)){
                typeBuilder.activate();
            } else if ( type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)) { 
                if (!typeStateManager.isEntered()){
                    typeBuilder.deactivate();
                    buildThrows();
                }
            }
        }
    }
    
    private void buildThrows() {
        UnresolvedTypeInfo<? extends TypeInfo> type = typeBuilder.popLastBuiltData();
        
        if (type instanceof UnresolvedClassTypeInfo){
            UnresolvedClassTypeInfo classType = (UnresolvedClassTypeInfo) type;
            registBuiltData(classType);
            
            if (null != buildDataManager) {
                UnresolvedCallableUnitInfo callableInfo = buildDataManager.getCurrentCallableUnit();
                if (null != callableInfo){
                    callableInfo.addTypeParameter(classType);
                }
            }
        }
    }
    
    private final TypeBuilder typeBuilder;

    private final ThrownExceptionsDefinitionStateManager throwsStateManager = new ThrownExceptionsDefinitionStateManager();

    private final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();

    private final BuildDataManager buildDataManager;
}
