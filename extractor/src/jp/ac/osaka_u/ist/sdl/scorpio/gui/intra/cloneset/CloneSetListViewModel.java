package jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.cloneset;


import java.util.Set;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;


/**
 * CloneSetListViewのモデル
 * 
 * @author higo
 *
 */
class CloneSetListViewModel extends AbstractTableModel {

    public CloneSetListViewModel(final Set<CloneSetInfo> cloneSets) {
        this.cloneSets = cloneSets.toArray(new CloneSetInfo[] {});
    }

    /**
     * 行数を返す
     * 
     * @return 行数
     */
    @Override
    public int getRowCount() {
        return this.cloneSets.length;
    }

    /**
     * 列数を返す
     * 
     * @return 列数
     */
    @Override
    public int getColumnCount() {
        return TITLES.length;
    }

    /**
     * 指定されたセルのオブジェクトを返す
     * 
     * @param row 行
     * @param col 列
     * @return 指定されたセルのオブジェクト
     */
    @Override
    public Object getValueAt(int row, int col) {

        switch (col) {
        case COL_SIZE:
            return this.cloneSets[row].getNumberOfElements();
        case COL_LENGTH:
            return this.cloneSets[row].getLength();
        case COL_GAPS:
            return this.cloneSets[row].getNumberOfGapps();
        default:
            assert false : "Here shouldn't be reached!";
            return null;
        }
    }

    /**
     * セルの型を返す
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
     * 指定された行のクローンセットを返す
     * 
     * @param row　行
     * @return　指定された行のクローンセット
     */
    public CloneSetInfo getCloneset(final int row) {
        return this.cloneSets[row];
    }

    /**
     * クローンセット群を返す
     * 
     * @return クローンセット群
     */
    public CloneSetInfo[] getClonesets() {
        return this.cloneSets;
    }

    static final int COL_SIZE = 0;

    static final int COL_LENGTH = 1;

    static final int COL_GAPS = 2;

    static final String[] TITLES = new String[] {"# of Instances", "size", "gaps" };

    final private CloneSetInfo[] cloneSets;

}
