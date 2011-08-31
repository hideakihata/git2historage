package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * synchronized ブロックを表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class SynchronizedBlockInfo extends BlockInfo {

    /**
     * 位置情報を与えて synchronized ブロックを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public SynchronizedBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.synchronizedExpression = null;
    }

    /**
     * このsynchronized文のテキスト表現（String型）を返す
     * 
     * @return このsynchronized文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("synchronized (");

        final ExpressionInfo expression = this.getSynchronizedExpression();
        sb.append(expression.getText());

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

    /**
     * 同期式を設定する
     * 
     * @param synchronizedExpression 同期式
     */
    public final void setSynchronizedExpression(final ExpressionInfo synchronizedExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == synchronizedExpression) {
            throw new IllegalArgumentException();
        }

        this.synchronizedExpression = synchronizedExpression;
        this.synchronizedExpression.setOwnerExecutableElement(this);
    }

    /**
     * 同期式を返す
     * 
     * @return　同期式
     */
    public final ExpressionInfo getSynchronizedExpression() {
        assert null != this.synchronizedExpression : "synchronizedExpression is null!";
        return this.synchronizedExpression;
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final SynchronizedBlockInfo newSynchronizedBlock = new SynchronizedBlockInfo(fromLine,
                fromColumn, toLine, toColumn);

        final ExpressionInfo synchronizedExpression = (ExpressionInfo) this
                .getSynchronizedExpression().copy();
        newSynchronizedBlock.setSynchronizedExpression(synchronizedExpression);

        final UnitInfo outerUnit = this.getOuterUnit();
        newSynchronizedBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newSynchronizedBlock.addStatement((StatementInfo) statement.copy());
        }

        return newSynchronizedBlock;
    }

    private ExpressionInfo synchronizedExpression;
}
