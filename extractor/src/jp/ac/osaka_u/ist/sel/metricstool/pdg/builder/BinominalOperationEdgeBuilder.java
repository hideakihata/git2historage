package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;

public class BinominalOperationEdgeBuilder extends
		EdgeBuilder<BinominalOperationInfo> {

	BinominalOperationEdgeBuilder(final BinominalOperationInfo element) {
		super(element);
	}

	@Override
	public void addInterproceduralEdge() {

		{
			final ExpressionInfo expression = this.statement.getFirstOperand();
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(expression);
			builder.addInterproceduralEdge();
		}

		{
			final ExpressionInfo expression = this.statement.getSecondOperand();
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(expression);
			builder.addInterproceduralEdge();
		}
	}
}
