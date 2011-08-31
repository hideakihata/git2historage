package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * 制御ノード以外のノードを表す
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class PDGNormalNode<T extends CFGNormalNode<? extends ExecutableElementInfo>>
        extends PDGNode<T> {

    protected PDGNormalNode(final T node) {
        super(node);
    }
}
