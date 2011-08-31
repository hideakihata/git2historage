package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * メソッドの入口を表すノード
 * 
 * @author higo
 *
 */
public class PDGMethodEnterNode extends PDGControlNode {

    public static PDGMethodEnterNode createNode(final CallableUnitInfo owner) {
        PDGMethodEnterNode enterNode = NODE_MAP.get(owner);

        if (null == enterNode) {
            final ICFGNodeFactory nodeFactory = new DefaultCFGNodeFactory();
            final CFGControlNode cfgNode = nodeFactory.makeControlNode(new PseudoConditionInfo(
                    owner));
            enterNode = new PDGMethodEnterNode(cfgNode);
            NODE_MAP.put(owner, enterNode);
        }
        return enterNode;
    }

    private static final ConcurrentMap<CallableUnitInfo, PDGMethodEnterNode> NODE_MAP = new ConcurrentHashMap<CallableUnitInfo, PDGMethodEnterNode>();

    private PDGMethodEnterNode(final CFGControlNode node) {
        super(node);
    }

    /**
     * PDGMethodEnterNodeのコンストラクタ内でもちいるための，疑似ConditionInfo
     * 
     * @author higo
     * 
     */
    @SuppressWarnings("serial")
    static public class PseudoConditionInfo implements ConditionInfo {

        PseudoConditionInfo(final CallableUnitInfo owner) {
            this.owner = owner;
        }

        @Override
        public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
            return new HashSet<CallInfo<? extends CallableUnitInfo>>();
        }

        @Override
        public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
            return new HashSet<VariableInfo<? extends UnitInfo>>();
        }

        @Override
        public CallableUnitInfo getOwnerMethod() {
            return this.owner;
        }

        @Override
        public LocalSpaceInfo getOwnerSpace() {
            return this.owner;
        }

        @Override
        public String getText() {
            return "EnterNode";
        }

        @Override
        public Set<ReferenceTypeInfo> getThrownExceptions() {
            return new HashSet<ReferenceTypeInfo>();
        }

        @Override
        public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
            return new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        }

        @Override
        public ConditionalBlockInfo getOwnerConditionalBlock() {
            return null;
        }

        @Override
        public void setOwnerConditionalBlock(final ConditionalBlockInfo ownerConditionalBlock) {
        }

        @Override
        public int getFromColumn() {
            return 0;
        }

        @Override
        public int getFromLine() {
            return 0;
        }

        @Override
        public int getToColumn() {
            return 0;
        }

        @Override
        public int getToLine() {
            return 0;
        }

        @Override
        public int compareTo(Position o) {

            if (this.getFromLine() < o.getFromLine()) {
                return -1;
            } else if (this.getFromLine() > o.getFromLine()) {
                return 1;
            } else if (this.getFromColumn() < o.getFromColumn()) {
                return -1;
            } else if (this.getFromColumn() > o.getFromColumn()) {
                return 1;
            } else if (this.getToLine() < o.getToLine()) {
                return -1;
            } else if (this.getToLine() > o.getToLine()) {
                return 1;
            } else if (this.getToColumn() < o.getToColumn()) {
                return -1;
            } else if (this.getToColumn() > o.getToColumn()) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public int hashCode() {
            return this.getText().hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof PseudoConditionInfo;
        }

        @Override
        public ExecutableElementInfo copy() {
            final CallableUnitInfo ownerMethod = this.getOwnerMethod();
            return new PseudoConditionInfo(ownerMethod);
        }

        private final CallableUnitInfo owner;
    }
}
