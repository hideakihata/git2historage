package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import jp.ac.osaka_u.ist.sdl.scorpio.Entity;

public class MethodInfo implements Entity, Comparable<MethodInfo> {

	public MethodInfo(final String name, final int id, final int fileID,
			final int fromLine, final int toLine) {
		this.name = name;
		this.id = id;
		this.fileID = fileID;
		this.fromLine = fromLine;
		this.toLine = toLine;
		this.codeclone = null;
	}

	public MethodInfo() {
		this(null, 0, 0, 0, 0);
	}

	/**
	 * メソッド名を返す
	 * 
	 * @return メソッド名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * メソッド名を設定する
	 * 
	 * @param name
	 *            設定するメソッド名
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * メソッドのIDを返す
	 * 
	 * @return　メソッドのID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * メソッドのIDを設定する
	 * 
	 * @param id
	 *            設定するID
	 */
	public void setID(final int id) {
		this.id = id;
	}

	/**
	 * ファイルのIDを返す
	 * 
	 * @return　ファイルのID
	 */
	public int getFileID() {
		return this.fileID;
	}

	/**
	 * ファイルのIDを設定する
	 * 
	 * @param id
	 *            設定するファイルID
	 */
	public void setFileID(final int fileID) {
		this.fileID = fileID;
	}

	/**
	 * メソッドの開始行を返す
	 * 
	 * @return　メソッドの開始行
	 */
	public int getFromLine() {
		return this.fromLine;
	}

	/**
	 * メソッドの開始行を設定する
	 * 
	 * @param fromLine
	 *            設定する行数
	 */
	public void setFromLine(final int fromLine) {
		this.fromLine = fromLine;
	}

	/**
	 * メソッドの開始行を返す
	 * 
	 * @return　メソッドの開始行
	 */
	public int getToLine() {
		return this.toLine;
	}

	/**
	 * メソッドの開始行を設定する
	 * 
	 * @param fromLine
	 *            設定する行数
	 */
	public void setToLine(final int toLine) {
		this.toLine = toLine;
	}

	@Override
	public int compareTo(MethodInfo o) {
		if (this.id < o.id) {
			return -1;
		} else if (this.id > o.id) {
			return 1;
		} else {
			return 0;
		}
	}

	public void setCodeClone(final CodeCloneInfo codeclone) {
		this.codeclone = codeclone;
	}

	public void removeCodeClone() {
		this.codeclone = null;
	}

	public CodeCloneInfo getCodeClone() {
		return this.codeclone;
	}

	@Override
	public String toString() {
		final StringBuilder label = new StringBuilder();
		label.append(this.name);
		label.append("()");
		// label.append("#");
		// final FileInfo file = FileController.getInstance(ScorpioGUI.ID)
		// .getFile(this.fileID);
		// label.append(file.getName());
		return label.toString();
	}

	public static String METHOD = new String("METHOD");

	private String name;
	private int id;
	private int fileID;
	private int fromLine;
	private int toLine;

	private CodeCloneInfo codeclone;
}
