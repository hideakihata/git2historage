package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * else ブロックを表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ElseBlockInfo extends BlockInfo implements SubsequentialBlockInfo<IfBlockInfo> {

    /**
     * 対応する if ブロックを与えて，else ブロック情報を初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ElseBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * このelse文のテキスト表現（String型）を返す
     * 
     * @return このelse文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("else {");
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
     * この else ブロックと対応する if ブロックを返す
     * このメソッドは将来廃止予定であり，使用は推奨されない
     * {@link ElseBlockInfo#getOwnerBlock()} を使用すべきである．
     * 
     * @return この else ブロックと対応する if ブロック
     * @deprecated
     */
    public IfBlockInfo getOwnerIfBlock() {
        return this.ownerIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを返す
     * 
     * @return この else ブロックと対応する if ブロック
     */
    @Override
    public IfBlockInfo getOwnerBlock() {
        assert null != this.ownerIfBlock : "this.ownerIfBlock must not be null!";
        return this.ownerIfBlock;
    }

    @Override
    public void setOwnerBlock(final IfBlockInfo ownerIfBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerIfBlock) {
            throw new NullPointerException();
        }

        if (null != this.ownerIfBlock) {
            throw new IllegalStateException();
        }

        this.ownerIfBlock = ownerIfBlock;
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ElseBlockInfo newElseBlock = new ElseBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final IfBlockInfo ownerIfBlock = this.getOwnerBlock();
        newElseBlock.setOwnerBlock(ownerIfBlock);

        final UnitInfo outerUnit = this.getOuterUnit();
        newElseBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newElseBlock.addStatement((StatementInfo) statement.copy());
        }

        return newElseBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを保存するための変数
     */
    private IfBlockInfo ownerIfBlock;

}
