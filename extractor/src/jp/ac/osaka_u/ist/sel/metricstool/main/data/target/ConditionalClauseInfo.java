package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * 条件文の条件節の情報を表すクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public final class ConditionalClauseInfo extends UnitInfo {

    /**
     * 条件節を保持するブロック文と位置情報
     * @param ownerConditionalBlock 条件文の条件節を保持するブロック
     * @param condition 条件節に記述されている条件
     * @param fromLine 開始行
     * @param fromColumn 開始位置
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ConditionalClauseInfo(final ConditionalBlockInfo ownerConditionalBlock,
            final ConditionInfo condition, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == ownerConditionalBlock) {
            throw new IllegalArgumentException();
        }

        this.ownerConditionalBlock = ownerConditionalBlock;
        if (null != condition) {
            this.condition = condition;
            if (this.condition instanceof ExpressionInfo) {
                ((ExpressionInfo) this.condition)
                        .setOwnerExecutableElement(this.ownerConditionalBlock);
            }
        } else {

            final CallableUnitInfo ownerMethod = ownerConditionalBlock.getOwnerMethod();
            this.condition = new EmptyExpressionInfo(ownerMethod, toLine, toColumn - 1, toLine,
                    toColumn - 1);
            ((ExpressionInfo) this.condition).setOwnerExecutableElement(this.ownerConditionalBlock);
        }
    }

    /**
     * 条件節を保持するブロックを返す
     * @return 条件節を保持するブロック
     */
    public final ConditionalBlockInfo getOwnerConditionalBlock() {
        return this.ownerConditionalBlock;
    }

    /**
     * 条件節に記述されている条件を返す
     * @return 条件節に記述されている条件
     */
    public final ConditionInfo getCondition() {
        return this.condition;
    }

    /**
     * 条件節のテキスト表現を返す
     * 
     * @return 条件節のテキスト表現
     */
    public final String getText() {
        return this.getCondition().getText();
    }

    /**
     * この条件節のハッシュコードを返す
     */
    @Override
    public final int hashCode() {
        return this.getCondition().hashCode();
    }

    /**
     * 条件節内における変数使用のSetを返す
     * 
     * @return 条件節内における変数使用のSet 
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return this.getCondition().getVariableUsages();
    }

    /**
     * 条件節で定義された変数のSetを返す
     * 
     * @return 条件節で定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return this.getCondition().getDefinedVariables();
    }

    /**
     * 条件節における呼び出しのSetを返す
     * 
     * @return 条件sつにおける呼び出しのSet
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return this.getCondition().getCalls();
    }

    public ConditionalClauseInfo copy() {

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        final ConditionInfo condition = (ConditionInfo) this.getCondition().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(
                ownerConditionalBlock, condition, fromLine, fromColumn, toLine, toColumn);

        return newConditionalClause;
    }

    /**
     * 条件節を保持するブロックを表す変数
     */
    private final ConditionalBlockInfo ownerConditionalBlock;

    /**
     * 条件節に記述されている条件を表す変数
     */
    private final ConditionInfo condition;
}
