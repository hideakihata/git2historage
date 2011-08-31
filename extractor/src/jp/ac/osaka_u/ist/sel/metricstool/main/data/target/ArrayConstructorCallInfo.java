package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 配列コンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayConstructorCallInfo extends ConstructorCallInfo<ArrayTypeInfo> {

    /**
     * 型を与えて配列コンストラクタ呼び出しを初期化
     * 
     * @param arrayType 呼び出しの型
     * @param ownerMethod オーナーメソッド 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列 
     */
    public ArrayConstructorCallInfo(final ArrayTypeInfo arrayType,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(arrayType, null, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.indexExpressions = new TreeMap<Integer, ExpressionInfo>();
    }

    public void addIndexExpression(final int dimension, final ExpressionInfo indexExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == indexExpression) {
            throw new IllegalArgumentException();
        }

        this.indexExpressions.put(dimension, indexExpression);
        indexExpression.setOwnerExecutableElement(this);
    }

    /**
     * インデックスの式を取得
     * @param dimention インデックスの式を取得する配列の次元
     * @return 指定した次元のインデックスの式
     */
    public ExpressionInfo getIndexExpression(final int dimention) {
        return this.indexExpressions.get(dimention);
    }

    /**
     * インデックスの式のリストを取得
     * 
     * @return インデックスの式のリスト 
     */
    public SortedMap<Integer, ExpressionInfo> getIndexExpressions() {
        return Collections.unmodifiableSortedMap(this.indexExpressions);
    }

    /**
     * 配列の初期化式のテキスト表現を返す
     * 
     * @return 配列の初期化式のテキスト表現
     * 
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        text.append("new ");

        final ArrayTypeInfo arrayType = this.getType();
        final TypeInfo elementType = arrayType.getElementType();
        text.append(elementType.getTypeName());

        for (final ExpressionInfo indexExpression : this.getIndexExpressions().values()) {
            text.append("[");
            text.append(indexExpression.getText());
            text.append("]");
        }

        final List<ExpressionInfo> arguments = this.getArguments();
        if (0 < arguments.size()) {
            text.append("{");
            for (final ExpressionInfo argument : arguments) {
                text.append(argument.getText());
                text.append(",");
            }
            text.deleteCharAt(text.length() - 1);
            text.append("}");
        }
        return text.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        thrownExceptions.addAll(super.getThrownExceptions());
        for (final ExpressionInfo indexExpression : this.getIndexExpressions().values()) {
            thrownExceptions.addAll(indexExpression.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExceptions);
    }

    /**
     * この呼び出しにおける変数使用群を返す
     * 
     * @return この呼び出しにおける変数使用群
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());
        for (final ExpressionInfo indexExpression : this.getIndexExpressions().values()) {
            variableUsages.addAll(indexExpression.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    @Override
    public ExecutableElementInfo copy() {

        final ArrayTypeInfo arrayType = this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ArrayConstructorCallInfo newCall = new ArrayConstructorCallInfo(arrayType,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);

        for (final ExpressionInfo argument : this.getArguments()) {
            newCall.addArgument((ExpressionInfo) argument.copy());
        }

        for (final Entry<Integer, ExpressionInfo> entry : this.getIndexExpressions().entrySet()) {
            final Integer dimension = entry.getKey();
            final ExpressionInfo indexExpression = (ExpressionInfo) entry.getValue().copy();
            newCall.addIndexExpression(dimension, indexExpression);
        }

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCall.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCall.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCall;
    }

    private final SortedMap<Integer, ExpressionInfo> indexExpressions;
}
