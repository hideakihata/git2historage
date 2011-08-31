package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * try ブロックを表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class TryBlockInfo extends BlockInfo {

    /**
     * 位置情報を与えて try ブロックを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TryBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.sequentFinallyBlock = null;
        this.sequentCatchBlocks = new TreeSet<CatchBlockInfo>();
    }

    /**
     * 対応する finally 文をセットする
     * 
     * @param sequentFinallyBlock 対応する finally 文
     */
    public void setSequentFinallyBlock(final FinallyBlockInfo sequentFinallyBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentFinallyBlock) {
            throw new NullPointerException();
        }

        this.sequentFinallyBlock = sequentFinallyBlock;
    }

    /**
     * 対応する finally 文を返す
     * 
     * @return 対応する finally 文
     */
    public FinallyBlockInfo getSequentFinallyBlock() {
        return this.sequentFinallyBlock;
    }

    /**
     * 対応するcatchブロックを追加する
     * @param catchBlock 対応するcatchブロック
     */
    public void addSequentCatchBlock(final CatchBlockInfo catchBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == catchBlock) {
            throw new IllegalArgumentException("catchBlock is null");
        }

        this.sequentCatchBlocks.add(catchBlock);
    }

    /**
     * 対応するcatchブロックのSetを返す
     * @return 対応するcatchブロックのSet
     */
    public SortedSet<CatchBlockInfo> getSequentCatchBlocks() {
        return this.sequentCatchBlocks;
    }

    /**
     * 対応するfinallyブロックが存在するかどうか返す
     * @return 対応するfinallyブロックが存在するならtrue
     */
    public boolean hasFinallyBlock() {
        return null != this.sequentFinallyBlock;
    }

    /**
     * このtry文のテキスト表現（型）を返す
     * 
     * @return このtry文のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("try {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final TryBlockInfo newTryBlock = new TryBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final FinallyBlockInfo sequentFinallyBlock = (FinallyBlockInfo) this
                .getSequentFinallyBlock().copy();
        newTryBlock.setSequentFinallyBlock(sequentFinallyBlock);

        for (final CatchBlockInfo catchBlockInfo : this.getSequentCatchBlocks()) {
            newTryBlock.addSequentCatchBlock((CatchBlockInfo) catchBlockInfo.copy());
        }

        final UnitInfo outerUnit = this.getOuterUnit();
        newTryBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newTryBlock.addStatement((StatementInfo) statement.copy());
        }

        return newTryBlock;
    }

    /**
     * 対応するcatchブロックを保存する変数
     */
    private final SortedSet<CatchBlockInfo> sequentCatchBlocks;

    private FinallyBlockInfo sequentFinallyBlock;
}
