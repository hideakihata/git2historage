package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 空の式を表すクラス
 * return ; や for ( ; ; ) などに用いる
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class EmptyExpressionInfo extends ExpressionInfo {

    /**
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public EmptyExpressionInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * void 型を返す
     * 
     * return void型 
     */
    @Override
    public TypeInfo getType() {
        return VoidTypeInfo.getInstance();
    }

    /**
     * 長さ0のStringを返す
     * 
     * return 長さ0のString
     */
    @Override
    public String getText() {
        return "";
    }

    /**
     * 使用されている変数のSetを返す．
     * 実際はなにも使用されていないので，空のSetが返される．
     * 
     * @return 使用されている変数のSet
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    @Override
    public ExecutableElementInfo copy() {
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final EmptyExpressionInfo newEmptyExpression = new EmptyExpressionInfo(ownerMethod,
                fromLine, fromColumn, toLine, toColumn);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newEmptyExpression.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newEmptyExpression.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newEmptyExpression;

    }
}
