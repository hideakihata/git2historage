package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;

public class ConditionalBlockEdgeBuilder<T extends ConditionalBlockInfo>
		extends BlockEdgeBuilder<T> {

	ConditionalBlockEdgeBuilder(final T block) {
		super(block);
	}

	@Override
	public void addInterproceduralEdge() {
		super.addInterproceduralEdge();
		final ConditionInfo condition = this.statement.getConditionalClause()
				.getCondition();
		final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(condition);
		builder.addInterproceduralEdge();
	}

}
