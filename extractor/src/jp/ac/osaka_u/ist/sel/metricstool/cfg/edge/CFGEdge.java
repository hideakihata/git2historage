package jp.ac.osaka_u.ist.sel.metricstool.cfg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

public abstract class CFGEdge implements Comparable<CFGEdge> {

	private final CFGNode<?> fromNode;

	private final CFGNode<?> toNode;

	public CFGEdge(CFGNode<?> fromNode, final CFGNode<?> toNode) {
		if (null == fromNode || null == toNode) {
			throw new IllegalArgumentException();
		}

		this.fromNode = fromNode;
		this.toNode = toNode;

		if (!((ExecutableElementInfo) fromNode.getCore()).getOwnerMethod()
				.equals(
						((ExecutableElementInfo) toNode.getCore())
								.getOwnerMethod())) {
			System.out.println();
		}
	}

	public abstract String getDependenceTypeString();

	public abstract String getDependenceString();

	public final CFGNode<?> getFromNode() {
		return this.fromNode;
	}

	public final CFGNode<?> getToNode() {
		return this.toNode;
	}

	@Override
	public boolean equals(Object arg) {
		if (this.getClass().equals(arg.getClass())) {
			CFGEdge edge = (CFGEdge) arg;
			return this.fromNode.equals(edge.getFromNode())
					&& this.toNode.equals(getToNode());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int fromHash = this.fromNode.hashCode() * 10;
		final int toHash = this.toNode.hashCode();
		return fromHash + toHash;
	}

	@Override
	public int compareTo(final CFGEdge edge) {

		if (null == edge) {
			throw new IllegalArgumentException();
		}

		final int fromOrder = this.getFromNode().compareTo(edge.getFromNode());
		if (0 != fromOrder) {
			return fromOrder;
		}

		final int toOrder = this.getToNode().compareTo(edge.getToNode());
		if (0 != toOrder) {
			return toOrder;
		}

		return this.getDependenceTypeString().compareTo(
				edge.getDependenceTypeString());
	}

	/**
	 * fromNodeを引数で与えられたノードで置換したエッジを返す
	 * 
	 * @param node
	 * @return
	 */
	abstract public CFGEdge replaceFromNode(final CFGNode<?> newFromNode);

	/**
	 * toNodeを引数で与えられたノードで置換したエッジを返す
	 * 
	 * @param node
	 * @return
	 */
	abstract public CFGEdge replaceToNode(final CFGNode<?> newToNode);

	/**
	 * 両方のノードを引数で与えられたノードで置換したエッジを返す
	 * 
	 * @param newFromNode
	 * @param newToNode
	 * @return
	 */
	abstract public CFGEdge replaceBothNodes(final CFGNode<?> newFromNode,
			final CFGNode<?> newToNode);
}
