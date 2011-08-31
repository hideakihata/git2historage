package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;


/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionStatementNode extends CFGStatementNode<ExpressionStatementInfo> {

    /**
     * ê∂ê¨Ç∑ÇÈÉmÅ[ÉhÇ…ëŒâûÇ∑ÇÈï∂Çó^Ç¶Çƒèâä˙âª
     * 
     * @param statement
     *            ê∂ê¨Ç∑ÇÈÉmÅ[ÉhÇ…ëŒâûÇ∑ÇÈï∂
     */
    CFGExpressionStatementNode(final ExpressionStatementInfo statement) {
        super(statement);
    }

    @Override
    ExpressionInfo getDissolvingTarget() {

        final ExpressionStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getExpression();

        if (expression instanceof BinominalOperationInfo) {

            final BinominalOperationInfo binominalOperation = (BinominalOperationInfo) expression;
            final OPERATOR operator = binominalOperation.getOperator();

            if (operator.equals(OPERATOR.ASSIGN)) {
                return (ExpressionInfo) binominalOperation.getSecondOperand();
            }
        }

        return statement.getExpression();
    }

    @Override
    ExpressionStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn,
            final ExpressionInfo... requiredExpressions) {

        if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
            throw new IllegalArgumentException();
        }

        final ExpressionStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getExpression();

        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        if (expression instanceof BinominalOperationInfo) {

            final BinominalOperationInfo binominalOperation = (BinominalOperationInfo) expression;
            final OPERATOR operator = binominalOperation.getOperator();
            final ExpressionInfo firstOperand = binominalOperation.getFirstOperand();

            if (operator.equals(OPERATOR.ASSIGN)) {
                final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
                        operator, firstOperand, requiredExpressions[0], outerCallableUnit,
                        fromLine, fromColumn, toLine, toColumn);
                final ExpressionStatementInfo newStatement = new ExpressionStatementInfo(
                        ownerSpace, newBinominalOperation, fromLine, fromColumn, toLine, toColumn);
                return newStatement;
            }
        }

        final ExpressionStatementInfo newStatement = new ExpressionStatementInfo(ownerSpace,
                requiredExpressions[0], fromLine, fromColumn, toLine, toColumn);
        return newStatement;
    }

    @Override
    ExpressionStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpressions) {

        if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
            throw new IllegalArgumentException();
        }

        final ExpressionStatementInfo statement = this.getCore();
        final int fromLine = statement.getFromLine();
        final int fromColumn = statement.getFromColumn();
        final int toLine = statement.getToLine();
        final int toColumn = statement.getToColumn();

        return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine, toColumn,
                requiredExpressions);
    }
}
