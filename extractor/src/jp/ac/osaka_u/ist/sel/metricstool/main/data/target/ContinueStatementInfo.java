package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


@SuppressWarnings("serial")
public class ContinueStatementInfo extends JumpStatementInfo {

    public ContinueStatementInfo(LocalSpaceInfo ownerSpace, LabelInfo destinationLabel,
            int fromLine, int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, destinationLabel, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    protected String getReservedKeyword() {
        return "continue";
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final LabelInfo label = this.getDestinationLabel();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ContinueStatementInfo newStatement = new ContinueStatementInfo(outerUnit, label,
                fromLine, fromColumn, toLine, toColumn);

        return newStatement;
    }
}
