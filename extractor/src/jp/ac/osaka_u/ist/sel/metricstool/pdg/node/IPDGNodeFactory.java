package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;

import java.util.Collection;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

public interface IPDGNodeFactory {

	PDGControlNode makeControlNode(CFGControlNode cfgNode);

	PDGNormalNode<?> makeNormalNode(CFGNormalNode<?> cfgNode);

	PDGNode<?> getNode(ExecutableElementInfo element);

	SortedSet<PDGNode<?>> getAllNodes();

	void removeNode(ExecutableElementInfo element);

	void addNode(PDGNode<?> node);

	void addNodes(Collection<PDGNode<?>> nodes);
}
