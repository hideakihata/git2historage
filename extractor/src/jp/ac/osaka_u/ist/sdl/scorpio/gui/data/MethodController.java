package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * GUIでメソッドを管理するためのクラス
 * 
 * @author higo
 */
public class MethodController {

	private MethodController() {
		this.methods = new TreeMap<Integer, MethodInfo>();
	}

	/**
	 * メソッドを追加する
	 * 
	 * @param method
	 *            追加するメソッド
	 */
	public void add(final MethodInfo method) {

		if (null == method) {
			throw new IllegalArgumentException();
		}

		this.methods.put(method.getID(), method);
	}

	/**
	 * IDで指定されたメソッドを返す
	 * 
	 * @param id
	 *            メソッドのID
	 * @return IDで指定されたメソッド
	 */
	public MethodInfo getMethod(final int id) {
		return this.methods.get(id);
	}

	/**
	 * メソッドのCollectionを返す
	 * 
	 * @return メソッドのCollection
	 */
	public Collection<MethodInfo> getMethods() {
		return Collections.unmodifiableCollection(this.methods.values());
	}

	/**
	 * メソッドの数を返す
	 * 
	 * @return　メソッドの数
	 */
	public int getNumberOfMethods() {
		return this.methods.size();
	}

	/**
	 * ファイルをすべて消す
	 */
	public void clear() {
		this.methods.clear();
	}

	private final SortedMap<Integer, MethodInfo> methods;

	/**
	 * 指定されたFileControllerを返す
	 * 
	 * @param id
	 * @return
	 */
	public static MethodController getInstance(final String id) {

		MethodController controller = map.get(id);
		if (null == controller) {
			controller = new MethodController();
			map.put(id, controller);
		}

		return controller;
	}

	private static final Map<String, MethodController> map = new HashMap<String, MethodController>();
}
