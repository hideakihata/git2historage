package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;


/**
 * このインターフェースは，フィールド情報を取得するためのメソッド郡を提供する．
 * 
 * @author higo
 *
 */
public interface FieldInfoAccessor extends Iterable<TargetFieldInfo> {

    /**
     * 対象フィールドの数を返すメソッド.
     * @return 対象フィールドの数
     */
    public int getFieldCount();
}
