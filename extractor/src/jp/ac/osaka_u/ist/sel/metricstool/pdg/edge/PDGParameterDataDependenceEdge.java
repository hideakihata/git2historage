package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class PDGParameterDataDependenceEdge extends PDGAcrossDataDependenceEdge {

	public PDGParameterDataDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode, final VariableInfo<?> variable,
			final CallInfo<?> call) {
		super(fromNode, toNode, variable, call);
	}
}
