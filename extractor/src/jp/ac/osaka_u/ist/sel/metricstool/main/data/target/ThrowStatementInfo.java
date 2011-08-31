package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * throw文の情報を保有するクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class ThrowStatementInfo extends SingleStatementInfo {

    /**
     * throw文によって投げられる例外を表す式と位置情報を与えて初期化
     * 
     * @param ownerSpace 文を直接所有する空間
     * @param thrownEpression throw文によって投げられる例外を表す式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ThrowStatementInfo(final LocalSpaceInfo ownerSpace, ExpressionInfo thrownEpression,
            int fromLine, int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == thrownEpression) {
            throw new IllegalArgumentException("thrownExpression is null");
        }
        this.thrownEpression = thrownEpression;

        this.thrownEpression.setOwnerExecutableElement(this);
    }

    /**
     * throw文によって投げられる例外を表す式を返す
     * 
     * @return throw文によって投げられる例外を表す式
     */
    public final ExpressionInfo getThrownExpression() {
        return this.thrownEpression;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getThrownExpression().getVariableUsages();
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
        return this.getThrownExpression().getCalls();
    }

    /**
     * このthrow文のテキスト表現（型）を返す
     * 
     * @return このthrow文のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("throw ");

        final ExpressionInfo expression = this.getThrownExpression();
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
        final Set<ReferenceTypeInfo> thrownExpressions = new HashSet<ReferenceTypeInfo>();
        if (this.getThrownExpression().getType() instanceof ClassTypeInfo) {
            thrownExpressions.add((ClassTypeInfo) this.getThrownExpression().getType());
        }
        return Collections.unmodifiableSet(thrownExpressions);
    }

    @Override
    public ExecutableElementInfo copy() {

        final LocalSpaceInfo outerUnit = this.getOwnerSpace();
        final ExpressionInfo thrownExpression = (ExpressionInfo) this.getThrownExpression().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ThrowStatementInfo newStatement = new ThrowStatementInfo(outerUnit, thrownExpression,
                fromLine, fromColumn, toLine, toColumn);

        return newStatement;
    }

    /**
     * throw文によって投げられる例外を表す式
     */
    private final ExpressionInfo thrownEpression;

}
