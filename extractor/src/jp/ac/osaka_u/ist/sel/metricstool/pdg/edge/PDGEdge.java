package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import java.util.concurrent.atomic.AtomicLong;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public abstract class PDGEdge implements Comparable<PDGEdge> {

	private final PDGNode<?> fromNode;

	private final PDGNode<?> toNode;

	public final PDG_EDGE_TYPE type;

	public final long id;

	private static final AtomicLong MAKE_ID = new AtomicLong(0);

	public PDGEdge(final PDG_EDGE_TYPE type, final PDGNode<?> fromNode,
			final PDGNode<?> toNode) {
		if (null == fromNode || null == toNode) {
			throw new IllegalArgumentException();
		}

		this.type = type;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.id = MAKE_ID.getAndIncrement();
	}

	public abstract String getDependenceString();

	public abstract String getDependenceTypeString();

	public final PDGNode<?> getFromNode() {
		return this.fromNode;
	}

	public final PDGNode<?> getToNode() {
		return this.toNode;
	}

	public void vanish() {
		this.fromNode.removeForwardEdge(this);
		this.toNode.removeBackwardEdge(this);
	}

	@Override
	public boolean equals(Object arg) {

		if (null == arg) {
			return false;
		}

		if (!(arg instanceof PDGEdge)) {
			return false;
		}

		PDGEdge edge = (PDGEdge) arg;
		return this.id == edge.id;
	}

	@Override
	public int hashCode() {
		if ((Integer.MIN_VALUE < this.id) && (this.id < Integer.MAX_VALUE)) {
			return (int) this.id;
		} else {
			return (int) (this.id % Integer.MAX_VALUE);
		}
	}

	@Override
	public int compareTo(final PDGEdge edge) {

		if (null == edge) {
			throw new IllegalArgumentException();
		}

		if (this.id < edge.id) {
			return 1;
		} else if (this.id > edge.id) {
			return -1;
		} else {
			return 0;
		}
	}
}
