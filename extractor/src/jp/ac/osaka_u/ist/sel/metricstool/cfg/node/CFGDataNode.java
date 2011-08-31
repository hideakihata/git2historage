package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public abstract class CFGDataNode<T extends VariableUsageInfo<?>> extends CFGNode<T> {
    CFGDataNode(final T usage) {
        super(usage);
    }
}
