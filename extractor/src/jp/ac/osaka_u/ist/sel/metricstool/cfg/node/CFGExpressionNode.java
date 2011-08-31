package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;

public class CFGExpressionNode extends CFGNormalNode<ExpressionInfo> {

	CFGExpressionNode(final ExpressionInfo expression) {
		super(expression);
	}

	@Override
	ExpressionInfo getDissolvingTarget() {

		final ExpressionInfo expression = this.getCore();

		if (expression instanceof BinominalOperationInfo) {

			final BinominalOperationInfo binominalOperation = (BinominalOperationInfo) expression;
			final OPERATOR operator = binominalOperation.getOperator();

			if (operator.equals(OPERATOR.ASSIGN)) {
				return binominalOperation.getSecondOperand();
			}
		}

		return expression;
	}

	@Override
	ExpressionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ExpressionInfo expression = this.getCore();
		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		if (expression instanceof BinominalOperationInfo) {

			final BinominalOperationInfo binominalOperation = (BinominalOperationInfo) expression;
			final OPERATOR operator = binominalOperation.getOperator();
			final ExpressionInfo firstOperand = binominalOperation
					.getFirstOperand();

			if (operator.equals(OPERATOR.ASSIGN)) {
				final BinominalOperationInfo newExpression = new BinominalOperationInfo(
						operator, firstOperand, requiredExpressions[0],
						outerCallableUnit, fromLine, fromColumn, toLine,
						toColumn);
				return newExpression;
			}
		}

		return requiredExpressions[0];
	}

	@Override
	ExpressionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ExpressionInfo expression = this.getCore();
		final int fromLine = expression.getFromLine();
		final int fromColumn = expression.getFromColumn();
		final int toLine = expression.getToLine();
		final int toColumn = expression.getToColumn();

		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpressions);
	}
}
