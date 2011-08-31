package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.Entity;
import jp.ac.osaka_u.ist.sdl.scorpio.ScorpioGUI;

/**
 * GUIでコードクローンを表すクラス
 * 
 * @author higo
 * 
 */
public class CodeCloneInfo implements Entity, Comparable<CodeCloneInfo> {

	/**
	 * コンストラクタ
	 */
	public CodeCloneInfo() {
		this.elements = new TreeSet<ElementInfo>();
		this.calls = new TreeSet<MethodCallInfo>();
	}

	/**
	 * 要素を加える
	 * 
	 * @param element
	 *            加える要素
	 */
	public void add(final ElementInfo element) {

		if (null == element) {
			throw new IllegalArgumentException();
		}

		this.elements.add(element);
	}

	/**
	 * コードクローンをなす要素のSortedSetを返す
	 * 
	 * @return　コードクローンをなす要素のSortedSet
	 */
	public SortedSet<ElementInfo> getElements() {
		return Collections.unmodifiableSortedSet(this.elements);
	}

	/**
	 * 引数で指定されたメソッドに含まれる要素のSortedSetを返す
	 * 
	 * @param methodID
	 * @return
	 */
	public SortedSet<ElementInfo> getElements(final int methodID) {

		final SortedSet<ElementInfo> elements = new TreeSet<ElementInfo>();
		for (final ElementInfo element : this.getElements()) {
			if (element.getMethodID() == methodID) {
				elements.add(element);
			}
		}
		return elements;
	}

	public void add(final MethodCallInfo call) {
		this.calls.add(call);
	}

	public SortedSet<MethodCallInfo> getCalls() {
		return new TreeSet<MethodCallInfo>(this.calls);
	}

	/**
	 * コードクローンの大きさを返す
	 * 
	 * @return　コードクローンの大きさ
	 */
	public int getLength() {
		return this.elements.size();
	}

	/**
	 * コードクローンをなす先頭の要素を返す
	 * 
	 * @return コードクローンをなす先頭の要素
	 */
	public ElementInfo getFirstElement() {
		return this.elements.first();
	}

	/**
	 * コードクローンをなす最後の要素を返す
	 * 
	 * @return コードクローンをなす最後の要素
	 */
	public ElementInfo getLastElement() {
		return this.elements.last();
	}

	public void setID(final int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

	/**
	 * ギャップの数を設定する
	 * 
	 * @param gap
	 *            ギャップの数
	 */
	public void setNumberOfGapps(final int gap) {
		this.gap = gap;
	}

	/**
	 * ギャップの数を返す
	 * 
	 * @return　ギャップの数
	 */
	public int getNumberOfGapps() {
		return this.gap;
	}

	/**
	 * このコードクローンの要素を含むメソッドの数を設定する
	 * 
	 * @param method
	 */
	public void setNumberOfMethods(final int method) {
		this.method = method;
	}

	/**
	 * このコードクローンの要素を含むメソッドの数を返す
	 * 
	 * @param method
	 */
	public int getNumberOfMethods() {
		return this.method;
	}

	/**
	 * このクローンに含まれる要素を所有するメソッドの集合を返す
	 * 
	 * @return
	 */
	public SortedSet<MethodInfo> getOwnerMethods() {
		final SortedSet<MethodInfo> ownerMethods = new TreeSet<MethodInfo>();
		for (final ElementInfo element : this.elements) {
			final int methodID = element.getMethodID();
			final MethodInfo ownerMethod = MethodController.getInstance(
					ScorpioGUI.ID).getMethod(methodID);
			ownerMethods.add(ownerMethod);
		}
		return ownerMethods;
	}

	/**
	 * 比較関数
	 */
	@Override
	public int compareTo(CodeCloneInfo o) {

		final Iterator<ElementInfo> thisIterator = this.getElements()
				.iterator();
		final Iterator<ElementInfo> targetIterator = o.getElements().iterator();

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

	@Override
	public int hashCode() {
		long total = 0;
		for (final ElementInfo element : this.getElements()) {
			total += element.hashCode();
		}
		return (int) total / this.getLength();
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

		return this.elements.containsAll(target.elements)
				&& target.elements.containsAll(this.elements);
	}

	public static String CODECLONE = new String("CODECLONE");

	private final SortedSet<ElementInfo> elements;

	private final SortedSet<MethodCallInfo> calls;

	private int id;

	private int gap;

	private int method;
}
