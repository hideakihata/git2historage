package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGDataNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public abstract class PDGDataNode<T extends CFGDataNode<? extends VariableUsageInfo<?>>> extends
        PDGNode<T> {
    public PDGDataNode(final T node) {
        super(node);
    }
}
