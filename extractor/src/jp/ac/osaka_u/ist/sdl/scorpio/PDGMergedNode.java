package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;

public class PDGMergedNode extends PDGNormalNode<CFGNormalNode<?>> {

	/**
	 * 引数で与えられたPDGの頂点圧縮を行う
	 * 
	 * @param pdg
	 */
	public static void merge(final IntraProceduralPDG pdg,
			final IPDGNodeFactory pdgNodeFactory) {

		for (final PDGNode<?> node : PDGControlNode.getControlNodes(pdg
				.getAllNodes())) {

			// 制御ノードの次がノーマルノードであれば圧縮可能かどうかを調べる
			if (node instanceof PDGControlNode) {
				for (final PDGEdge edge : PDGExecutionDependenceEdge
						.extractExecutionDependenceEdge(node.getForwardEdges())) {
					final PDGNode<?> toNode = edge.getToNode();
					if (toNode instanceof PDGNormalNode<?>) {
						findMergedNodes(pdg, (PDGNormalNode<?>) toNode,
								pdgNodeFactory);
					}
				}
			}
		}
	}

	/**
	 * 引数で与えられた頂点から，実行依存でたどっていき，PDGNormalNodeであるかぎり，圧縮を試みる
	 * 
	 * @param node
	 */
	private static void findMergedNodes(final IntraProceduralPDG pdg,
			final PDGNormalNode<?> node, final IPDGNodeFactory pdgNodeFactory) {

		final int hash = Conversion.getNormalizedElement(node.getCore())
				.hashCode();
		final PDGMergedNode mergedNode = new PDGMergedNode(node);

		final SortedSet<PDGExecutionDependenceEdge> toEdges = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(node.getForwardEdges());
		if (toEdges.isEmpty()) {
			return;
		}

		PDGNode<?> toNode = toEdges.first().getToNode();
		if (!(toNode instanceof PDGNormalNode<?>)) { // このtoNodeがノーマルでない場合は何もしなくていい
			return;
		}
		int toHash = Conversion.getNormalizedElement(toNode.getCore())
				.hashCode();

		while (hash == toHash) {
			mergedNode.addNode((PDGNormalNode<?>) toNode);
			final SortedSet<PDGExecutionDependenceEdge> forwardEdges = PDGExecutionDependenceEdge
					.extractExecutionDependenceEdge(toNode.getForwardEdges());
			if (forwardEdges.isEmpty()) {
				insertMergedNode(pdg, mergedNode, pdgNodeFactory);
				return;
			}
			toNode = forwardEdges.first().getToNode();
			if (!(toNode instanceof PDGNormalNode<?>)) {
				insertMergedNode(pdg, mergedNode, pdgNodeFactory);
				return;
			}
			toHash = Conversion.getNormalizedElement(toNode.getCore())
					.hashCode();
		}
		insertMergedNode(pdg, mergedNode, pdgNodeFactory);
		findMergedNodes(pdg, (PDGNormalNode<?>) toNode, pdgNodeFactory);
	}

