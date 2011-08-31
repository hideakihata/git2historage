package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;


/**
 * 未解決参照型を表すインターフェース
 * 
 * @author higo
 *
 * @param <T> 解決された型を表す型パラメータ
 */
public interface UnresolvedReferenceTypeInfo<T extends ReferenceTypeInfo> extends
        UnresolvedTypeInfo<T> {

}
