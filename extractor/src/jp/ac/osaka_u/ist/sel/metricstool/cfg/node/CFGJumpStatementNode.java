package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGJumpEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;

abstract public class CFGJumpStatementNode extends
		CFGStatementNode<JumpStatementInfo> {

	CFGJumpStatementNode(final JumpStatementInfo jumpStatement) {
		super(jumpStatement);
	}

	@Override
	public final boolean optimize() {

		final Set<CFGNode<?>> backwardNodes = this.getBackwardNodes();
		final Set<CFGNode<?>> forwardNodes = this.getForwardNodes();

		this.remove();

		// バックワードノード群とフォワードノード群をつなぐ
		for (final CFGNode<?> backwardNode : backwardNodes) {
			for (final CFGNode<?> forwardNode : forwardNodes) {
				final CFGJumpEdge edge = new CFGJumpEdge(backwardNode,
						forwardNode);
				backwardNode.addForwardEdge(edge);
				forwardNode.addBackwardEdge(edge);
			}
		}

		return true;
	}

	@Override
	final ExpressionInfo getDissolvingTarget() {
		return null;
	}

	@Override
	final JumpStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {
		return null;
	}

	@Override
	final JumpStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {
		return null;
	}
}
