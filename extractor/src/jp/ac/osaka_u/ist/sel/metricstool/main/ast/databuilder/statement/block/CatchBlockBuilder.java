package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.LocalVariableBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CatchBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CatchBlockStateManager.CATCH_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;


public class CatchBlockBuilder extends InnerBlockBuilder<CatchBlockInfo, UnresolvedCatchBlockInfo> {

    public CatchBlockBuilder(final BuildDataManager targetDataManager,
            final LocalVariableBuilder exceptionParameterBuilder) {
        super(targetDataManager, new CatchBlockStateManager());

        if (null == exceptionParameterBuilder) {
            throw new IllegalArgumentException("exceptionParameterBuilder is null.");
        }

        this.exceptionParameterBuilder = exceptionParameterBuilder;
        this.addInnerBuilder(this.exceptionParameterBuilder);
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChanged(event);

        final StateChangeEventType type = event.getType();
        if (type.equals(CATCH_BLOCK_STATE_CHANGE.ENTER_LOCAL_PARAMETER)) {
            this.exceptionParameterBuilder.activate();
        } else if (type.equals(CATCH_BLOCK_STATE_CHANGE.EXIT_LOCAL_PARAMETER)) {
            this.getBuildingBlock().setCaughtException(
                    this.exceptionParameterBuilder.getLastBuildData());
            this.exceptionParameterBuilder.deactivate();
            this.getBuildingBlock().initBody();
        }
    }

    @Override
    protected UnresolvedCatchBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        if (outerSpace instanceof UnresolvedTryBlockInfo) {
            final UnresolvedTryBlockInfo ownerTry = (UnresolvedTryBlockInfo) outerSpace;

            final UnresolvedCatchBlockInfo catchBlock = new UnresolvedCatchBlockInfo(ownerTry,
                    ownerTry.getOuterSpace());

            ownerTry.addSequentCatchBlock(catchBlock);

            return catchBlock;
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

    private final LocalVariableBuilder exceptionParameterBuilder;

}
