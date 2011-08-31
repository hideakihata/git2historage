package jp.ac.osaka_u.ist.sdl.scorpio.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.PDGMergedNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * コードクローンを表すクラス
 * 
 * @author higo
 * 
 */
public class CodeCloneInfo implements Comparable<CodeCloneInfo> {

	final private SortedSet<PDGNode<?>> allElements;
	final private SortedSet<PDGNode<?>> realElements;

	final private SortedSet<CallInfo<?>> calls;

	private int hash;

	/**
	 * コンストラクタ
	 */
	public CodeCloneInfo() {
		this.allElements = new TreeSet<PDGNode<?>>();
		this.realElements = new TreeSet<PDGNode<?>>();
		this.calls = new TreeSet<CallInfo<?>>();
		this.hash = 0;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param element
	 *            初期要素
	 */
	public CodeCloneInfo(final PDGNode<?> node) {
		this();
		this.add(node);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param allElements
	 *            初期要素群
	 */
	public CodeCloneInfo(final Collection<PDGNode<?>> nodes) {
		this();
		this.addAll(nodes);
	}

	public void addElements(final CodeCloneInfo codeclone) {
		this.allElements.addAll(codeclone.getAllElements());
		this.realElements.addAll(codeclone.getRealElements());
	}

	public void add(final PDGNode<?> node) {

		if (null == node) {
			return;
		}

		// メソッドエンターノードは追加しない
		else if (node instanceof PDGMethodEnterNode) {
			return;
		}

		// データノード（パラメータやフィールドパッシングのための）は追加しない
		else if (node instanceof PDGDataNode<?>) {
		}

		// 集約ノードのは，全てのコアを追加
		else if (node instanceof PDGMergedNode) {
			final PDGMergedNode mergedNode = (PDGMergedNode) node;
			for (final PDGNode<?> originalNode : mergedNode.getOriginalNodes()) {
				this.add(originalNode);
			}
		}

		// それ以外の時はコアを追加
		else {

			final ExecutableElementInfo core = node.getCore();
			if (core.getFromLine() != core.getToLine()
					|| core.getFromColumn() != core.getToColumn()) {
				boolean a = this.realElements.add(node);
				// if(!a){
				// System.out.println("aaa");
				// }
			}
			this.allElements.add(node);
		}
	}

	public void addAll(final Collection<PDGNode<?>> nodes) {
		for (final PDGNode<?> node : nodes) {
			this.add(node);
		}
	}

	public void remove(final PDGNode<?> node) {
		this.allElements.remove(node);
		this.realElements.remove(node);
		this.hash = 0;
	}

	public void removeAll(final Collection<PDGNode<?>> nodes) {
		for (final PDGNode<?> node : nodes) {
			this.remove(node);
		}
	}

	public void addCall(final CallInfo<?> call) {
		this.calls.add(call);
	}

	public void addCalls(final Collection<CallInfo<?>> calls) {
		this.calls.addAll(calls);
	}

	public SortedSet<CallInfo<?>> getCalls() {
		return new TreeSet<CallInfo<?>>(this.calls);
	}

	/**
	 * 引数で与えられた要素が，このコードクローンに含まれるか判定する
	 * 
	 * @param element
	 *            対象要素
	 * @return　含まれる場合はtrue,　そうでない場合はfalse
	 */
	public boolean contain(final ExecutableElementInfo element) {
		return this.allElements.contains(element);
	}

	public FileInfo getOwnerFile() {
		return ((TargetClassInfo) this.realElements.first().getCore()
				.getOwnerMethod().getOwnerClass()).getOwnerFile();
	}

	/**
	 * コードクローンの長さを返す
	 * 
	 * @return　コードクローンの長さ
	 */
	public int length() {
		return this.realElements.size();
	}

	/**
	 * 引数で与えられたコードクローンに，このコードクローンが包含されているか判定する
	 * 
	 * @param codeclone
	 *            対象コードクローン
	 * @return 包含される場合はtrue, そうでない場合はfalse
	 */
	public boolean subsumedBy(final CodeCloneInfo codeclone) {

		// this.の方が大きいので包含されているはずがない
		if (codeclone.length() <= this.length()) {
			return false;
		}

		return codeclone.realElements.containsAll(this.realElements);
	}

	@Override
	public boolean equals(Object o) {

		if (null == o) {
			return false;
		}

		if (!(o instanceof CodeCloneInfo)) {
			return false;
		}

		final CodeCloneInfo target = (CodeCloneInfo) o;
		if (target.length() != this.length()) {
			return false;
		}

		if (!this.realElements.containsAll(target.realElements)) {
			return false;
		}

		if (!target.realElements.containsAll(this.realElements)) {
			return false;
		}

		return true;
	}

	/**
	 * コードクローンを構成する要素群を返す
	 * 
	 * @return　コードクローンを構成する要素群
	 */
	@Deprecated
	public SortedSet<PDGNode<?>> getElements() {
		return this.getRealElements();
	}

	public SortedSet<PDGNode<?>> getAllElements() {
		// final SortedSet<ExecutableElementInfo> elements = new
		// TreeSet<ExecutableElementInfo>();
		// elements.addAll(this.allElements);
		// return elements;
		return Collections.unmodifiableSortedSet(this.allElements);
	}

	public SortedSet<PDGNode<?>> getRealElements() {
		// final SortedSet<ExecutableElementInfo> elements = new
		// TreeSet<ExecutableElementInfo>();
		// elements.addAll(this.realElements);
		// return elements;
		return Collections.unmodifiableSortedSet(this.realElements);
	}

	/**
	 * コードクローンを構成する要素を含むメソッド一覧を返す
	 * 
	 * @return
	 */
	public SortedSet<CallableUnitInfo> getOwnerCallableUnits() {
		final SortedSet<CallableUnitInfo> methods = new TreeSet<CallableUnitInfo>();
		for (final PDGNode<?> node : this.getElements()) {
			methods.add(node.getCore().getOwnerMethod());
		}
		return methods;
	}

	@Override
	public int hashCode() {

		if (0 != this.hash) {
			return this.hash;
		}

		long hash = 0;
		for (final PDGNode<?> node : this.realElements) {
			hash += node.hashCode();
		}

		if (Integer.MIN_VALUE < hash && hash < Integer.MAX_VALUE) {
			this.hash = (int) hash;
		} else {
			this.hash = (int) (hash % Integer.MAX_VALUE);
		}

		return this.hash;
	}

	public boolean contains(final PDGNode<?> node) {
		return this.allElements.contains(node);
	}

	public float density() {
		final Set<Integer> duplication = new HashSet<Integer>();
		for (final PDGNode<?> node : this.getElements()) {
			for (int line = node.getCore().getFromLine(); line <= node
					.getCore().getToColumn(); line++) {
				duplication.add(new Integer(line));
			}
		}
		return (float) duplication.size()
				/ (float) (this.getElements().last().getCore().getToLine()
						- this.getElements().first().getCore().getFromLine() + 1);
	}

	@Override
	public int compareTo(final CodeCloneInfo o) {

		// 必要な処理，消さないこと！
		if (this.equals(o)) {
			return 0;
		}

		final Iterator<PDGNode<?>> thisIterator = this.getRealElements()
				.iterator();
		final Iterator<PDGNode<?>> targetIterator = o.getRealElements()
				.iterator();

		// 両方の要素がある限り
		while (thisIterator.hasNext() && targetIterator.hasNext()) {

			final int elementOrder = thisIterator.next().compareTo(
					targetIterator.next());
			if (0 != elementOrder) {
				return elementOrder;
			}
		}

		if (!thisIterator.hasNext() && !targetIterator.hasNext()) {
			return 0;
		}

		if (!thisIterator.hasNext()) {
			return -1;
		}

		if (!targetIterator.hasNext()) {
			return 1;
		}

		assert false : "Here shouldn't be reached!";
		return 0;
	}

	/**
	 * このコードクローン内に存在するギャップの数を返す
	 * 
	 * @return このコードクローン内に存在するギャップの数
	 */
	public int getGapsNumber() {
		int gap = 0;

		final CallableUnitInfo ownerMethod = this.getRealElements().first()
				.getCore().getOwnerMethod();
		final SortedSet<ExecutableElementInfo> elements = getAllExecutableElements(ownerMethod);
		final ExecutableElementInfo[] elementArray = elements
				.toArray(new ExecutableElementInfo[0]);

		final SortedSet<PDGNode<?>> clonedElements = new TreeSet<PDGNode<?>>(
				new Comparator<PDGNode<?>>() {
					@Override
					public int compare(final PDGNode<?> node1,
							final PDGNode<?> node2) {
						return node1.getCore().compareTo(node2.getCore());
					}
				});
		clonedElements.addAll(this.getRealElements());
		final PDGNode<?>[] clonedElementArray = clonedElements
				.toArray(new PDGNode<?>[0]);
		CLONE: for (int i = 0; i < clonedElementArray.length - 1; i++) {
			for (int j = 0; j < elementArray.length - 1; j++) {

				// i番目とj番目の要素が等しいかをチェック
				if (equals(clonedElementArray[i].getCore(), elementArray[j])) {

					// i+1番目とj+1番目が等しくないのであれば，ギャップあり
					if (!equals(clonedElementArray[i + 1].getCore(),
							elementArray[j + 1])) {
						gap++;
					}

					continue CLONE;
				}
			}
		}

		return gap;
	}

	private static SortedSet<ExecutableElementInfo> getAllExecutableElements(
			final LocalSpaceInfo localSpace) {

		if (null == localSpace) {
			throw new IllegalArgumentException("localSpace is null.");
		}

		if (localSpace instanceof ExternalMethodInfo
				|| localSpace instanceof ExternalConstructorInfo) {
			throw new IllegalArgumentException(
					"localSpace is an external local space.");
		}

		final SortedSet<ExecutableElementInfo> allElements = new TreeSet<ExecutableElementInfo>();
		for (final StatementInfo innerStatement : localSpace.getStatements()) {

			// 単文の場合
			if (innerStatement instanceof SingleStatementInfo
					|| innerStatement instanceof CaseEntryInfo
					|| innerStatement instanceof LabelInfo) {
				allElements.add(innerStatement);
			}

			// ブロック文の場合
			else if (innerStatement instanceof BlockInfo) {

				if (innerStatement instanceof ConditionalBlockInfo) {
					final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
							.getConditionalClause().getCondition();
					allElements.add(condition);

					// for文だった場合
					if (innerStatement instanceof ForBlockInfo) {

						final ForBlockInfo innerForBlock = (ForBlockInfo) innerStatement;
						allElements.addAll(innerForBlock
								.getInitializerExpressions());
						allElements.addAll(innerForBlock
								.getIteratorExpressions());
					}

					// if文だった場合
					else if (innerStatement instanceof IfBlockInfo) {

						final IfBlockInfo innerIfBlock = (IfBlockInfo) innerStatement;
						if (innerIfBlock.hasElseBlock()) {
							allElements
									.addAll(getAllExecutableElements(innerIfBlock
											.getSequentElseBlock()));
						}
					}

					else if (innerStatement instanceof TryBlockInfo) {

						final TryBlockInfo innerTryBlock = (TryBlockInfo) innerStatement;
						for (final CatchBlockInfo catchBlock : innerTryBlock
								.getSequentCatchBlocks()) {
							allElements
									.addAll(getAllExecutableElements(catchBlock));
						}

						if (innerTryBlock.hasFinallyBlock()) {
							allElements
									.addAll(getAllExecutableElements(innerTryBlock
											.getSequentFinallyBlock()));
						}
					}

				} else if (innerStatement instanceof SynchronizedBlockInfo) {

					allElements.add(((SynchronizedBlockInfo) innerStatement)
							.getSynchronizedExpression());
				}

				allElements
						.addAll(getAllExecutableElements((BlockInfo) innerStatement));
			}

			else {
				assert false : "Here shouldn't be reached!";
			}
		}

		return allElements;
	}

	private static boolean equals(final ExecutableElementInfo element1,
			final ExecutableElementInfo element2) {

		return (element1.getFromLine() == element2.getFromLine())
				&& (element1.getFromColumn() == element2.getFromColumn())
				&& (element1.getToLine() == element2.getToLine())
				&& (element1.getToColumn() == element2.getToColumn());
	}
}
