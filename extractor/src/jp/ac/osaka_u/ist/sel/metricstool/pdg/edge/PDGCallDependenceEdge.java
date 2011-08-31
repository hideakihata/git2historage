package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class PDGCallDependenceEdge extends PDGEdge {

	public PDGCallDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode, final CallInfo<?> call) {
		super(PDG_EDGE_TYPE.CALL, fromNode, toNode);
		this.call = call;
	}

	public CallInfo<? extends CallableUnitInfo> getCallInfo() {
		return this.call;
	}

	@Override
	public String getDependenceString() {
		return this.getCallInfo().getText();
	}

	@Override
	public String getDependenceTypeString() {
		return "Control Dependency";
	}

	private final CallInfo<? extends CallableUnitInfo> call;
}
