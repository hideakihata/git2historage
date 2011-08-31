package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.FinallyBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;


public class FinallyBlockBuilder extends InnerBlockBuilder<FinallyBlockInfo, UnresolvedFinallyBlockInfo> {

    public FinallyBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new FinallyBlockStateManager());
    }

    @Override
    protected UnresolvedFinallyBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        if (outerSpace instanceof UnresolvedTryBlockInfo) {
            final UnresolvedTryBlockInfo ownerTry = (UnresolvedTryBlockInfo) outerSpace;
            final UnresolvedFinallyBlockInfo finallyBlock = new UnresolvedFinallyBlockInfo(
                    ownerTry, ownerTry.getOuterSpace());

            ownerTry.setSequentFinallyBlock(finallyBlock);

            return finallyBlock;
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

}
