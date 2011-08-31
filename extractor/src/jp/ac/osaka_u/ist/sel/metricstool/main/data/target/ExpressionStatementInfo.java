package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * 式文の情報を表すクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class ExpressionStatementInfo extends SingleStatementInfo {

    /**
     * 式と位置情報を与えて初期化
     * 
     * @param ownerSpace 外側のスコープ
     * @param expression 式文を構成する式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ExpressionStatementInfo(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo expression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == expression) {
            throw new IllegalArgumentException("expression is null");
        }

        this.expression = expression;

        this.expression.setOwnerExecutableElement(this);
    }

    /**
     * 式文を構成する式を返す
     * 
     * @return 式文を構成する式
     */
    public final ExpressionInfo getExpression() {
        return this.expression;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo#getVariableUsages()
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getExpression().getVariableUsages();
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getExpression().getCalls();
    }

    /**
     * この式文のテキスト表現（型）を返す
     * 
     * @return この式文のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo expression = this.getExpression();
        sb.append(expression.getText());

        sb.append(";");

        return sb.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getExpression().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final ExpressionInfo expression = (ExpressionInfo) this.getExpression().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ExpressionStatementInfo newStatement = new ExpressionStatementInfo(outerUnit,
                expression, fromLine, fromColumn, toLine, toColumn);

        return newStatement;
    }

    /**
     * 式文を構成する式を保存するための変数
     */
    private final ExpressionInfo expression;

}
