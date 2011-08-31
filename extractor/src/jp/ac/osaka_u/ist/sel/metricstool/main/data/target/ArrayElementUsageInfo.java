package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 配列要素の使用を表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ArrayElementUsageInfo extends ExpressionInfo {

    /**
     * 要素の親，つまり配列型の式とインデックスを与えて，オブジェクトを初期化
     * 
     * @param indexExpression インデックス
     * @param qualifierExpression 配列型の式
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayElementUsageInfo(final ExpressionInfo indexExpression,
            final ExpressionInfo qualifierExpression, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == qualifierExpression) {
            throw new NullPointerException();
        }

        this.qualifierExpression = qualifierExpression;
        this.indexExpression = indexExpression;

        this.indexExpression.setOwnerExecutableElement(this);
    }

    /**
     * この配列要素の使用の型を返す
     * 
     * @return この配列要素の使用の型
     */
    @Override
    public TypeInfo getType() {

        final TypeInfo ownerType = this.getQualifierExpression().getType();

        // 親が配列型である，と解決できている場合
        if (ownerType instanceof ArrayTypeInfo) {
            // 配列の次元に応じて型を生成
            final int ownerArrayDimension = ((ArrayTypeInfo) ownerType).getDimension();
            final TypeInfo ownerArrayElement = ((ArrayTypeInfo) ownerType).getElementType();

            // 配列が二次元以上の場合は，次元を一つ落とした配列を返し，一次元の場合は，要素の型を返す．
            return 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                    ownerArrayDimension - 1) : ownerArrayElement;
        }

        // 配列型でない，かつ不明型でない場合はおかしい
        assert ownerType instanceof UnknownTypeInfo : "ArrayElementUsage attaches unappropriate type!";

        return ownerType;
    }

    /**
     * この要素の親，つまり配列型の式を返す
     * 
     * @return この要素の親を返す
     */
    public ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    /**
     * この要素のインデックスを返す
     * 
     * @return　この要素のインデックス
     */
    public ExpressionInfo getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * この式（配列要素の使用）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final Set<VariableUsageInfo<?>> variableUsages = new HashSet<VariableUsageInfo<?>>(
                this.indexExpression.getVariableUsages());
        variableUsages.addAll(this.getQualifierExpression().getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
        //return this.getOwnerEntityUsage().getVariableUsages();
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        final Set<CallInfo<?>> calls = new HashSet<CallInfo<?>>();
        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        calls.addAll(quantifierExpression.getCalls());
        final ExpressionInfo indexExpression = this.getIndexExpression();
        calls.addAll(indexExpression.getCalls());
        return Collections.unmodifiableSet(calls);
    }

    @Override
    public void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement) {
        super.setOwnerExecutableElement(ownerExecutableElement);

        this.qualifierExpression.setOwnerExecutableElement(ownerExecutableElement);
    }

    /**
     * この配列要素使用のテキスト表現（String型）を返す
     * 
     * @return この配列要素使用のテキスト表現
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo expression = this.getQualifierExpression();
        sb.append(expression.getText());

        sb.append("[");

        final ExpressionInfo indexExpression = this.getIndexExpression();
        sb.append(indexExpression.getText());

        sb.append("]");

        return sb.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getIndexExpression().getThrownExceptions());
    }

    @Override
    public ExecutableElementInfo copy() {
        final ExpressionInfo indexExpression = (ExpressionInfo) this.getIndexExpression().copy();
        final ExpressionInfo qualifiedExpression = this.getQualifierExpression();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                indexExpression, qualifiedExpression, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newArrayElementUsage.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newArrayElementUsage.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newArrayElementUsage;
    }

    private final ExpressionInfo qualifierExpression;

    private final ExpressionInfo indexExpression;
}
