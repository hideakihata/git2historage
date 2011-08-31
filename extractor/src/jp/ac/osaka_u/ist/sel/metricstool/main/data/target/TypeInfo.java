package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;


/**
 * 「型」を表すインターフェース．
 * 
 * @author higo
 * 
 */
public interface TypeInfo extends Serializable {

    /**
     * 型名を返す
     * 
     * @return 型名
     */
    String getTypeName();

    /**
     * 等価性のチェック
     * 
     * @param typeInfo 比較対照オブジェクト
     * @return 等しい場合はtrue，そうでない場合はfalse
     */
    boolean equals(TypeInfo typeInfo);

}
