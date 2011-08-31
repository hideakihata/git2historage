package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement.block;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ElseBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedIfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;

public class ElseBlockBuilder extends InnerBlockBuilder<ElseBlockInfo, UnresolvedElseBlockInfo> {

    public ElseBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new ElseBlockStateManager());
    }

    @Override
    protected UnresolvedElseBlockInfo createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        if(outerSpace instanceof UnresolvedIfBlockInfo) {
            final UnresolvedIfBlockInfo ownerIf = (UnresolvedIfBlockInfo) outerSpace;
            final UnresolvedElseBlockInfo elseBlock = new UnresolvedElseBlockInfo(ownerIf, ownerIf.getOuterSpace());
            
            ownerIf.setSequentElseBlock(elseBlock);
            
            return elseBlock;
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

}
