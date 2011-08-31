package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.DETECTION_TYPE;

/**
 * GUIでクローンセットを管理するためのクラス
 * 
 * @author higo
 * 
 */
public class CodeCloneController {

	private CodeCloneController() {
		this.clonesets = new TreeSet<CloneSetInfo>();
	}

	public void setDetectionType(final DETECTION_TYPE detectionType) {
		this.detectionType = detectionType;
	}

	public DETECTION_TYPE getDetectionType() {
		return this.detectionType;
	}

	/**
	 * クローンセットを追加する
	 * 
	 * @param cloneset
	 *            追加するクローンセット
	 */
	public void add(final CloneSetInfo cloneset) {

		if (null == cloneset) {
			throw new IllegalArgumentException();
		}

		this.clonesets.add(cloneset);
	}

	/**
	 * クローンセットのSortedSetを返す
	 * 
	 * @return クローンセットのSortedSet
	 */
	public SortedSet<CloneSetInfo> getCloneSets() {
		return Collections.unmodifiableSortedSet(this.clonesets);
	}

	/**
	 * クローンセットの数を返す
	 * 
	 * @return　クローンセットの数
	 */
	public int getNumberOfClonesets() {
		return this.clonesets.size();
	}

	/**
	 * クローンセットをすべて消す
	 */
	public void clear() {
		this.clonesets.clear();
	}

	private SortedSet<CloneSetInfo> clonesets;

	/**
	 * 指定されたCodeCloneControllerを返す
	 * 
	 * @param id
	 * @return
	 */
	public static CodeCloneController getInstance(final String id) {

		CodeCloneController controller = map.get(id);
		if (null == controller) {
			controller = new CodeCloneController();
			map.put(id, controller);
		}

		return controller;
	}

	private static final Map<String, CodeCloneController> map = new HashMap<String, CodeCloneController>();

	private DETECTION_TYPE detectionType;
}
