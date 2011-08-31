package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * 一項演算を保存するためのクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public final class MonominalOperationInfo extends ExpressionInfo {

    /**
     * 一項演算のオペランド、位置情報を与えて初期化
     * 
     * @param operand オペランド
     * @param operator オペレーター
     * @param isPreposed 演算子の位置
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public MonominalOperationInfo(final ExpressionInfo operand, final OPERATOR operator,
            final boolean isPreposed, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == operand || null == operator) {
            throw new IllegalArgumentException();
        }

        this.operand = operand;
        this.operator = operator;
        this.isPreposed = isPreposed;
        this.type = null != this.operator.getSpecifiedResultType() ? this.operator
                .getSpecifiedResultType() : this.operand.getType();

        this.operand.setOwnerExecutableElement(this);
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    /**
     * オペランドを返す
     * @return オペランド
     */
    public final ExpressionInfo getOperand() {
        return this.operand;
    }

    /**
     * 一項演算の演算子を返す．
     * @return 演算子
     */
    public final OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * 演算子が前置されているかどうか返す
     * @return 演算子が前置されているならtrue
     */
    public final boolean isPreposed() {
        return this.isPreposed;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getOperand().getVariableUsages();
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getOperand().getCalls();
    }

    /**
     * この単項演算のテキスト表現（String型）を返す
     * 
     * @return この単項演算のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo operand = this.getOperand();
        final OPERATOR operator = this.getOperator();

        if (this.isPreposed()) {

            sb.append(operator.getToken());
            sb.append(operand.getText());

        } else {

            sb.append(operand.getText());
            sb.append(operator.getToken());
        }

        return sb.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getOperand().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo operand = (ExpressionInfo) this.getOperand().copy();
        final OPERATOR operator = this.getOperator();
        final boolean isPreposed = this.isPreposed();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final MonominalOperationInfo newMonominalOperation = new MonominalOperationInfo(operand,
                operator, isPreposed, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newMonominalOperation.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newMonominalOperation.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newMonominalOperation;
    }

    /**
     * オペランドを保存するための変数
     */
    private final ExpressionInfo operand;

    /**
     * 一項演算の演算子を保存するための変数
     */
    private final OPERATOR operator;

    /**
     * 一項演算の結果の型を保存するための変数
     */
    private final TypeInfo type;

    /**
     * 演算子が前置しているかどうかを示す変数
     */
    private final boolean isPreposed;
}
