package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.Entity;

/**
 * GUIでクローンセットを表すクラス
 * 
 * @author higo
 * 
 */
public class CloneSetInfo implements Entity, Comparable<CloneSetInfo> {

	/**
	 * コンストラクタ
	 */
	public CloneSetInfo() {
		this.codeclones = new TreeSet<CodeCloneInfo>();
	}

	/**
	 * コードクローンを追加する
	 * 
	 * @param codeclone
	 *            追加されるコードクローン
	 */
	public void add(final CodeCloneInfo codeclone) {

		if (null == codeclone) {
			throw new IllegalArgumentException();
		}

		this.codeclones.add(codeclone);
	}

	/**
	 * このクローンセットに含まれるコードクローンのSortedSetを返す
	 * 
	 * @return　このクローンセットに含まれるコードクローンのSortedSet
	 */
	public SortedSet<CodeCloneInfo> getCodeclones() {
		return Collections.unmodifiableSortedSet(this.codeclones);
	}

	/**
	 * クローンセットに含まれるコードクローンの大きさ（の平均値）を返す
	 * 
	 * @return　クローンセットに含まれるコードクローンの大きさ（の平均値）
	 */
	public int getLength() {

		int totalLength = 0;
		for (final CodeCloneInfo codeclone : this.getCodeclones()) {
			totalLength += codeclone.getLength();
		}

		return totalLength / this.getNumberOfElements();
	}

	/**
	 * クローンセットの要素数（コードクローン数）を返す
	 * 
	 * @return　クローンセットの要素数（コードクローン数）
	 */
	public int getNumberOfElements() {
		return this.codeclones.size();
	}

	/**
	 * クローンセット内のギャップの数を返す
	 * 
	 * @return クローンセット内のギャップの数
	 */
	public int getNumberOfGapps() {

		int gap = 0;

		for (final CodeCloneInfo codeclone : this.getCodeclones()) {
			gap += codeclone.getNumberOfGapps();
		}

		return gap;
	}

	/**
	 * 先頭のコードクローンを返す
	 * 
	 * @return　先頭のコードクローン
	 */
	public CodeCloneInfo getFirstCodeclone() {
		return this.codeclones.first();
	}

	/**
	 * 比較関数
	 */
	@Override
	public int compareTo(CloneSetInfo o) {
		final Iterator<CodeCloneInfo> thisIterator = this.codeclones.iterator();
		final Iterator<CodeCloneInfo> targetIterator = o.codeclones.iterator();

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

	public static String CLONESET = new String("CLONESET");

	private final SortedSet<CodeCloneInfo> codeclones;
}
