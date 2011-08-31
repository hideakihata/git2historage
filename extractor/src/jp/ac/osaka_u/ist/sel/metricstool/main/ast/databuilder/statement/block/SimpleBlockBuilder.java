package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SimpleBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSimpleBlockInfo;


public class SimpleBlockBuilder extends InnerBlockBuilder<SimpleBlockInfo, UnresolvedSimpleBlockInfo> {

    public SimpleBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new SimpleBlockStateManager());
    }

    @Override
    protected UnresolvedSimpleBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedSimpleBlockInfo(outerSpace);
    }

}
