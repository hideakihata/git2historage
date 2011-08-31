package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;

public class CFGForeachControlNode extends CFGControlNode {

	CFGForeachControlNode(final ForeachConditionInfo condition) {
		super(condition);
	}

	@Override
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		if (null == nodeFactory) {
			throw new IllegalArgumentException();
		}

		final ExpressionInfo iteratorExpression = this.getDissolvingTarget();
		final ExpressionInfo target = (ExpressionInfo) iteratorExpression
				.copy();

		// conditionが分解の必要がない場合は何もせずに抜ける
		if (!CFGUtility.isDissolved(target)) {
			return null;
		}

		// 分解前の文から必要な情報を取得
		final ForeachConditionInfo condition = (ForeachConditionInfo) this
				.getCore();
		final ConditionalBlockInfo ownerForeachBlock = (ConditionalBlockInfo) condition
				.getOwnerExecutableElement();
		final LocalSpaceInfo outerUnit = ownerForeachBlock.getOwnerSpace();
		final int fromLine = target.getFromLine();
		final int fromColumn = target.getFromColumn();
		final int toLine = target.getToLine();
		final int toColumn = target.getToColumn();

		// 古いノードを削除
		nodeFactory.removeNode(condition);
		this.remove();

		final VariableDeclarationStatementInfo newStatement = this
				.makeVariableDeclarationStatement(outerUnit, target);
		final ExpressionInfo newIteratorExpression = LocalVariableUsageInfo
				.getInstance(newStatement.getDeclaredLocalVariable(), true,
						false, ownerForeachBlock.getOuterCallableUnit(),
						fromLine, fromColumn, toLine, toColumn);
		final ForeachConditionInfo newCondition = this.makeNewElement(
				ownerForeachBlock, newIteratorExpression);
		newCondition.setOwnerConditionalBlock(ownerForeachBlock);
		newCondition.setOwnerExecutableElement(ownerForeachBlock);
		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		dissolvedNodeList.add(nodeFactory.makeNormalNode(newStatement));
		dissolvedNodeList.add(nodeFactory.makeControlNode(newCondition));

		// 分解したノードをエッジでつなぐ
		this.makeEdges(dissolvedNodeList);

		// 分解したノード群からCFGを構築
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// 分割したノードをノードファクトリのdissolvedNodeに登録
		nodeFactory.addDissolvedNodes(condition, newCFG.getAllNodes());

		return newCFG;
	}

	@Override
	ExpressionInfo getDissolvingTarget() {
		final ForeachConditionInfo condition = (ForeachConditionInfo) this
				.getCore();
		return condition.getIteratorExpression();
	}

	/**
	 * 与えられた引数の情報を用いて，ノードの核となるプログラム要素を生成する
	 */
	@Override
	ForeachConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ForeachConditionInfo originalCondition = (ForeachConditionInfo) this
				.getCore();
		final VariableDeclarationStatementInfo iteratorVariable = (VariableDeclarationStatementInfo) originalCondition
				.getIteratorVariable().copy();
		final CallableUnitInfo ownerMethod = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		return new ForeachConditionInfo(ownerMethod, fromLine, fromColumn,
				toLine, toColumn, iteratorVariable, requiredExpressions[0]);
	}

	/**
	 * 与えられた引数の情報を用いて，ノードの核となるプログラム要素を生成する
	 */
	@Override
	ForeachConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ForeachConditionInfo originalCondition = (ForeachConditionInfo) this
				.getCore();
		final int fromLine = originalCondition.getFromLine();
		final int fromColumn = originalCondition.getFromLine();
		final int toLine = originalCondition.getToLine();
		final int toColumn = originalCondition.getToColumn();

		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpressions);
	}
}
