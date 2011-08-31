package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;

/**
 * throw文を表すノード
 * 
 * @author higo
 * 
 */
public class CFGThrowStatementNode extends CFGStatementNode<ThrowStatementInfo> {

	/**
	 * ノードを生成するthrow文を与えて初期化
	 * 
	 * @param throwStatement
	 */
	CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
		super(throwStatement);
	}

	@Override
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		final StatementInfo statement = this.getCore();
		final ExpressionInfo expression = (ExpressionInfo) this
				.getDissolvingTarget().copy();

		// assertの判定部分が変数使用の式でない場合は分解を行う
		if (!CFGUtility.isDissolved(expression)) {
			return null;
		}

		// 古いノードを削除
		nodeFactory.removeNode(statement);
		this.remove();

		// 分解前の文から必要な情報を取得
		final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		this.makeDissolvedNode(expression, nodeFactory, dissolvedNodeList,
				dissolvedVariableUsageList);
		final ThrowStatementInfo newThrowStatement = this.makeNewElement(
				ownerSpace, dissolvedVariableUsageList.getFirst());
		final CFGNode<?> newNode = nodeFactory
				.makeNormalNode(newThrowStatement);
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
		final ThrowStatementInfo statement = this.getCore();
		return statement.getThrownExpression();
	}

	@Override
	ThrowStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpression) {

		if ((null == ownerSpace) || (1 != requiredExpression.length)) {
			throw new IllegalArgumentException();
		}

		final ThrowStatementInfo newStatement = new ThrowStatementInfo(
				ownerSpace, requiredExpression[0], fromLine, fromColumn,
				toLine, toColumn);
		return newStatement;
	}

	@Override
	ThrowStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpression) {

		if ((null == ownerSpace) || (1 != requiredExpression.length)) {
			throw new IllegalArgumentException();
		}

		final ThrowStatementInfo statement = this.getCore();
		final int fromLine = statement.getFromLine();
		final int fromColumn = statement.getFromColumn();
		final int toLine = statement.getToLine();
		final int toColumn = statement.getToColumn();

		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpression);
	}
}
