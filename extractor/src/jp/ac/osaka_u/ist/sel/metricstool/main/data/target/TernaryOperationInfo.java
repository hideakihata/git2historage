package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 三項演算使用を表すクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class TernaryOperationInfo extends ExpressionInfo {

    /**
     * 三項演算の条件式(第一項)，条件式がtrueの時に返される式，条件式がfalseの時に返される式(第三項)，開始位置，終了位置を与えて初期化
     * 
     * @param condtion 条件式(第一項)
     * @param trueExpression 条件式がtrueのときに返される式(第二項)
     * @param falseExpression 条件式がfalseのといに返される式(第三項)
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TernaryOperationInfo(final ConditionInfo condtion, ExpressionInfo trueExpression,
            ExpressionInfo falseExpression, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == condtion || null == trueExpression || null == falseExpression) {
            throw new IllegalArgumentException();
        }

        this.condition = condtion;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;

        if (this.condition instanceof ExpressionInfo) {
            ((ExpressionInfo) this.condition).setOwnerExecutableElement(this);
        }
        this.trueExpression.setOwnerExecutableElement(this);
        this.falseExpression.setOwnerExecutableElement(this);
    }

    @Override
    public TypeInfo getType() {
        return this.trueExpression.getType();
    }

    /**
     * 三項演算の条件式(第一項)を返す
     * @return 三項演算の条件式(第一項)
     */
    public ConditionInfo getCondition() {
        return this.condition;
    }

    /**
     * 三項演算の条件式がtrueのときに返される式(第二項)を返す
     * @return 三項演算の条件式がtrueのときに返される式(第二項)
     */
    public ExpressionInfo getTrueExpression() {
        return this.trueExpression;
    }

    /**
     * 三項演算の条件式がfalseときに返される式(第三項)を返す
     * @return 三項演算の条件式がfalseのときに返される式(第三項)
     */
    public ExpressionInfo getFalseExpression() {
        return this.falseExpression;
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<?>> variableUsages = new HashSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getCondition().getVariableUsages());
        variableUsages.addAll(this.getTrueExpression().getVariableUsages());
        variableUsages.addAll(this.getFalseExpression().getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        calls.addAll(this.getCondition().getCalls());
        calls.addAll(this.getTrueExpression().getCalls());
        calls.addAll(this.getFalseExpression().getCalls());
        return Collections.unmodifiableSet(calls);
    }

    /**
     * この三項演算のテキスト表現（型）を返す
     * 
     * @return この三項演算のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ConditionInfo condition = this.getCondition();
        sb.append(condition.getText());

        sb.append(" ? ");

        final ExpressionInfo trueExpression = this.getTrueExpression();
        sb.append(trueExpression.getText());

        sb.append(" : ");

        final ExpressionInfo falseExpression = this.getFalseExpression();
        sb.append(falseExpression.getText());

        return sb.toString();

    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        thrownExceptions.addAll(this.getCondition().getThrownExceptions());
        thrownExceptions.addAll(this.getTrueExpression().getThrownExceptions());
        thrownExceptions.addAll(this.getFalseExpression().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExceptions);
    }

    @Override
    public ExecutableElementInfo copy() {
        final ConditionInfo condition = (ConditionInfo) this.getCondition().copy();
        final ExpressionInfo trueExpression = (ExpressionInfo) this.getTrueExpression().copy();
        final ExpressionInfo falseExpression = (ExpressionInfo) this.getFalseExpression().copy();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final TernaryOperationInfo newTernaryOperation = new TernaryOperationInfo(condition,
                trueExpression, falseExpression, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newTernaryOperation.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newTernaryOperation.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newTernaryOperation;
    }

    /**
     * 三項演算の条件式(第一項)を保存する変数
     */
    private final ConditionInfo condition;

    /**
     * 三項演算の条件式がtrueのときに返される式(第二項)を保存する変数
     */
    private final ExpressionInfo trueExpression;

    /**
     * 三項演算の条件式がfalseのときに返される式(第三項)を保存する変数
     */
    private final ExpressionInfo falseExpression;
}
