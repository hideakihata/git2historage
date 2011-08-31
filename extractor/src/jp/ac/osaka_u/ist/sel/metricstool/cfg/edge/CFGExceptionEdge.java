package jp.ac.osaka_u.ist.sel.metricstool.cfg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;

public class CFGExceptionEdge extends CFGEdge {

	public CFGExceptionEdge(CFGNode<?> fromNode, final CFGNode<?> toNode,
			final TypeInfo thrownException) {
		super(fromNode, toNode);
		this.thrownException = thrownException;
	}

	public TypeInfo getThrownException() {
		return this.thrownException;
	}

	@Override
	public String getDependenceTypeString() {
		return "exception";
	}

	@Override
	public String getDependenceString() {
		return this.getThrownException().getTypeName();
	}

	@Override
	public CFGEdge replaceFromNode(final CFGNode<?> newFromNode) {
		final CFGNode<?> toNode = this.getToNode();
		final TypeInfo thrownException = this.getThrownException();
		return new CFGExceptionEdge(newFromNode, toNode, thrownException);
	}

	@Override
	public CFGEdge replaceToNode(final CFGNode<?> newToNode) {
		final CFGNode<?> fromNode = this.getFromNode();
		final TypeInfo thrownException = this.getThrownException();
		return new CFGExceptionEdge(fromNode, newToNode, thrownException);
	}

	@Override
	public CFGEdge replaceBothNodes(final CFGNode<?> newFromNode,
			final CFGNode<?> newToNode) {
		final TypeInfo thrownException = this.getThrownException();
		return new CFGExceptionEdge(newFromNode, newToNode, thrownException);
	}

	private final TypeInfo thrownException;
}
