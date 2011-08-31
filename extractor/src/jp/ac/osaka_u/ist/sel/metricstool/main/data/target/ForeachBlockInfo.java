package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;


/**
 * Foreachブロックを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ForeachBlockInfo extends ConditionalBlockInfo {

    /**
     * 位置情報を与えてForeachブロックを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ForeachBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * このForeachブロックのテキスト表現を返す
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();

        text.append("for (");

        text.append(this.getConditionalClause().getText());

        text.append(") {");
        text.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            text.append(statement.getText());
            text.append(System.getProperty("line.separator"));
        }

        text.append("}");

        return text.toString();
    }

    @Override
    public boolean isLoopStatement() {
        return true;
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        thrownExpressions.addAll(super.getThrownExceptions());
        thrownExpressions.addAll(this.getConditionalClause().getCondition().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExpressions);
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ForeachBlockInfo newForeachBlock = new ForeachBlockInfo(fromLine, fromColumn, toLine,
                toColumn);

        final ConditionalClauseInfo newConditionalClause = this.getConditionalClause().copy();
        newForeachBlock.setConditionalClause(newConditionalClause);

        final UnitInfo outerUnit = this.getOuterUnit();
        newForeachBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newForeachBlock.addStatement((StatementInfo) statement.copy());
        }

        return newForeachBlock;
    }
}
