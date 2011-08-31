package jp.ac.osaka_u.ist.sel.metricstool.cfg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;

public class CFGControlEdge extends CFGEdge {

	public CFGControlEdge(CFGNode<?> fromNode, final CFGNode<?> toNode,
			final boolean control) {
		super(fromNode, toNode);
		this.control = control;
	}

	public boolean getControl() {
		return this.control;
	}

	@Override
	public String getDependenceString() {
		return Boolean.toString(this.control);
	}

	@Override
	public String getDependenceTypeString() {
		return "control";
	}

	@Override
	public CFGEdge replaceFromNode(final CFGNode<?> newFromNode) {
		final CFGNode<?> toNode = this.getToNode();
		final boolean control = this.getControl();
		return new CFGControlEdge(newFromNode, toNode, control);
	}

	@Override
	public CFGEdge replaceToNode(final CFGNode<?> newToNode) {
		final CFGNode<?> fromNode = this.getFromNode();
		final boolean control = this.getControl();
		return new CFGControlEdge(fromNode, newToNode, control);
	}

	@Override
	public CFGEdge replaceBothNodes(final CFGNode<?> newFromNode,
			final CFGNode<?> newToNode) {
		final boolean control = this.getControl();
		return new CFGControlEdge(newFromNode, newToNode, control);
	}

	private final boolean control;
}
