package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;


/**
 * ファイル内での位置情報をセットするためのインターフェース
 * 
 * @author higo
 */
public interface PositionSetting extends Position {

    /**
     * 開始行をセットする
     * 
     * @param line 開始行
     */
    void setFromLine(int line);

    /**
     * 開始列をセットする
     * 
     * @param column 開始列
     */
    void setFromColumn(int column);

    /**
     * 終了行をセットする
     * 
     * @param line 終了行
     */
    void setToLine(int line);

    /**
     * 終了列をセットする
     * 
     * @param column 終了列
     */
    void setToColumn(int column);
}
