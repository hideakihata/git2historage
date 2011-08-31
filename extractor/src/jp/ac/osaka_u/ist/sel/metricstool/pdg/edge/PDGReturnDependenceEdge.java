package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class PDGReturnDependenceEdge extends PDGEdge {

	public PDGReturnDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode) {
		super(PDG_EDGE_TYPE.RETURN, fromNode, toNode);
	}

	@Override
	public String getDependenceString() {
		return "return";
	}

	@Override
	public String getDependenceTypeString() {
		return "Return Dependency";
	}
}
