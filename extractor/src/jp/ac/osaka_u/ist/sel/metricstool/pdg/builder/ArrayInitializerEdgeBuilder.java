package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;

public class ArrayInitializerEdgeBuilder extends
		EdgeBuilder<ArrayInitializerInfo> {

	ArrayInitializerEdgeBuilder(final ArrayInitializerInfo element) {
		super(element);
	}

	@Override
	public void addInterproceduralEdge() {
		for (final ExpressionInfo expression : this.statement
				.getElementInitializers()) {
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(expression);
			builder.addInterproceduralEdge();
		}
	}
}
