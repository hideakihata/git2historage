package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;

public class TernaryOperationEdgeBuilder extends
		EdgeBuilder<TernaryOperationInfo> {

	TernaryOperationEdgeBuilder(final TernaryOperationInfo element) {
		super(element);
	}

	@Override
	public void addInterproceduralEdge() {

		{
			final ConditionInfo condition = this.statement.getCondition();
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(condition);
			builder.addInterproceduralEdge();
		}

		{
			final ExpressionInfo expression = this.statement
					.getTrueExpression();
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(expression);
			builder.addInterproceduralEdge();
		}

		{
			final ExpressionInfo expression = this.statement
					.getFalseExpression();
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(expression);
			builder.addInterproceduralEdge();
		}
	}
}
