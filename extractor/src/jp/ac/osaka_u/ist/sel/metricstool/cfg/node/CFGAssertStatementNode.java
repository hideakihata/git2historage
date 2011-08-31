package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;

/**
 * assert文を表すCFGノード
 * 
 * @author higo
 * 
 */
public class CFGAssertStatementNode extends
		CFGStatementNode<AssertStatementInfo> {

	CFGAssertStatementNode(final AssertStatementInfo statement) {
		super(statement);
	}

	/**
	 * assert文を分解する
	 */
	@Override
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		if (null == nodeFactory) {
			throw new IllegalArgumentException();
		}

		final AssertStatementInfo statement = this.getCore();
		final ExpressionInfo target = (ExpressionInfo) this
				.getDissolvingTarget().copy();

		// assertの判定部分が分解の必要がない場合は何もせずに抜ける
		if (!CFGUtility.isDissolved(target)) {
			return null;
		}

		// 分解前の文から必要な情報を取得
		final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();

		// 古いノードを削除
		nodeFactory.removeNode(statement);
		this.remove();

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		this.makeDissolvedNode(target, nodeFactory, dissolvedNodeList,
				dissolvedVariableUsageList);
		final AssertStatementInfo newStatement = this.makeNewElement(
				ownerSpace, dissolvedVariableUsageList.getFirst());
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newStatement);
		dissolvedNodeList.add(newNode);

		// 分解したノードをエッジでつなぐ
		this.makeEdges(dissolvedNodeList);

		// 分解したノード群からCFGを構築
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// 分割したノードをノードファクトリのdissolvedNodeに登録
		nodeFactory.addDissolvedNodes(statement, newCFG.getAllNodes());

		return newCFG;
	}

	@Override
	ExpressionInfo getDissolvingTarget() {
		final AssertStatementInfo statement = this.getCore();
		return statement.getAssertedExpression();
	}

	@Override
	AssertStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final AssertStatementInfo statement = this.getCore();
		final ExpressionInfo messageExpression = statement
				.getMessageExpression();

		final AssertStatementInfo newStatement = new AssertStatementInfo(
				ownerSpace, requiredExpressions[0], messageExpression,
				fromLine, fromColumn, toLine, toColumn);
		return newStatement;

	}

	@Override
	AssertStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final AssertStatementInfo statement = this.getCore();
		final int fromLine = statement.getFromLine();
		final int fromColumn = statement.getFromColumn();
		final int toLine = statement.getToLine();
		final int toColumn = statement.getToColumn();
		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpressions);
	}
}
