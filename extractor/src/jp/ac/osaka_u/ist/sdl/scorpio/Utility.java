package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class Utility {

	public static Set<PDGNode<?>> getEquivalenceNodesFollowedWithExecutionDependency(
			final PDGNode<?> node) {

		final Set<PDGNode<?>> equivalenceNodes = new HashSet<PDGNode<?>>();
		equivalenceNodes.add(node);
		final int hash = Conversion.getNormalizedElement(node.getCore())
				.hashCode();
		for (final PDGEdge edge : node.getForwardEdges()) {
			if (edge instanceof PDGExecutionDependenceEdge) {
				final PDGNode<?> toNode = edge.getToNode();
				final int toHash = Conversion.getNormalizedElement(
						toNode.getCore()).hashCode();
				if (hash == toHash) {
					equivalenceNodes
							.addAll(getEquivalenceNodesFollowedWithExecutionDependency(toNode));
				}
			}
		}

		return equivalenceNodes;
	}
}
