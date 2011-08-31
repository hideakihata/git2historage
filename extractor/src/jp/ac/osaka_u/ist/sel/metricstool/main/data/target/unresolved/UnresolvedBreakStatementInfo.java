package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


public class UnresolvedBreakStatementInfo extends UnresolvedJumpStatementInfo<BreakStatementInfo> {

    public UnresolvedBreakStatementInfo(
            UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace) {
        this(ownerSpace, 0, 0, 0, 0);
    }

    public UnresolvedBreakStatementInfo(
            UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    protected BreakStatementInfo createResolvedInfo(LocalSpaceInfo outerLocalSpace,
            LabelInfo destinationLabel, int fromLine, int fromColumn, int toLine, int toColumn) {
        return new BreakStatementInfo(outerLocalSpace, destinationLabel, fromLine, fromColumn,
                toLine, toColumn);
    }

}
