package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;


/**
 * デフォルトのCFGノードファクトリ このファクトリで生成されたノードはマップで管理される．
 * 
 * @author t-miyake
 * 
 */
public class DefaultCFGNodeFactory implements ICFGNodeFactory {

    /**
     * 生成されたノードを管理するマップ
     */
    private final ConcurrentMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>> elementToNodeMap;

    /**
     * 分解されたノードを管理するマップ． 分解されないノードは登録されない．
     */
    private final ConcurrentMap<ExecutableElementInfo, Set<CFGNode<? extends ExecutableElementInfo>>> elementToDissolvedNodesMap;

    /**
     * オブジェクトを生成
     */
    public DefaultCFGNodeFactory() {
        this.elementToNodeMap = new ConcurrentHashMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>>();
        this.elementToDissolvedNodesMap = new ConcurrentHashMap<ExecutableElementInfo, Set<CFGNode<? extends ExecutableElementInfo>>>();
    }

    /**
     * 既存のノードファクトリを与えて初期化． このコンストラクタで生成されるオブジェクトは既存のファクトリのノード構築情報を継承する．
     * 
     * @param nodeFactory
     *            既存のノードファクトリ
     */
    public DefaultCFGNodeFactory(final DefaultCFGNodeFactory nodeFactory) {
        this();

        if (null == nodeFactory) {
            throw new NullPointerException("nodeFactory is null");
        }

        this.elementToNodeMap.putAll(nodeFactory.elementToNodeMap);
    }

    /**
     * ExecutableElementInfo　から CFG のノーマルノードを生成
     */
    @Override
    public synchronized CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(
            final ExecutableElementInfo element) {
        CFGNormalNode<? extends ExecutableElementInfo> node = (CFGNormalNode<? extends ExecutableElementInfo>) this
                .getNode(element);

        if (null != node) {
            return node;
        }

        if (element instanceof SingleStatementInfo) {
            if (element instanceof ReturnStatementInfo) {
                node = new CFGReturnStatementNode((ReturnStatementInfo) element);
            } else if (element instanceof ThrowStatementInfo) {
                node = new CFGThrowStatementNode((ThrowStatementInfo) element);
            } else if (element instanceof BreakStatementInfo) {
                node = new CFGBreakStatementNode((BreakStatementInfo) element);
            } else if (element instanceof ContinueStatementInfo) {
                node = new CFGContinueStatementNode((ContinueStatementInfo) element);
            } else if (element instanceof AssertStatementInfo) {
                node = new CFGAssertStatementNode((AssertStatementInfo) element);
            } else if (element instanceof ExpressionStatementInfo) {
                node = new CFGExpressionStatementNode((ExpressionStatementInfo) element);
            } else if (element instanceof VariableDeclarationStatementInfo) {
                node = new CFGVariableDeclarationStatementNode(
                        (VariableDeclarationStatementInfo) element);
            } else {
                throw new IllegalStateException("Here shouldn't be reached!");
            }
        }

        else if (element instanceof CaseEntryInfo) {
            node = new CFGCaseEntryNode((CaseEntryInfo) element);
        }

        else if (element instanceof ExpressionInfo) {
            node = new CFGExpressionNode((ExpressionInfo) element);
        }

        else if (element instanceof ExecutableElementInfo) {
            node = new CFGEmptyNode((ExecutableElementInfo) element);
        }

        else {
            throw new IllegalArgumentException();
        }

        this.elementToNodeMap.put(element, node);

        return node;
    }

    /**
     * ConditionInfo から CFG のコントロールノードを生成
     */
    @Override
    public synchronized CFGControlNode makeControlNode(final ConditionInfo condition) {

        CFGControlNode node = (CFGControlNode) this.getNode(condition);
        if (null != node) {
            return node;
        }

        if (condition instanceof ForeachConditionInfo) {
            node = new CFGForeachControlNode((ForeachConditionInfo) condition);
            this.elementToNodeMap.put(condition, node);
        } else {
            node = new CFGControlNode(condition);
            this.elementToNodeMap.put(condition, node);
        }

        return node;
    }

    @Override
    public CFGNode<? extends ExecutableElementInfo> getNode(final ExecutableElementInfo statement) {
        return this.elementToNodeMap.get(statement);
    }

    @Override
    public synchronized boolean removeNode(final ExecutableElementInfo element) {
        return null != this.elementToNodeMap.remove(element) ? true : false;
    }

    @Override
    public Collection<CFGNode<? extends ExecutableElementInfo>> getAllNodes() {
        return Collections.unmodifiableCollection(this.elementToNodeMap.values());
    }

    @Override
    public Set<CFGNode<? extends ExecutableElementInfo>> getDissolvedNodes(
            final ExecutableElementInfo element) {

        if (null == element) {
            throw new IllegalArgumentException();
        }

        final Set<CFGNode<? extends ExecutableElementInfo>> nodes = this.elementToDissolvedNodesMap
                .get(element);
        return null == nodes ? null : Collections.unmodifiableSet(nodes);
    }

    @Override
    public synchronized void addDissolvedNode(final ExecutableElementInfo element,
            final CFGNode<?> newNode) {

        if ((null == element) || (null == newNode)) {
            throw new IllegalArgumentException();
        }

        Set<CFGNode<? extends ExecutableElementInfo>> nodes = this.elementToDissolvedNodesMap
                .get(element);
        if (null == nodes) {
            nodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
            this.elementToDissolvedNodesMap.put(element, nodes);
        }
        nodes.add(newNode);
    }

    @Override
    public synchronized void addDissolvedNodes(final ExecutableElementInfo element,
            final Set<CFGNode<?>> newNodes) {

        if ((null == element) || (null == newNodes)) {
            throw new IllegalArgumentException();
        }

        Set<CFGNode<? extends ExecutableElementInfo>> nodes = this.elementToDissolvedNodesMap
                .get(element);
        if (null == nodes) {
            nodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
            this.elementToDissolvedNodesMap.put(element, nodes);
        }
        nodes.addAll(newNodes);
    }

    public boolean isDissolvedNode(final CFGNode<? extends ExecutableElementInfo> node) {

        for (final Set<CFGNode<? extends ExecutableElementInfo>> dissolvedNodes : this.elementToDissolvedNodesMap
                .values()) {
            if (dissolvedNodes.contains(node)) {
                return true;
            }
        }

        return false;
    }
}
