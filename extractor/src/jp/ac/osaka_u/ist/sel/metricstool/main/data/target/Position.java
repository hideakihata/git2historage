package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ファイル内での位置を表すためのインターフェース．
 * 
 * @author higo
 */
public interface Position extends Comparable<Position>{

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    int getFromLine();

    /**
     * 開始列を表す
     * 
     * @return 開始列
     */
    int getFromColumn();

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    int getToLine();

    /**
     * 終了列を表す
     * 
     * @return 終了列
     */
    int getToColumn();
}