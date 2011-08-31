package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * データ依存辺を表すクラス
 * 
 * @author higo
 * 
 */
public class PDGDataDependenceEdge extends PDGEdge {

	/**
	 * エッジの集合から，データ依存を表すエッジのみを抽出し，そのSetを返す
	 * 
	 * @param edges
	 * @return
	 */
	public static SortedSet<PDGDataDependenceEdge> extractDataDependenceEdge(
			final Set<PDGEdge> edges) {
		final SortedSet<PDGDataDependenceEdge> dataDependenceEdges = new TreeSet<PDGDataDependenceEdge>();
		for (final PDGEdge edge : edges) {
			if (PDG_EDGE_TYPE.DATA == edge.type) {
				dataDependenceEdges.add((PDGDataDependenceEdge) edge);
			}
		}
		return dataDependenceEdges;
	}

	public PDGDataDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode, final VariableInfo<?> data) {
		super(PDG_EDGE_TYPE.DATA, fromNode, toNode);

		this.data = data;
	}

	public VariableInfo<?> getVariable() {
		return this.data;
	}

	@Override
	public String getDependenceString() {
		return this.data.getName();
	}

	@Override
	public String getDependenceTypeString() {
		return "Data Dependency";
	}

	@Override
	public boolean equals(Object arg) {
		if (this.getClass().equals(arg.getClass())) {

			if (!super.equals(arg)) {
				return false;
			}

			final PDGDataDependenceEdge edge = (PDGDataDependenceEdge) arg;
			return this.getVariable().equals(edge.getVariable());

		} else {
			return false;
		}
	}

	@Override
	public int compareTo(final PDGEdge edge) {

		if (null == edge) {
			throw new IllegalArgumentException();
		}

		if (!(edge instanceof PDGDataDependenceEdge)) {
			return super.compareTo(edge);
		}

		int order = super.compareTo(edge);
		if (0 != order) {
			return order;
		}

		return this.getVariable().compareTo(
				((PDGDataDependenceEdge) edge).getVariable());
	}

	private final VariableInfo<?> data;
}
