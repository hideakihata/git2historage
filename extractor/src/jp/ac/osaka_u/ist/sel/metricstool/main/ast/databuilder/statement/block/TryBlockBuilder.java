package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.TryBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;


public class TryBlockBuilder extends InnerBlockBuilder<TryBlockInfo, UnresolvedTryBlockInfo> {

    public TryBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new TryBlockStateManager());
    }

    @Override
    protected UnresolvedTryBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedTryBlockInfo(outerSpace);
    }

}
