package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collection;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public class SimpleCFG extends CFG {

    public SimpleCFG(final ICFGNodeFactory nodeFactory) {
        super(nodeFactory);
    }

    public void setEnterNode(final CFGNode<? extends ExecutableElementInfo> enterNode) {

        if (null == enterNode) {
            throw new IllegalArgumentException();
        }

        this.enterNode = enterNode;
    }

    public void addExitNode(final CFGNode<? extends ExecutableElementInfo> exitNode) {

        if (null == exitNode) {
            throw new IllegalArgumentException();
        }

        this.exitNodes.add(exitNode);
    }

    public void addExitNodes(final Set<CFGNode<? extends ExecutableElementInfo>> exitNodes) {

        if (null == exitNodes) {
            throw new IllegalArgumentException();
        }

        this.exitNodes.addAll(exitNodes);
    }

    public void removeExitNode(final CFGNode<? extends ExecutableElementInfo> exitNode) {

        if (null == exitNode) {
            throw new IllegalArgumentException();
        }

        this.exitNodes.remove(exitNode);
    }

    public void addNode(final CFGNode<? extends ExecutableElementInfo> node) {

        if (null == node) {
            throw new IllegalArgumentException();
        }

        this.nodes.add(node);
    }

    public void addNodes(final Collection<CFGNode<? extends ExecutableElementInfo>> nodes) {

        if (null == nodes) {
            throw new IllegalArgumentException();
        }

        this.nodes.addAll(nodes);
    }

    public void removeNode(final CFGNode<? extends ExecutableElementInfo> node) {

        if (null == node) {
            throw new IllegalArgumentException();
        }

        this.nodes.remove(node);
    }
}
