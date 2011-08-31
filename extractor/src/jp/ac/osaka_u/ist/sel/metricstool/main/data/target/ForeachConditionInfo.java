package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Foreach •¶‚ÌŽ®‚ð•\‚·ƒNƒ‰ƒX
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ForeachConditionInfo extends ExpressionInfo {

    public ForeachConditionInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn,
            final VariableDeclarationStatementInfo iteratorVariable,
            final ExpressionInfo iteratorExpression) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.iteratorVariable = iteratorVariable;
        this.iteratorExpression = iteratorExpression;

        this.iteratorExpression.setOwnerExecutableElement(this);
    }

    public VariableDeclarationStatementInfo getIteratorVariable() {
        return this.iteratorVariable;
    }

    public ExpressionInfo getIteratorExpression() {
        return this.iteratorExpression;
    }

    @Override
    public TypeInfo getType() {
        return this.getIteratorVariable().getType();
    }

    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getIteratorExpression().getCalls();
    }

    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();

        text.append(this.getIteratorVariable().getType().getTypeName());
        text.append(" ");
        text.append(this.getIteratorVariable().getDeclaredLocalVariable().getName());
        text.append(" : ");
        text.append(this.getIteratorExpression().getText());

        return text.toString();
    }

    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return this.getIteratorExpression().getThrownExceptions();
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        variableUsages.addAll(this.getIteratorExpression().getVariableUsages());
        variableUsages.addAll(iteratorVariable.getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
    }

    @Override
    public ExecutableElementInfo copy() {
        final VariableDeclarationStatementInfo iteratorVariable = (VariableDeclarationStatementInfo) this
                .getIteratorVariable().copy();
        final ExpressionInfo iteratorExpression = (ExpressionInfo) this.getIteratorExpression()
                .copy();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ForeachConditionInfo newForeachCondition = new ForeachConditionInfo(ownerMethod,
                fromLine, fromColumn, toLine, toColumn, iteratorVariable, iteratorExpression);

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newForeachCondition.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newForeachCondition.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newForeachCondition;
    }

    private final VariableDeclarationStatementInfo iteratorVariable;

    private final ExpressionInfo iteratorExpression;
}
