package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

public class BlockEdgeBuilder<T extends BlockInfo> extends EdgeBuilder<T> {

	BlockEdgeBuilder(final T block) {
		super(block);
		if (block instanceof ConditionalBlockInfo) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void addInterproceduralEdge() {
		for (final StatementInfo inner : this.statement.getStatements()) {
			final EdgeBuilder<?> builder = EdgeBuilder.getBuilder(inner);
			builder.addInterproceduralEdge();
		}
	}
}
