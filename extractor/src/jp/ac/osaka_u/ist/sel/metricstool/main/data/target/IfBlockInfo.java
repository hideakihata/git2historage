package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if ブロックを表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class IfBlockInfo extends ConditionalBlockInfo {

    /**
     * 位置情報を与えて if ブロックを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public IfBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * else 文を追加する
     * 
     * @param sequentElseBlock 追加する else 文
     */
    public void setSequentElseBlock(final ElseBlockInfo sequentElseBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentElseBlock) {
            throw new NullPointerException();
        }

        this.sequentElseBlock = sequentElseBlock;
    }

    /**
     * このIf文に対応するElse文を返す
     * 
     * @return このIf文に対応するElse文
     */
    public ElseBlockInfo getSequentElseBlock() {
        return this.sequentElseBlock;
    }

    /**
     * 対応するelseブロックが存在するかどうか表す
     * @return 対応するelseブロックが存在するならtrue
     */
    public boolean hasElseBlock() {
        return null != this.sequentElseBlock;
    }

    /**
     * このif文のテキスト表現（String型）を返す
     * 
     * @return このif文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("if (");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

        sb.append(") {");
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

        final IfBlockInfo newIfBlock = new IfBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final ConditionalClauseInfo newConditionalClause = this.getConditionalClause().copy();
        newIfBlock.setConditionalClause(newConditionalClause);

        final UnitInfo outerUnit = this.getOuterUnit();
        newIfBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newIfBlock.addStatement((StatementInfo) statement.copy());
        }

        final ElseBlockInfo sequentElseBlock = (ElseBlockInfo) this.getSequentElseBlock().copy();
        newIfBlock.setSequentElseBlock(sequentElseBlock);

        return newIfBlock;
    }

    private ElseBlockInfo sequentElseBlock;
}
