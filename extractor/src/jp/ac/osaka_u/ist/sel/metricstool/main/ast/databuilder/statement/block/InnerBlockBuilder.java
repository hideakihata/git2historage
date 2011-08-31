package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager.INNER_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


public abstract class InnerBlockBuilder<TResolved extends BlockInfo, T extends UnresolvedBlockInfo<TResolved>>
        extends CompoundDataBuilder<T> {

    protected InnerBlockBuilder(final BuildDataManager targetDataManager,
            final InnerBlockStateManager innerBlockStateManager) {

        if (null == targetDataManager) {
            throw new IllegalArgumentException("targetDataManager is null.");
        }

        this.buildManager = targetDataManager;
        this.blockStateManager = innerBlockStateManager;
        addStateManager(blockStateManager);
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();

        if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_DEF)) {
            buildBlockDefinition(event.getTrigger());
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_DEF)) {
            endBlockDefinition();
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_CLAUSE)) {

        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_CLAUSE)) {

        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_SCOPE)) {
            final T buildingBlock = this.getBuildingBlock();
            if(null != buildingBlock) {
                // èåèêﬂì‡ÇÃïœêîêÈåæï∂ìôÇ™èåèêﬂì‡ÇÃï∂Ç∆ÇµÇƒï€ë∂Ç≥ÇÍÇƒÇ¢ÇÈâ¬î\ê´Ç™Ç†ÇÈÇΩÇﬂèâä˙âªèàóùÇçsÇ§
                //buildingBlock.initBody();
            }
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_SCOPE)) {

        }
    }

    protected void endBlockDefinition() {
        T buildBlock = buildingBlockStack.pop();

        if (null != buildManager && null != buildBlock) {
            registBuiltData(buildBlock);
            buildManager.endInnerBlockDefinition();
        }
    }

    protected void buildBlockDefinition(AstVisitEvent triggerEvent) {
        final UnresolvedLocalSpaceInfo<?> currentSpace = this.getCurrentSpace();

        assert currentSpace != null : "IllegalState: owner of inner block was not local space";

        final T newBlock = createUnresolvedBlockInfo(currentSpace);

        newBlock.setFromLine(triggerEvent.getStartLine());
        newBlock.setFromColumn(triggerEvent.getStartColumn());
        newBlock.setToLine(triggerEvent.getEndLine());
        newBlock.setToColumn(triggerEvent.getEndColumn());

        newBlock.getOuterSpace().addStatement(newBlock);

        startBlockDefinition(newBlock);
    }

    protected void startBlockDefinition(T newBlock) {
        this.buildingBlockStack.push(newBlock);
        buildManager.startInnerBlockDefinition(newBlock);
    }

    protected UnresolvedLocalSpaceInfo<?> getCurrentSpace() {
        UnresolvedUnitInfo<?> currentUnit = this.buildManager.getCurrentUnit();
        if (currentUnit instanceof UnresolvedLocalSpaceInfo) {
            return (UnresolvedLocalSpaceInfo<?>) currentUnit;
        } else {
            return null;
        }

    }
    
    protected T getBuildingBlock() {
        return this.buildingBlockStack.peek();
    }

    protected abstract T createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace);

    protected Stack<T> buildingBlockStack = new Stack<T>();

    protected final BuildDataManager buildManager;

    protected final InnerBlockStateManager blockStateManager;
}
