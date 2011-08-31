package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.BlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.BlockStateManager.BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;

public class BlockScopeBuilder extends StateDrivenDataBuilder{

    public BlockScopeBuilder(BuildDataManager buildManager) {
        if (null == buildManager){
            throw new NullPointerException("buildManager is null.");
        }
        
        this.buildManager = buildManager;
        addStateManager(new BlockStateManager());
    }

    @Override
    public void stateChanged(StateChangeEvent event) {
        StateChangeEventType type = event.getType();
        if (type.equals(BLOCK_STATE_CHANGE.ENTER)){
            buildManager.startScopedBlock();
        } else if (type.equals(BLOCK_STATE_CHANGE.EXIT)){
            buildManager.endScopedBlock();
        }
    }
    
    private final BuildDataManager buildManager;
}
