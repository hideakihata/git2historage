package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scorpio.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.data.NodePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class MethodSlicing extends Slicing {

	final private PDGNode<?> pointA;
	final private PDGNode<?> pointB;

	private ClonePairInfo clonepair;

	public MethodSlicing(final PDGNode<?> pointA, final PDGNode<?> pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.clonepair = null;
	}

	public ClonePairInfo perform() {
		if (null == this.clonepair) {
			final Set<PDGNode<?>> predecessorsA = new HashSet<PDGNode<?>>();
			final Set<PDGNode<?>> predecessorsB = new HashSet<PDGNode<?>>();
			this.clonepair = this.perform(this.pointA, this.pointB,
					predecessorsA, predecessorsB);

			// 共通部分を取り除く処理
			// final SortedSet<PDGNode<?>> commonNodes = new
			// TreeSet<PDGNode<?>>();
			// commonNodes.addAll(this.clonepair.codecloneA.getAllElements());
			// commonNodes.retainAll(this.clonepair.codecloneB.getAllElements());
			// this.clonepair.codecloneA.removeAll(commonNodes);
			// this.clonepair.codecloneB.removeAll(commonNodes);
		}
		return this.clonepair;
	}

	private ClonePairInfo perform(final PDGNode<?> nodeA,
			final PDGNode<?> nodeB, final Set<PDGNode<?>> predecessorsA,
			final Set<PDGNode<?>> predecessorsB) {

		// すでに利用されたノードのペアかどうかをチェック
		final NodePairInfo nodepair = NodePairInfo.getInstance(nodeA, nodeB);
		if (NODE_PAIR_CACHE.contains(nodepair)) {
			return new ClonePairInfo();
		}

		// このノードを前任者セットに追加，この処理は再帰呼び出しの前でなければならない
		predecessorsA.add(nodeA);
		predecessorsB.add(nodeB);

		// ここから，各エッジの先にあるノードの集合を得るための処理
		final SortedSet<PDGEdge> backwardEdgesA = nodeA.getBackwardEdges();
		final SortedSet<PDGEdge> backwardEdgesB = nodeB.getBackwardEdges();
		final SortedSet<PDGEdge> forwardEdgesA = nodeA.getForwardEdges();
		final SortedSet<PDGEdge> forwardEdgesB = nodeB.getForwardEdges();

		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesA = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(backwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesA = PDGDataDependenceEdge
				.extractDataDependenceEdge(backwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesA = PDGControlDependenceEdge
				.extractControlDependenceEdge(backwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesB = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(backwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesB = PDGDataDependenceEdge
				.extractDataDependenceEdge(backwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesB = PDGControlDependenceEdge
				.extractControlDependenceEdge(backwardEdgesB);

		final SortedSet<PDGNode<?>> backwardExecutionNodesA = this
				.getFromNodes(backwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> backwardDataNodesA = this
				.getFromNodes(backwardDataEdgesA);
		final SortedSet<PDGNode<?>> backwardControlNodesA = this
				.getFromNodes(backwardControlEdgesA);
		final SortedSet<PDGNode<?>> backwardExecutionNodesB = this
				.getFromNodes(backwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> backwardDataNodesB = this
				.getFromNodes(backwardDataEdgesB);
		final SortedSet<PDGNode<?>> backwardControlNodesB = this
				.getFromNodes(backwardControlEdgesB);

		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesA = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(forwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesA = PDGDataDependenceEdge
				.extractDataDependenceEdge(forwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesA = PDGControlDependenceEdge
				.extractControlDependenceEdge(forwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesB = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(forwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesB = PDGDataDependenceEdge
				.extractDataDependenceEdge(forwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesB = PDGControlDependenceEdge
				.extractControlDependenceEdge(forwardEdgesB);

		final SortedSet<PDGNode<?>> forwardExecutionNodesA = this
				.getToNodes(forwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> forwardDataNodesA = this
				.getToNodes(forwardDataEdgesA);
		final SortedSet<PDGNode<?>> forwardControlNodesA = this
				.getToNodes(forwardControlEdgesA);
		final SortedSet<PDGNode<?>> forwardExecutionNodesB = this
				.getToNodes(forwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> forwardDataNodesB = this
				.getToNodes(forwardDataEdgesB);
		final SortedSet<PDGNode<?>> forwardControlNodesB = this
				.getToNodes(forwardControlEdgesB);

		// 各ノードの集合に対してその先にあるクローンペアの構築
		final ClonePairInfo clonepair = new ClonePairInfo();

		{ // バックワードスライスを使う設定の場合
			final ClonePairInfo predicessor1 = this.enlargeClonePair(
					backwardExecutionNodesA, backwardExecutionNodesB,
					predecessorsA, predecessorsB);
			clonepair.add(predicessor1);
			final ClonePairInfo predicessor2 = this.enlargeClonePair(
					backwardDataNodesA, backwardDataNodesB, predecessorsA,
					predecessorsB);
			clonepair.add(predicessor2);
			final ClonePairInfo predicessor3 = this.enlargeClonePair(
					backwardControlNodesA, backwardControlNodesB,
					predecessorsA, predecessorsB);
			clonepair.add(predicessor3);
		}

		{ // フォワードスライスを使う設定の場合
			final ClonePairInfo successor1 = this.enlargeClonePair(
					forwardExecutionNodesA, forwardExecutionNodesB,
					predecessorsA, predecessorsB);
			clonepair.add(successor1);
			final ClonePairInfo successor2 = this.enlargeClonePair(
					forwardDataNodesA, forwardDataNodesB, predecessorsA,
					predecessorsB);
			clonepair.add(successor2);
			final ClonePairInfo successor3 = this.enlargeClonePair(
					forwardControlNodesA, forwardControlNodesB, predecessorsA,
					predecessorsB);
			clonepair.add(successor3);
		}

		NODE_PAIR_CACHE.add(nodepair);
		clonepair.add(nodepair);
		return clonepair;
	}

	private ClonePairInfo enlargeClonePair(final SortedSet<PDGNode<?>> nodesA,
			final SortedSet<PDGNode<?>> nodesB,
			final Set<PDGNode<?>> predecessorsA,
			final Set<PDGNode<?>> predecessorsB) {

		final ClonePairInfo clonepair = new ClonePairInfo();

		NODEA: for (final PDGNode<?> nodeA : nodesA) {

			// 既にクローンに入ることが確定しているノードのときは調査しない
			// 相手側のクローンに入っているノードのときも調査しない
			if (predecessorsA.contains(nodeA) || predecessorsB.contains(nodeA)) {
				continue NODEA;
			}

			// データノードの時は調査しない
			if (nodeA instanceof PDGDataNode<?>) {
				continue NODEA;
			}

			NODEB: for (final PDGNode<?> nodeB : nodesB) {

				// 既にクローンに入ることが確定しているノードのときは調査しない
				// 相手側のクローンに入っているノードのときも調査しない
				if (predecessorsB.contains(nodeB)
						|| predecessorsA.contains(nodeB)) {
					continue NODEB;
				}

				// データノードの時は調査しない
				if (nodeB instanceof PDGDataNode<?>) {
					continue NODEB;
				}

				// ノードAのハッシュ値を得る
				Integer hashA = NODE_TO_HASH_MAP.get(nodeA);
				if (null == hashA) {
					final ExecutableElementInfo coreA = nodeA.getCore();
					hashA = Conversion.getNormalizedElement(coreA).hashCode();
					NODE_TO_HASH_MAP.put(nodeA, hashA);
				}

				// ノードBのハッシュ値を得る
				Integer hashB = NODE_TO_HASH_MAP.get(nodeB);
				if (null == hashB) {
					final ExecutableElementInfo coreB = nodeB.getCore();
					hashB = Conversion.getNormalizedElement(coreB).hashCode();
					NODE_TO_HASH_MAP.put(nodeB, hashB);
				}

				SlicingThread.increaseNumberOfComparison();

				// ノードのハッシュ値が等しい場合は，そのノードペアの先をさらに調査
				if (hashA.equals(hashB)) {

					// ノードが同じ場合は調査しない
					if (nodeA == nodeB) {
						continue;
					}

					final Set<PDGNode<?>> newPredicessorsA = new HashSet<PDGNode<?>>(
							predecessorsA);
					final Set<PDGNode<?>> newPredicessorsB = new HashSet<PDGNode<?>>(
							predecessorsB);
					final ClonePairInfo successor = this.perform(nodeA, nodeB,
							newPredicessorsA, newPredicessorsB);
					clonepair.add(successor);
				}
			}
		}

		return clonepair;
	}
}
