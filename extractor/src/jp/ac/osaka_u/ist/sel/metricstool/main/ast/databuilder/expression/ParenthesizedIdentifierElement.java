package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParenthesesExpressionInfo;


/**
 * This class represents 'Parenthesized Identifier' like below:
 * (a) + (b)
 * @author g-yamada
 */
public class ParenthesizedIdentifierElement extends IdentifierElement {

    public ParenthesizedIdentifierElement(final IdentifierElement parenthesizedElement,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(parenthesizedElement.getName(), fromLine, fromColumn, toLine, toColumn);
        this.parenthesizedElement = parenthesizedElement;

        this.qualifiedName = new String[] { parenthesizedElement.getName() };
    }

    @Override
    public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveAsVariable(
            final BuildDataManager buildDataManager, final boolean reference,
            final boolean assignment) {
        this.usage = this.parenthesizedElement.resolveAsVariable(buildDataManager, reference,
                assignment);
        return this.buildParenthesesExpression();
        //return parenthesizedElement.resolveAsVariable(buildDataManager, reference, assignment);
    }

    @Override
    public IdentifierElement resolveAsCalledMethod(final BuildDataManager buildDataManager) {
        //return this.parenthesizedElement instanceof FieldOrMethodElement ? parenthesizedElement
//                : this.parenthesizedElement.resolveAsCalledMethod(buildDataManager);
        return parenthesizedElement.resolveAsCalledMethod(buildDataManager);
    }

    @Override
    public UnresolvedExpressionInfo<? extends ExpressionInfo> resolveReferencedEntityIfPossible(
            final BuildDataManager buildDataManager) {
        /*this.usage = this.parenthesizedElement instanceof FieldOrMethodElement ? this.parenthesizedElement
                .resolveAsCalledMethod(buildDataManager).getUsage() : parenthesizedElement
                .resolveReferencedEntityIfPossible(buildDataManager);
        return this.buildParenthesesExpression();*/
        return this.parenthesizedElement.resolveReferencedEntityIfPossible(buildDataManager);
    }

    private final UnresolvedParenthesesExpressionInfo buildParenthesesExpression() {
        if (this.usage == null) {
            throw new NullPointerException("usage is null");
        }
        final UnresolvedParenthesesExpressionInfo parenthesesExpression = new UnresolvedParenthesesExpressionInfo(
                this.usage);
        parenthesesExpression.setOuterUnit(this.usage.getOuterUnit());
        parenthesesExpression.setFromLine(this.usage.getFromLine());
        parenthesesExpression.setFromColumn(this.usage.getFromColumn());
        parenthesesExpression.setToLine(this.usage.getToLine());
        parenthesesExpression.setToColumn(this.usage.getToColumn());
        return parenthesesExpression;
    }

    private final IdentifierElement parenthesizedElement;
}
