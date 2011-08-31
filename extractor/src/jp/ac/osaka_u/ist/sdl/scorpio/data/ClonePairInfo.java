package jp.ac.osaka_u.ist.sdl.scorpio.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * クローンペアを表すクラス
 * 
 * @author higo
 * 
 */
public class ClonePairInfo implements Cloneable, Comparable<ClonePairInfo> {

	/**
	 * コンストラクタ
	 */
	public ClonePairInfo() {
		this.realNodepairs = new TreeSet<NodePairInfo>();
		this.allNodepairs = new TreeSet<NodePairInfo>();
		this.nodes = new HashSet<PDGNode<?>>();
		this.callsA = new TreeSet<CallInfo<?>>();
		this.callsB = new TreeSet<CallInfo<?>>();
		this.id = ID_GENERATOR.getAndIncrement();
	}

	public ClonePairInfo(final NodePairInfo nodepair) {

		this();

		this.add(nodepair);
	}

	public void add(final NodePairInfo nodepair) {
		if (this.nodes.contains(nodepair.nodeA)
				|| this.nodes.contains(nodepair.nodeB)) {
			return;
		}

		final ExecutableElementInfo coreA = nodepair.nodeA.getCore();
		final ExecutableElementInfo coreB = nodepair.nodeB.getCore();

		// コアの開始位置と終了位置が同じ場合はrealnodepairには追加しない
		if ((coreA.getFromLine() == coreA.getToLine() && coreA.getFromColumn() == coreA
				.getToColumn())
				&& (coreB.getFromLine() == coreB.getToLine() && coreB
						.getFromColumn() == coreB.getToColumn())) {
		} 
		
		// データノードである場合も追加しない
		else if (nodepair.nodeA instanceof PDGDataNode<?>
				|| nodepair.nodeB instanceof PDGDataNode<?>) {
		} else {
			this.realNodepairs.add(nodepair);
		}

		this.allNodepairs.add(nodepair);
		this.nodes.add(nodepair.nodeA);
		this.nodes.add(nodepair.nodeB);
	}

	public void addAll(final Collection<NodePairInfo> nodepairs) {
		for (final NodePairInfo nodepair : nodepairs) {
			this.add(nodepair);
		}
	}

	public void add(final ClonePairInfo clonepair) {
		this.addAll(clonepair.getAllNodePairs());
		this.addCallsA(clonepair.getCallsA());
		this.addCallsB(clonepair.getCallsB());
	}

	public SortedSet<NodePairInfo> getAllNodePairs() {
		return new TreeSet<NodePairInfo>(this.allNodepairs);
	}

	public SortedSet<NodePairInfo> getRealNodePairs() {
		return new TreeSet<NodePairInfo>(this.realNodepairs);
	}

	/**
	 *　クローンペアの長さを返す
	 * 
	 * @return　クローンペアの長さ
	 */
	public int length() {
		return this.realNodepairs.size();
	}

	/**
	 * 引数で与えられたクローンペアに，このクローンペアが包含されているか判定する
	 * 
	 * @param clonePair
	 *            対象クローンペア
	 * @return 包含される場合はtrue, そうでない場合はfalse
	 */
	public boolean subsumedBy(final ClonePairInfo clonepair) {

		if (this.length() <= clonepair.length()
				&& this.realNodepairs.containsAll(clonepair.realNodepairs)) {
			return true;
		} else {
			return false;
		}
	}

	public void addCallA(final CallInfo<?> callA) {
		this.callsA.add(callA);
	}

	public void addCallB(final CallInfo<?> callB) {
		this.callsB.add(callB);
	}

	public void addCallsA(final Collection<CallInfo<?>> callsA) {
		this.callsA.addAll(callsA);
	}

	public void addCallsB(final Collection<CallInfo<?>> callsB) {
		this.callsB.addAll(callsB);
	}

	public SortedSet<CallInfo<?>> getCallsA() {
		return new TreeSet<CallInfo<?>>(this.callsA);
	}

	public SortedSet<CallInfo<?>> getCallsB() {
		return new TreeSet<CallInfo<?>>(this.callsB);
	}

	@Override
	public int hashCode() {
		return (int) (this.id % Integer.MAX_VALUE);
	}

	public CodeCloneInfo getCodeCloneA() {
		final CodeCloneInfo codeclone = new CodeCloneInfo();
		for (final NodePairInfo nodepair : this.allNodepairs) {
			codeclone.add(nodepair.nodeA);
		}
		codeclone.addCalls(this.getCallsA());
		return codeclone;
	}

	public CodeCloneInfo getCodeCloneB() {
		final CodeCloneInfo codeclone = new CodeCloneInfo();
		for (final NodePairInfo nodepair : this.allNodepairs) {
			codeclone.add(nodepair.nodeB);
		}
		codeclone.addCalls(this.getCallsB());
		return codeclone;
	}

	@Override
	public boolean equals(Object o) {

		if (null == o) {
			return false;
		}

		final ClonePairInfo clonepair = (ClonePairInfo) o;
		return this.realNodepairs.containsAll(clonepair.realNodepairs)
				&& clonepair.realNodepairs.containsAll(this.realNodepairs);
	}

	@Override
	public ClonePairInfo clone() {

		final ClonePairInfo clonepair = new ClonePairInfo();
		clonepair.add(this);

		return clonepair;
	}

	@Override
	public int compareTo(final ClonePairInfo clonepair) {

		if (null == clonepair) {
			throw new IllegalArgumentException();
		}

		final Iterator<NodePairInfo> thisIterator = this.realNodepairs
				.iterator();
		final Iterator<NodePairInfo> targetIterator = clonepair.realNodepairs
				.iterator();
		while (thisIterator.hasNext() && targetIterator.hasNext()) {
			int order = thisIterator.next().compareTo(targetIterator.next());
			if (0 != order) {
				return order;
			}
		}

		if (thisIterator.hasNext() && !targetIterator.hasNext()) {
			return -1;
		} else if (!thisIterator.hasNext() && targetIterator.hasNext()) {
			return 1;
		} else {
			return 0;
		}
	}

	final private SortedSet<NodePairInfo> realNodepairs;

	final private SortedSet<NodePairInfo> allNodepairs;

	final private Set<PDGNode<?>> nodes;

	final private SortedSet<CallInfo<?>> callsA;

	final private SortedSet<CallInfo<?>> callsB;

	final public long id;

	private static AtomicLong ID_GENERATOR = new AtomicLong();
}
