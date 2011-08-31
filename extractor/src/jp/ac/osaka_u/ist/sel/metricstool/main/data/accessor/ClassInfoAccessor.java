package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


/**
 * このインターフェースは，クラス情報を取得するためのメソッド郡を提供する．
 * 
 * @author higo
 *
 */
public interface ClassInfoAccessor extends Iterable<TargetClassInfo> {

    /**
     * 対象クラスの数を返すメソッド.
     * @return 対象クラスの数
     */
    public int getClassCount();
}
