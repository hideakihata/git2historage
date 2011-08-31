package jp.ac.osaka_u.ist.sel.metricstool.cfg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;

public class CFGJumpEdge extends CFGEdge {

	public CFGJumpEdge(CFGNode<?> fromNode, final CFGNode<?> toNode) {
		super(fromNode, toNode);
	}

	@Override
	public String getDependenceTypeString() {
		return "jump";
	}

	@Override
	public String getDependenceString() {
		return "jump";
	}

	@Override
	public CFGEdge replaceFromNode(final CFGNode<?> newFromNode) {
		final CFGNode<?> toNode = this.getToNode();
		return new CFGJumpEdge(newFromNode, toNode);
	}

	@Override
	public CFGEdge replaceToNode(final CFGNode<?> newToNode) {
		final CFGNode<?> fromNode = this.getFromNode();
		return new CFGJumpEdge(fromNode, newToNode);
	}

	@Override
	public CFGEdge replaceBothNodes(final CFGNode<?> newFromNode,
			final CFGNode<?> newToNode) {
		return new CFGJumpEdge(newFromNode, newToNode);
	}
}
