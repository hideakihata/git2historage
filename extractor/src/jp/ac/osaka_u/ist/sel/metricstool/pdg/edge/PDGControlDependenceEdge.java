package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * 制御依存辺を表すクラス
 * 
 * @author t-miyake, higo
 * 
 */
public class PDGControlDependenceEdge extends PDGEdge {

	/**
	 * エッジの集合から，制御依存を表すエッジのみを抽出し，そのSetを返す
	 * 
	 * @param edges
	 * @return
	 */
	public static SortedSet<PDGControlDependenceEdge> extractControlDependenceEdge(
			final Set<PDGEdge> edges) {
		final SortedSet<PDGControlDependenceEdge> controlDependenceEdges = new TreeSet<PDGControlDependenceEdge>();
		for (final PDGEdge edge : edges) {
			if (PDG_EDGE_TYPE.CONTROL == edge.type) {
				controlDependenceEdges.add((PDGControlDependenceEdge) edge);
			}
		}
		return controlDependenceEdges;
	}

	public PDGControlDependenceEdge(final PDGControlNode fromNode,
			final PDGNode<?> toNode, final boolean trueDependence) {
		super(PDG_EDGE_TYPE.CONTROL, fromNode, toNode);

		this.trueDependence = trueDependence;

	}

	public boolean isTrueDependence() {
		return this.trueDependence;
	}

	public boolean isFalseDependence() {
		return !this.trueDependence;
	}

	@Override
	public String getDependenceString() {
		return this.trueDependence ? "true" : "false";
	}

	@Override
	public String getDependenceTypeString() {
		return "Control Dependency";
	}

	private final boolean trueDependence;
}