	private static void insertMergedNode(final IntraProceduralPDG pdg,
			final PDGMergedNode mergedNode, final IPDGNodeFactory pdgNodeFactory) {

		final List<PDGNormalNode<?>> originalNodes = mergedNode
				.getOriginalNodes();

		// 集約ノードの集約数が1の場合はなにもしなくていい
		if (1 == originalNodes.size()) {
			return;
		}

		// 元の先頭ノードを処理
		{
			final PDGNormalNode<?> startNode = originalNodes.get(0);
			for (final PDGEdge edge : startNode.getBackwardEdges()) {

				// 実行依存辺以外は対象外
				if (!(edge instanceof PDGExecutionDependenceEdge)) {
					continue;
				}

				final PDGNode<?> previousNode = edge.getFromNode();

				// オリジナルノードを削除
				for (final PDGEdge previousEdge : PDGExecutionDependenceEdge
						.extractExecutionDependenceEdge(previousNode
								.getForwardEdges())) {
					if (previousEdge.getToNode().equals(startNode)) {
						previousNode.removeForwardEdge(previousEdge);
					}
				}

				// 集約ノードを追加
				final PDGEdge mergedEdge = new PDGExecutionDependenceEdge(
						previousNode, mergedNode);
				previousNode.addForwardEdge(mergedEdge);
				mergedNode.addBackwardEdge(mergedEdge);
			}
		}

		// 元の最後ノードを処理
		{
			final PDGNormalNode<?> endNode = originalNodes.get(originalNodes
					.size() - 1);
			for (final PDGEdge edge : endNode.getForwardEdges()) {

				// 実行依存辺以外は対象外
				if (!(edge instanceof PDGExecutionDependenceEdge)) {
					continue;
				}

				final PDGNode<?> postiousNode = edge.getToNode();

				// オリジナルノードを削除
				for (final PDGEdge postiousEdge : PDGExecutionDependenceEdge
						.extractExecutionDependenceEdge(postiousNode
								.getBackwardEdges())) {
					if (postiousEdge.getFromNode().equals(endNode)) {
						postiousNode.removeBackwardEdge(postiousEdge);
					}
				}

				// 集約ノードを追加
				final PDGEdge mergedEdge = new PDGExecutionDependenceEdge(
						mergedNode, postiousNode);
				mergedNode.addForwardEdge(mergedEdge);
				postiousNode.addBackwardEdge(mergedEdge);
			}
		}

		// ノードファクトリへの登録と削除
		for (final PDGNode<?> node : originalNodes) {
			final ExecutableElementInfo element = node.getCore();
			pdgNodeFactory.removeNode(element);
		}
		pdgNodeFactory.addNode(mergedNode);

		// PDGへの登録と削除
		for (final PDGNode<?> node : originalNodes) {
			pdg.removeNode(node);
		}
		pdg.addNode(mergedNode);

		// 集約ノードに対するデータ依存辺，制御依存辺の構築
		for (final PDGNode<?> originalNode : originalNodes) {

			for (final PDGDataDependenceEdge edge : PDGDataDependenceEdge
					.extractDataDependenceEdge(originalNode.getBackwardEdges())) {

				final PDGNode<?> fromNode = edge.getFromNode();
				if (!originalNodes.contains(fromNode)) {
					final VariableInfo<?> variable = edge.getVariable();
					final PDGDataDependenceEdge newEdge = new PDGDataDependenceEdge(
							fromNode, mergedNode, variable);
					fromNode.addForwardEdge(newEdge);
					mergedNode.addBackwardEdge(newEdge);
					fromNode.removeForwardEdge(edge);
				}
			}

			for (final PDGDataDependenceEdge edge : PDGDataDependenceEdge
					.extractDataDependenceEdge(originalNode.getForwardEdges())) {

				final PDGNode<?> toNode = edge.getToNode();
				if (!originalNodes.contains(toNode)) {
					final VariableInfo<?> variable = edge.getVariable();
					final PDGDataDependenceEdge newEdge = new PDGDataDependenceEdge(
							mergedNode, toNode, variable);
					mergedNode.addForwardEdge(newEdge);
					toNode.addBackwardEdge(newEdge);
					toNode.removeBackwardEdge(edge);
				}
			}

			for (final PDGControlDependenceEdge edge : PDGControlDependenceEdge
					.extractControlDependenceEdge(originalNode
							.getBackwardEdges())) {

				final PDGControlNode fromNode = (PDGControlNode) edge
						.getFromNode();
				if (!originalNodes.contains(fromNode)) {
					final boolean flag = edge.isTrueDependence();
					final PDGControlDependenceEdge newEdge = new PDGControlDependenceEdge(
							fromNode, mergedNode, flag);
					fromNode.addForwardEdge(newEdge);
					mergedNode.addBackwardEdge(newEdge);
					fromNode.removeForwardEdge(edge);
				}
			}
		}
	}

	public PDGMergedNode(final PDGNormalNode<?> node) {
		super(node.getCFGNode());
		this.originalNodes = new LinkedList<PDGNormalNode<?>>();
		this.originalNodes.add(node);
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
		final SortedSet<VariableInfo<? extends UnitInfo>> variables = new TreeSet<VariableInfo<? extends UnitInfo>>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			variables.addAll(originalNode.getDefinedVariables());
		}

		return Collections.unmodifiableSortedSet(variables);
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
		final SortedSet<VariableInfo<? extends UnitInfo>> variables = new TreeSet<VariableInfo<? extends UnitInfo>>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			variables.addAll(originalNode.getReferencedVariables());
		}

		return Collections.unmodifiableSortedSet(variables);
	}

	public void addNode(final PDGNormalNode<?> node) {
		this.originalNodes.add(node);
	}

	public List<PDGNormalNode<?>> getOriginalNodes() {
		return Collections.unmodifiableList(this.originalNodes);
	}

	public SortedSet<ExecutableElementInfo> getCores() {
		final SortedSet<ExecutableElementInfo> cores = new TreeSet<ExecutableElementInfo>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			cores.add(originalNode.getCore());
		}
		return Collections.unmodifiableSortedSet(cores);
	}

	private final List<PDGNormalNode<?>> originalNodes;
}
