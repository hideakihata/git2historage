package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGParameterOutNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;


public class PDGParameterOutNode extends PDGDataNode<CFGParameterOutNode> implements PDGDataOutNode {

    public static PDGParameterOutNode getInstance(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        final CFGParameterOutNode cfgNode = CFGParameterOutNode.getInstance(parameter);
        return new PDGParameterOutNode(cfgNode);
    }

    private PDGParameterOutNode(final CFGParameterOutNode node) {
        super(node);
    }
}
