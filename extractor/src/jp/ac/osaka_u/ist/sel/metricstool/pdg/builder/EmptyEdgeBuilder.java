package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

public class EmptyEdgeBuilder extends EdgeBuilder<ExecutableElementInfo> {

	EmptyEdgeBuilder(final ExecutableElementInfo element) {
		super(element);
	}

	@Override
	public void addInterproceduralEdge() {
	}
}
