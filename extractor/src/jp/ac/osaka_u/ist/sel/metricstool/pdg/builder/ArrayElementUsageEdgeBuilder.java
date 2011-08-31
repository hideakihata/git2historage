package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;

public class ArrayElementUsageEdgeBuilder extends
		EdgeBuilder<ArrayElementUsageInfo> {

	ArrayElementUsageEdgeBuilder(final ArrayElementUsageInfo element) {
		super(element);
	}

	@Override
	public void addInterproceduralEdge() {

		{
			final ExpressionInfo expression = this.statement
					.getIndexExpression();
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(expression);
			builder.addInterproceduralEdge();
		}

		{
			final ExpressionInfo expression = this.statement
					.getQualifierExpression();
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(expression);
			builder.addInterproceduralEdge();
		}
	}
}
