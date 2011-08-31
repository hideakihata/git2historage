package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * 実行可能な単位を表す要素を表すインターフェース
 * 
 * @author higo
 *
 * @param <T> 名前書解決された型を表す型パラメータ
 */
public interface UnresolvedExecutableElementInfo<T extends ExecutableElementInfo> extends
        Resolvable<T>, PositionSetting, UnresolvedHavingOuterUnit {

}
