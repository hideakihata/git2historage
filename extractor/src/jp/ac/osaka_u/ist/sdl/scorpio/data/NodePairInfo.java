package jp.ac.osaka_u.ist.sdl.scorpio.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class NodePairInfo implements Comparable<NodePairInfo> {

	private static final AtomicLong ID_GENERATOR = new AtomicLong();

	public final PDGNode<?> nodeA;
	public final PDGNode<?> nodeB;
	private final long id;

	private NodePairInfo(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.id = ID_GENERATOR.getAndIncrement();
	}

	@Override
	public int compareTo(NodePairInfo o) {
		if (this.id < o.id) {
			return -1;
		} else if (this.id > o.id) {
			return 1;
		} else {
			return 0;
		}
	}

	public static NodePairInfo getInstance(final PDGNode<?> nodeA,
			final PDGNode<?> nodeB) {

//		if (nodeA.id > nodeB.id) {
//			return getInstance(nodeB, nodeA);
//		}

		Map<Long, NodePairInfo> map = INSTANCE_MAP.get(nodeA.id);
		if (null == map) {
			map = new HashMap<Long, NodePairInfo>();
			INSTANCE_MAP.put(nodeA.id, map);
		}

		NodePairInfo pair = map.get(nodeB.id);
		if (null == pair) {
			pair = new NodePairInfo(nodeA, nodeB);
			map.put(nodeB.id, pair);
		}

		return pair;
	}

	private static final Map<Long, Map<Long, NodePairInfo>> INSTANCE_MAP = new HashMap<Long, Map<Long, NodePairInfo>>();
}
