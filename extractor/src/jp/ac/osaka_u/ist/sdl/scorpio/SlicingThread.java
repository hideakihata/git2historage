package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scorpio.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.data.NodePairInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.settings.Configuration;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

class SlicingThread implements Runnable {

	private final List<NodePairInfo> nodepairs;

	private final AtomicInteger index;

	private final List<ClonePairInfo> clonepairList;

	SlicingThread(final List<NodePairInfo> nodepairs,
			final AtomicInteger index, final List<ClonePairInfo> clonepairList) {

		this.nodepairs = nodepairs;
		this.index = index;
		this.clonepairList = clonepairList;
	}

	@Override
	public void run() {

		while (true) {

			int i = this.index.getAndIncrement();
			if (!(i < this.nodepairs.size())) {
				break;
			}

			final NodePairInfo nodepair = this.nodepairs.get(i);

			final PDGNode<?> nodeA = nodepair.nodeA;
			final PDGNode<?> nodeB = nodepair.nodeB;

			final Slicing slicing;
			switch (Configuration.INSTANCE.getP()) {
			case INTRA:
				slicing = new MethodSlicing(nodeA, nodeB);
				break;
			case INTER:
				slicing = new SystemSlicing(nodeA, nodeB);
				break;
			default:
				throw new IllegalStateException();
			}
			final ClonePairInfo clonepair = slicing.perform();

			if (Configuration.INSTANCE.getS() <= clonepair.length()) {
				this.clonepairList.add(clonepair);
			}

			increaseNumberOfPairs();
		}
	}

	synchronized static void increaseNumberOfPairs() {
		numberOfPairs++;
	}

	synchronized static void increaseNumberOfComparison() {
		numberOfComparion++;
	}

	static int numberOfPairs = 0;

	static long numberOfComparion = 0;
}
