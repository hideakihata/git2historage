package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * このインターフェースは，メソッド情報を取得するためのメソッド郡を提供する．
 * 
 * @author higo
 *
 */
public interface MethodInfoAccessor extends Iterable<TargetMethodInfo> {

    /**
     * 対象メソッドのの数を返すメソッド.
     * @return 対象メソッドの数
     */
    public int getMethodCount();
}
