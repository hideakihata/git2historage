package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;

public class ForBlockEdgeBuilder extends
		ConditionalBlockEdgeBuilder<ForBlockInfo> {

	ForBlockEdgeBuilder(final ForBlockInfo block) {
		super(block);
	}

	@Override
	public void addInterproceduralEdge() {
		super.addInterproceduralEdge();
		for (final ConditionInfo initializer : this.statement
				.getInitializerExpressions()) {
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(initializer);
			builder.addInterproceduralEdge();
		}
		for (final ExpressionInfo iterator : this.statement
				.getIteratorExpressions()) {
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(iterator);
			builder.addInterproceduralEdge();
		}
	}
}
