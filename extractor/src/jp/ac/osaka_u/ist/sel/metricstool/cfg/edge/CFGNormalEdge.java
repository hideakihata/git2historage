package jp.ac.osaka_u.ist.sel.metricstool.cfg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;

public class CFGNormalEdge extends CFGEdge {

	public CFGNormalEdge(CFGNode<?> fromNode, final CFGNode<?> toNode) {
		super(fromNode, toNode);
	}

	@Override
	public String getDependenceTypeString() {
		return "normal";
	}

	@Override
	public String getDependenceString() {
		return "";
	}

	@Override
	public CFGEdge replaceFromNode(final CFGNode<?> newFromNode) {
		final CFGNode<?> toNode = this.getToNode();
		return new CFGNormalEdge(newFromNode, toNode);
	}

	@Override
	public CFGEdge replaceToNode(final CFGNode<?> newToNode) {
		final CFGNode<?> fromNode = this.getFromNode();
		return new CFGNormalEdge(fromNode, newToNode);
	}

	@Override
	public CFGEdge replaceBothNodes(final CFGNode<?> newFromNode,
			final CFGNode<?> newToNode) {
		return new CFGNormalEdge(newFromNode, newToNode);
	}
}
