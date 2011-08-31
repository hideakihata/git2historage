package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


/**
 * 未解決文を表すインターフェース
 * 
 * @author higo
 *
 * @param <T> 解決済みの型
 */
public interface UnresolvedStatementInfo<T extends StatementInfo> extends
        UnresolvedExecutableElementInfo<T> {

}
