package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * finally ブロック情報を表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class FinallyBlockInfo extends BlockInfo implements
        SubsequentialBlockInfo<TryBlockInfo> {

    /**
     * 対応する try ブロック情報を与えて finally ブロックを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public FinallyBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * このfinally節のテキスト表現（String型）を返す
     * 
     * @return このfinally節のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("finally {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * 対応する try ブロックを返す
     * このメソッドは将来廃止されるため，使用は推奨されない
     * {@link FinallyBlockInfo#getOwnerBlock()} を使用すべきである．
     * 
     * @return 対応する try ブロック
     * @deprecated
     */
    public TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    @Override
    public TryBlockInfo getOwnerBlock() {
        assert null != this.ownerTryBlock : "this.ownerTryBlock must not be null!";
        return this.ownerTryBlock;
    }

    @Override
    public void setOwnerBlock(final TryBlockInfo ownerTryBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        if (null != this.ownerTryBlock) {
            throw new IllegalStateException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final FinallyBlockInfo newFinallyBlock = new FinallyBlockInfo(fromLine, fromColumn, toLine,
                toColumn);

        final TryBlockInfo ownerTryBlock = this.getOwnerBlock();
        newFinallyBlock.setOwnerBlock(ownerTryBlock);

        final UnitInfo outerUnit = this.getOuterUnit();
        newFinallyBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newFinallyBlock.addStatement((StatementInfo) statement.copy());
        }

        return newFinallyBlock;
    }

    private TryBlockInfo ownerTryBlock;

}
