package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


/**
 * 任意のオブジェクトに関して，開始行，開始列，終了行，終了列を管理するインタフェース.
 * @author kou-tngt
 *
 */
public interface PositionManager {

    /**
     * 引数keyの開始行を返す
     * @param key　開始行を取得したい要素
     * @return　開始行
     */
    public int getStartLine(Object key);

    /**
     * 引数keyの開始列を返す
     * @param key　開始列を取得したい要素
     * @return　開始列
     */
    public int getStartColumn(Object key);

    /**
     * 引数keyの終了行を返す
     * @param key　終了行を取得したい要素
     * @return　終了行
     */
    public int getEndLine(Object key);

    /**
     * 引数keyの終了列を返す
     * @param key　終了列を取得したい要素
     * @return　開始列
     */
    public int getEndColumn(Object key);

    /**
     * 引数keyの開始行をセットする
     * @param key　開始行をセットする要素
     * @param line　開始行
     */
    public void setStartLine(Object key, int line);

    /**
     * 引数keyの開始列をセットする
     * @param key　開始列をセットする要素
     * @param column　開始列
     */
    public void setStartColumn(Object key, int column);

    /**
     * 引数keyの終了行をセットする
     * @param key　終了行をセットする要素
     * @param line　終了行
     */
    public void setEndLine(Object key, int line);

    /**
     * 引数keyの終了列をセットする
     * @param key　終了列をセットする要素
     * @param column　終了列
     */
    public void setEndColumn(Object key, int column);

    /**
     * 引数keyの開始行，開始列，終了行，終了列をセットする
     * @param key　情報をセットしたい要素
     * @param startLine 開始行
     * @param startColumn　開始列
     * @param endLine　終了行
     * @param endColumn　終了列
     */
    public void setPosition(Object key, int startLine, int startColumn, int endLine, int endColumn);

}