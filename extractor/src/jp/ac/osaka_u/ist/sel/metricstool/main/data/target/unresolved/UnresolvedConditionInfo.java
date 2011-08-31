package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;

/**
 * 未解決条件文を表すクラス
 * 
 * @author higo
 *
 * @param <T> 解決された型を表す型パラメータ
 */
public interface UnresolvedConditionInfo<T extends ConditionInfo> extends
        UnresolvedExecutableElementInfo<T> {

}
