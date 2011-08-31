package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

/**
 * GUIでコードクローンを構成する要素を表すクラス
 * 
 * @author higo
 * 
 */
public class ElementInfo implements Comparable<ElementInfo> {

	/**
	 * コンストラクタ
	 * 
	 * @param fileID
	 *            ファイルID
	 * @param fromLine
	 *            開始行
	 * @param fromColumn
	 *            開始列
	 * @param toLine
	 *            終了行
	 * @param toColumn
	 *            終了列
	 */
	public ElementInfo(final int fileID, final int methodID,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn) {

		this.fileID = fileID;
		this.methodID = methodID;
		this.fromLine = fromLine;
		this.fromColumn = fromColumn;
		this.toLine = toLine;
		this.toColumn = toColumn;
	}

	public ElementInfo() {
	}

	/**
	 * ファイルのIDを返す
	 * 
	 * @return ファイルのID
	 */
	public int getFileID() {
		return this.fileID;
	}

	public void setFileID(final int fileID) {
		this.fileID = fileID;
	}

	/**
	 * メソッドのIDを返す
	 * 
	 * @return ファイルのID
	 */
	public int getMethodID() {
		return this.methodID;
	}

	public void setMethodID(final int methodID) {
		this.methodID = methodID;
	}

	/**
	 * 開始行を返す
	 * 
	 * @return　開始行
	 */
	public int getFromLine() {
		return this.fromLine;
	}

	/**
	 * 開始行を設定する
	 * 
	 * @param fromLine
	 *            　設定する開始行
	 */
	public void setFromLine(final int fromLine) {
		this.fromLine = fromLine;
	}

	/**
	 * 開始列を返す
	 * 
	 * @return　開始列
	 */
	public int getFromColumn() {
		return this.fromColumn;
	}

	/**
	 * 開始列を設定する
	 * 
	 * @param fromColumn
	 *            　設定する開始列
	 */
	public void setFromColumn(final int fromColumn) {
		this.fromColumn = fromColumn;
	}

	/**
	 * 終了行を返す
	 * 
	 * @return　終了行
	 */
	public int getToLine() {
		return this.toLine;
	}

	/**
	 * 終了行を設定する
	 * 
	 * @param toLine
	 *            　設定する終了行
	 */
	public void setToLine(final int toLine) {
		this.toLine = toLine;
	}

	/**
	 * 終了列を返す
	 * 
	 * @return　終了列
	 */
	public int getToColumn() {
		return this.toColumn;
	}

	/**
	 * 終了列を設定する
	 * 
	 * @param toColumn
	 *            　設定する終了列
	 */
	public void setToColumn(final int toColumn) {
		this.toColumn = toColumn;
	}

	/**
	 * 比較関数
	 */
	@Override
	public int compareTo(ElementInfo o) {

		if (this.getFileID() < o.getFileID()) {
			return -1;
		} else if (this.getFileID() > o.getFileID()) {
			return 1;
		} else if (this.getFromLine() < o.getFromLine()) {
			return -1;
		} else if (this.getFromLine() > o.getFromLine()) {
			return 1;
		} else if (this.getFromColumn() < o.getFromColumn()) {
			return -1;
		} else if (this.getFromColumn() > o.getFromColumn()) {
			return 1;
		} else if (this.getToLine() < o.getToLine()) {
			return -1;
		} else if (this.getToLine() > o.getToLine()) {
			return 1;
		} else if (this.getToColumn() < o.getToColumn()) {
			return -1;
		} else if (this.getToColumn() > o.getToColumn()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		return this.fileID + this.getFromLine() + this.getFromColumn()
				+ this.getToLine() + this.getToColumn();
	}

	@Override
	public boolean equals(Object o) {

		if (null == o) {
			return false;
		}

		if (!(o instanceof ElementInfo)) {
			return false;
		}

		final ElementInfo target = (ElementInfo) o;
		return (this.fileID == target.fileID)
				&& (this.fromLine == target.fromLine)
				&& (this.fromColumn == target.fromColumn)
				&& (this.toLine == target.toLine)
				&& (this.toColumn == target.toColumn);
	}

	private int fileID;

	private int methodID;

	private int fromLine;

	private int fromColumn;

	private int toLine;

	private int toColumn;
}
