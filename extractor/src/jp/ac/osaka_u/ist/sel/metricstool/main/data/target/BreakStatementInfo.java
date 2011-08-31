package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 
 * @author t-miyake
 *
 * break文を表すクラス
 */
@SuppressWarnings("serial")
public class BreakStatementInfo extends JumpStatementInfo {

    /**
     * オブジェクトを初期化
     * 
     * @param ownerSpace オーナーブロック
     * @param destinationLabel ラベル
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public BreakStatementInfo(LocalSpaceInfo ownerSpace, LabelInfo destinationLabel, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, destinationLabel, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    protected String getReservedKeyword() {
        return "break";
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final LabelInfo label = this.getDestinationLabel();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final BreakStatementInfo newStatement = new BreakStatementInfo(outerUnit, label, fromLine,
                fromColumn, toLine, toColumn);

        return newStatement;
    }
}
