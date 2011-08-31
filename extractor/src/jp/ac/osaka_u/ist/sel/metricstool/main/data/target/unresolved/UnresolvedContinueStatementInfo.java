package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


public class UnresolvedContinueStatementInfo extends
        UnresolvedJumpStatementInfo<ContinueStatementInfo> {

    public UnresolvedContinueStatementInfo(
            UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerLocalSpace) {
        this(outerLocalSpace, 0, 0, 0, 0);
    }

    public UnresolvedContinueStatementInfo(
            UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    protected ContinueStatementInfo createResolvedInfo(LocalSpaceInfo ownerSpace,
            LabelInfo destinationLabel, int fromLine, int fromColumn, int toLine, int toColumn) {
        return new ContinueStatementInfo(ownerSpace, destinationLabel, fromLine, fromColumn,
                toLine, toColumn);
    }

}
