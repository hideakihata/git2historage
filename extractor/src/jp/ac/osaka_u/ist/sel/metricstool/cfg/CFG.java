package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

public abstract class CFG {

	protected final ICFGNodeFactory nodeFactory;

	protected final Set<CFGNode<? extends ExecutableElementInfo>> nodes;

	protected CFGNode<? extends ExecutableElementInfo> enterNode;

	protected final Set<CFGNode<? extends ExecutableElementInfo>> exitNodes;

	protected CFG(final ICFGNodeFactory nodeFactory) {
		if (null == nodeFactory) {
			throw new IllegalArgumentException();
		}

		this.nodeFactory = nodeFactory;
		this.nodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		this.enterNode = null;
		this.exitNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
	}

	/*
	 * protected void connectNodes(final Set<CFGNode<? extends StatementInfo>>
	 * fromNodes, CFGNode<? extends StatementInfo> toNode) { for (final
	 * CFGNode<? extends StatementInfo> fromNode : fromNodes) {
	 * fromNode.addForwardNode(toNode); } }
	 */

	/**
	 * CFGの入り口ノードを返す
	 * 
	 * @return CFGの入り口ノード
	 */
	public final CFGNode<? extends ExecutableElementInfo> getEnterNode() {
		return this.enterNode;
	}

	/**
	 * CFGの出口ノードを返す
	 * 
	 * @return CFGの出口ノード
	 */
	public final Set<CFGNode<? extends ExecutableElementInfo>> getExitNodes() {
		return Collections.unmodifiableSet(this.exitNodes);
	}

	/**
	 * CFGの全ノードを返す
	 * 
	 * @return CFGの全ノード
	 */
	public final Set<CFGNode<? extends ExecutableElementInfo>> getAllNodes() {
		return Collections.unmodifiableSet(this.nodes);
	}

	/**
	 * 引数で与えられたノードから到達可能なノードを返す
	 * 
	 * @param startNode
	 *            開始ノード
	 * @return 引数で与えられたノードから到達可能なノード
	 */
	public final Set<CFGNode<? extends ExecutableElementInfo>> getReachableNodes(
			final CFGNode<? extends ExecutableElementInfo> startNode) {

		if (null == startNode) {
			throw new IllegalArgumentException();
		}

		final Set<CFGNode<? extends ExecutableElementInfo>> nodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		this.getReachableNodes(startNode, nodes);

		return Collections.unmodifiableSet(nodes);
	}

	private final void getReachableNodes(
			final CFGNode<? extends ExecutableElementInfo> startNode,
			final Set<CFGNode<? extends ExecutableElementInfo>> nodes) {

		if ((null == startNode) || (null == nodes)) {
			throw new IllegalArgumentException();
		}

		if (nodes.contains(startNode)) {
			return;
		}

		nodes.add(startNode);
		for (final CFGNode<? extends ExecutableElementInfo> node : startNode
				.getForwardNodes()) {
			this.getReachableNodes(node, nodes);
		}
	}

	public boolean isEmpty() {
		return null == this.enterNode;
	}

	public CFGNode<? extends ExecutableElementInfo> getCFGNode(
			final StatementInfo statement) {
		return this.nodeFactory.getNode(statement);
	}

	public ICFGNodeFactory getNodeFactory() {
		return this.nodeFactory;
	}
}
