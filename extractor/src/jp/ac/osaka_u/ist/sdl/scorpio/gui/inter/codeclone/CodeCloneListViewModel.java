package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.codeclone;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;

/**
 * 
 * @author higo
 * 
 */
class CodeCloneListViewModel extends AbstractTableModel {

	/**
	 * コンストラクタ
	 * 
	 * @param cloneSet
	 *            　モデルを初期化するためのクローンセット
	 */
	CodeCloneListViewModel(final CloneSetInfo cloneSet) {
		this.codeclones = cloneSet.getCodeclones().toArray(
				new CodeCloneInfo[] {});
		for (int index = 0; index < this.codeclones.length; index++) {
			this.codeclones[index].setID(index);
		}
	}

	/**
	 * 行数を返す
	 * 
	 * @Return 行数
	 */
	@Override
	public int getRowCount() {
		return this.codeclones.length;
	}

	/**
	 * 列数を返す
	 * 
	 * @Return 列数
	 */
	@Override
	public int getColumnCount() {
		return TITLES.length;
	}

	/**
	 * 指定した場所のオブジェクトを返す
	 * 
	 * @param row
	 *            行
	 * @param col
	 *            列
	 */
	@Override
	public Object getValueAt(int row, int col) {

		switch (col) {
		case COL_ID:
			return this.codeclones[row].getID();
		case COL_ELEMENTS:
			return this.codeclones[row].getElements().size();
		case COL_GAPS:
			return this.codeclones[row].getNumberOfGapps();
		default:
			assert false : "Here shouldn't be reached!";
			return null;
		}
	}

	/**
	 * 列の型を指定
	 */
	@Override
	public Class<?> getColumnClass(int row) {
		return Integer.class;
	}

	/**
	 * タイトルを返す
	 */
	@Override
	public String getColumnName(int col) {
		return TITLES[col];
	}

	/**
	 * 指定した列のコードクローンを返す
	 * 
	 * @param row
	 *            指令した列
	 * @return　指定した列のコードクローン
	 */
	public CodeCloneInfo getCodeClone(final int row) {
		return this.codeclones[row];
	}

	/**
	 * このモデル内のコードクローン一覧を返す
	 * 
	 * @return このモデル内のコードクローン一覧
	 */
	public CodeCloneInfo[] getCodeClones() {
		return this.codeclones;
	}

	private String getPositionText(final CodeCloneInfo codeFragment) {
		final StringBuilder text = new StringBuilder();
		text.append(codeFragment.getFirstElement().getFromLine());
		text.append(".");
		text.append(codeFragment.getFirstElement().getFromColumn());
		text.append(" - ");
		text.append(codeFragment.getLastElement().getToLine());
		text.append(".");
		text.append(codeFragment.getLastElement().getToColumn());
		return text.toString();
	}

	static final int COL_ID = 0;

	static final int COL_ELEMENTS = 1;

	static final int COL_GAPS = 2;

	static final String[] TITLES = new String[] { "ID", "# of Elements", "GAPS" };

	final private CodeCloneInfo[] codeclones;

}
