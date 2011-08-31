package jp.ac.osaka_u.ist.sdl.scorpio.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * クローンセットを表すクラス
 * 
 * @author higo
 * 
 */
public class CloneSetInfo implements Comparable<CloneSetInfo> {

	/**
	 * コンストラクタ
	 */
	public CloneSetInfo() {
		this.codeclones = new TreeSet<CodeCloneInfo>();
		this.id = number++;
	}

	/**
	 * コードクローンを追加する
	 * 
	 * @param codeclone
	 *            追加するコードクローン
	 * @return 追加した場合はtrue,　すでに含まれており追加しなかった場合はfalse
	 */
	public boolean add(final CodeCloneInfo codeclone) {
		return this.codeclones.add(codeclone);
	}

	/**
	 * コードクローン群を追加する
	 * 
	 * @param codeclones
	 *            追加するコードクローン群
	 */
	public void addAll(final Collection<CodeCloneInfo> codeclones) {

		for (final CodeCloneInfo codeFragment : codeclones) {
			this.add(codeFragment);
		}
	}

	/**
	 * クローンセットを構成するコードクローン群を返す
	 * 
	 * @return　クローンセットを構成するコードクローン群
	 */
	public SortedSet<CodeCloneInfo> getCodeClones() {
		return Collections.unmodifiableSortedSet(this.codeclones);
	}

	/**
	 * クローンセットのIDを返す
	 * 
	 * @return　クローンセットのID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * クローンセットに含まれるコードクローンの数を返す
	 * 
	 * @return　クローンセットに含まれるコードクローンの数
	 */
	public int getNumberOfCodeclones() {
		return this.codeclones.size();
	}

	/**
	 * クローンセットに含まれるギャップの数を返す
	 * 
	 * @return　クローンセットに含まれるギャップの数
	 */
	public int getGapsNumber() {

		int gap = 0;

		for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
			gap += codeFragment.getGapsNumber();
		}

		return gap;
	}

	/**
	 * クローンセットの長さ（含まれるコードクローンの大きさ）を返す
	 * 
	 * @return　クローンセットの長さ（含まれるコードクローンの大きさ）
	 */
	public int getLength() {
		int total = 0;
		for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
			total += codeFragment.length();
		}

		return total / this.getNumberOfCodeclones();
	}

	@Override
	public int compareTo(CloneSetInfo o) {

		final Iterator<CodeCloneInfo> thisIterator = this.getCodeClones()
				.iterator();
		final Iterator<CodeCloneInfo> targetIterator = o.getCodeClones()
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

	final private SortedSet<CodeCloneInfo> codeclones;

	final private int id;

	private static int number = 0;
}
