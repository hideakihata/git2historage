package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Collection;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * CFGノードのファクトリを表すインターフェース
 * @author t-miyake
 *
 */
public interface ICFGNodeFactory {

    /**
     * CFGのコントロールノードを生成
     * @param element 生成するCFGノードに対応する文
     * @return CFGノード
     */
    CFGControlNode makeControlNode(ConditionInfo condition);

    /**
     * CFGのノーマルノードを生成
     * @param element 生成するCFGノードに対応する文
     * @return CFGノード
     */
    CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(ExecutableElementInfo element);

    /**
     * このファクトリで生成されたノードのうち，引数で指定された文に対応するノードを返す
     * @param element 文
     * @return このファクトリで生成されたノード．対応するノードが生成済みでない場合はnull．
     */
    CFGNode<? extends ExecutableElementInfo> getNode(ExecutableElementInfo element);

    boolean removeNode(ExecutableElementInfo element);

    Collection<CFGNode<? extends ExecutableElementInfo>> getAllNodes();

    Set<CFGNode<? extends ExecutableElementInfo>> getDissolvedNodes(ExecutableElementInfo element);

    void addDissolvedNode(final ExecutableElementInfo element,
            final CFGNode<? extends ExecutableElementInfo> node);

    void addDissolvedNodes(final ExecutableElementInfo element,
            final Set<CFGNode<? extends ExecutableElementInfo>> nodes);

    boolean isDissolvedNode(final CFGNode<? extends ExecutableElementInfo> node);
}
