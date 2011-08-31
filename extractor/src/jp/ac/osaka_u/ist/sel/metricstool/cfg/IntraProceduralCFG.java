package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGControlEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGExceptionEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGJumpEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGBreakStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGContinueStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGReturnStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGVariableDeclarationStatementNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;

/**
 * 
 * @author t-miyake, higo
 * 
 */
public class IntraProceduralCFG extends CFG {

	/**
	 * CFG構築対象要素
	 */
	private final Object element;

	/**
	 * 呼び出し可能ユニットとノードファクトリを与えて，制御フローグラフを生成
	 * 
	 * @param unit
	 *            呼び出し可能ユニット
	 * @param nodeFactory
	 *            ノードファクトリ
	 */
	public IntraProceduralCFG(final CallableUnitInfo unit,
			final ICFGNodeFactory nodeFactory) {
		this(unit, nodeFactory, true, DISSOLUTION.FALSE);
	}

	/**
	 * 
	 * @param unit
	 * @param nodeFactory
	 * @param optimize
	 *            caseラベルや，break, continueを取り除くかどうか
	 * @param dissolve
	 *            複雑な文を分解して複数の簡単な文にするかどうか
	 */
	public IntraProceduralCFG(final CallableUnitInfo unit,
			final ICFGNodeFactory nodeFactory, final boolean optimize,
			final DISSOLUTION dissolve) {

		super(nodeFactory);

		if (null == unit) {
			throw new IllegalArgumentException("unit is null");
		}

		this.element = unit;

		if (unit instanceof ExternalMethodInfo
				|| unit instanceof ExternalConstructorInfo) {
			throw new IllegalArgumentException(
					"unit is an external infromation.");
		}

		final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
				unit.getStatementsWithoutSubsequencialBlocks(), nodeFactory);
		this.enterNode = statementsCFG.getEnterNode();
		this.exitNodes.addAll(statementsCFG.getExitNodes());
		this.nodes.addAll(statementsCFG.getAllNodes());

		if (optimize) {
			this.optimizeCFG();
		}

		if (dissolve == DISSOLUTION.TRUE || dissolve == DISSOLUTION.PARTLY) {
			this.dissolveCFG();
		}

		if (dissolve == DISSOLUTION.PARTLY) {
			this.packCFG();
		}
	}

	/**
	 * 呼び出し可能ユニットを与えて，制御フローグラフを生成
	 * 
	 * @param unit
	 *            呼び出し可能ユニット
	 */
	public IntraProceduralCFG(final CallableUnitInfo unit) {
		this(unit, new DefaultCFGNodeFactory());
	}

	/**
	 * 文の制御フローグラフを生成する
	 * 
	 * @param statement
	 * @param nodeFactory
	 */
	private IntraProceduralCFG(final StatementInfo statement,
			final ICFGNodeFactory nodeFactory) {

		super(nodeFactory);

		if (null == statement) {
			throw new IllegalArgumentException();
		}

		this.element = statement;

		// 作成したCFGをキャッシュとして持つ
		statementCFG.put(statement, this);

		// 単文の場合
		if (statement instanceof SingleStatementInfo) {
			final CFGNormalNode<?> node = nodeFactory.makeNormalNode(statement);
			assert null != node : "node is null!";
			this.enterNode = node;
			this.exitNodes.add(node);
			this.nodes.add(node);

			// break文の場合は対応するブロックのexitNodesに追加する
			if (statement instanceof BreakStatementInfo) {
				final BreakStatementInfo breakStatement = (BreakStatementInfo) statement;
				final BlockInfo correspondingBlock = breakStatement
						.getCorrespondingBlock();
				final CFG correspondingBlockCFG = getCFG(correspondingBlock,
						nodeFactory);
				correspondingBlockCFG.exitNodes.add(node);
			}

			// 例外に関する処理
			for (final ReferenceTypeInfo thrownException : statement
					.getThrownExceptions()) {
				final CatchBlockInfo correspondingCatchBlock = CatchBlockInfo
						.getCorrespondingCatchBlock(statement, thrownException);

				if (null != correspondingCatchBlock) {
					final CFG catchBlockCFG = new IntraProceduralCFG(
							correspondingCatchBlock, nodeFactory);
					this.nodes.addAll(catchBlockCFG.nodes);
					final CFGExceptionEdge edge = new CFGExceptionEdge(node,
							catchBlockCFG.getEnterNode(),
							correspondingCatchBlock.getCaughtException()
									.getType());
					node.addForwardEdge(edge);
				}
			}
		}

		// caseエントリの場合
		else if (statement instanceof CaseEntryInfo) {

			final CaseEntryInfo caseEntry = (CaseEntryInfo) statement;
			final CFGNormalNode<?> node = nodeFactory.makeNormalNode(caseEntry);
			this.enterNode = node;
			this.exitNodes.add(node);
			this.nodes.add(node);
		}

		// Labelの場合
		else if (statement instanceof LabelInfo) {
			// 何もしなくていいはず
		}

		// if文の場合
		else if (statement instanceof IfBlockInfo) {

			// if文の条件式からコントロールノードを生成
			final IfBlockInfo ifBlock = (IfBlockInfo) statement;
			final ConditionInfo condition = ifBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.enterNode = controlNode;
			this.nodes.add(controlNode);

			// if文の内側を処理
			{
				final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
						ifBlock.getStatementsWithoutSubsequencialBlocks(),
						nodeFactory);
				this.nodes.addAll(statementsCFG.nodes);

				// if文の内部が空の場合は，if文の条件式がexitノードになる
				if (statementsCFG.isEmpty()) {
					this.exitNodes.add(controlNode);
				}

				// if文の内部が空でない場合は，内部の最後の文がexitノードになる
				else {
					final CFGControlEdge edge = new CFGControlEdge(controlNode,
							statementsCFG.getEnterNode(), true);
					controlNode.addForwardEdge(edge);
					this.exitNodes.addAll(statementsCFG.getExitNodes());
				}
			}

			// 対応するelse文がある場合の処理
			if (ifBlock.hasElseBlock()) {
				final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
						ifBlock.getSequentElseBlock()
								.getStatementsWithoutSubsequencialBlocks(),
						nodeFactory);
				this.nodes.addAll(statementsCFG.nodes);

				// else文の内部が空の場合は，if文の条件式がexitノードになる
				if (statementsCFG.isEmpty()) {
					this.exitNodes.add(controlNode);
				}

				// else文の内部が～でない場合は，内部の文の最後の文がexitノードになる
				else {
					final CFGControlEdge edge = new CFGControlEdge(controlNode,
							statementsCFG.getEnterNode(), false);
					controlNode.addForwardEdge(edge);
					this.exitNodes.addAll(statementsCFG.getExitNodes());
				}
			}

			// 対応するelse文がない場合は，if文の条件式がexitノードになる
			else {
				this.exitNodes.add(controlNode);
			}
		}

		// while文の場合
		else if (statement instanceof WhileBlockInfo) {

			// while文の条件式からコントロールノードを生成
			final WhileBlockInfo whileBlock = (WhileBlockInfo) statement;
			final ConditionInfo condition = whileBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.enterNode = controlNode;
			this.exitNodes.add(controlNode);
			this.nodes.add(controlNode);

			// while文内部の処理
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					whileBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);

			// 内部が空でない場合は処理を行う
			if (!statementsCFG.isEmpty()) {

				{
					final CFGControlEdge edge = new CFGControlEdge(controlNode,
							statementsCFG.getEnterNode(), true);
					controlNode.addForwardEdge(edge);
				}

				for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

					// return文の場合はexitノードに追加
					if (exitNode instanceof CFGReturnStatementNode) {
						this.exitNodes.add(exitNode);
					}

					// continue文の場合
					else if (exitNode instanceof CFGContinueStatementNode) {

						final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
								.getCore();
						final BlockInfo correspondingBlock = continueStatement
								.getCorrespondingBlock();

						// continue文のに対応しているのがこのwhile文の時
						if (statement == correspondingBlock) {
							final CFGJumpEdge edge = new CFGJumpEdge(exitNode,
									controlNode);
							exitNode.addForwardEdge(edge);
						}

						// continue文のに対応しているのがこのwhile文ではない時
						else {
							this.exitNodes.add(exitNode);
						}
					}

					else {
						final CFGNormalEdge edge = new CFGNormalEdge(exitNode,
								controlNode);
						exitNode.addForwardEdge(edge);
					}
				}
			}
		}

		// else 文の場合
		else if (statement instanceof ElseBlockInfo) {
			// else文は対応するif文で処理しているため，ここではなにもしない
		}

		// do文の場合
		else if (statement instanceof DoBlockInfo) {

			// do文の条件式からコントロールノードを生成
			final DoBlockInfo doBlock = (DoBlockInfo) statement;
			final ConditionInfo condition = doBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.exitNodes.add(controlNode);
			this.nodes.add(controlNode);

			// do文内部の処理
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					doBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);

			// コントロールノードからdo文内部へ遷移
			{
				final CFGControlEdge edge = new CFGControlEdge(controlNode,
						statementsCFG.getEnterNode(), true);
				controlNode.addForwardEdge(edge);
			}

			// 内部が空の時は，do文の条件式がenterノードになる
			if (statementsCFG.isEmpty()) {
				this.enterNode = controlNode;
			}

			// 空でない場合は，内部CFGのenterノードが，このCFGのenterノードになる
			else {
				this.enterNode = statementsCFG.getEnterNode();
				for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

					// Return文の場合はexitノードに追加
					if (exitNode instanceof CFGReturnStatementNode) {
						this.exitNodes.add(exitNode);
					}

					else {
						final CFGNormalEdge edge = new CFGNormalEdge(exitNode,
								controlNode);
						exitNode.addForwardEdge(edge);
					}
				}
			}
		}

		// for文の場合
		else if (statement instanceof ForBlockInfo) {

			// for文の条件式からコントロールノードを生成
			final ForBlockInfo forBlock = (ForBlockInfo) statement;
			final ConditionInfo condition = forBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null";
			this.nodes.add(controlNode);

			// EmptyExpressionでなければ，exitNodesに追加
			if (!(condition instanceof EmptyExpressionInfo)) {
				this.exitNodes.add(controlNode);
			}

			// 初期化式からCFGを生成
			final SortedSet<ConditionInfo> initializers = forBlock
					.getInitializerExpressions();
			final SequentialExpressionsCFG initializersCFG = new SequentialExpressionsCFG(
					initializers, nodeFactory);
			this.nodes.addAll(initializersCFG.nodes);

			// 初期化式をfor文のCFGに追加
			if (initializersCFG.isEmpty()) {
				this.enterNode = controlNode;
			} else {
				this.enterNode = initializersCFG.getEnterNode();
				for (final CFGNode<?> exitNode : initializersCFG.getExitNodes()) {
					final CFGNormalEdge edge = new CFGNormalEdge(exitNode,
							controlNode);
					exitNode.addForwardEdge(edge);
				}
			}

			// 繰り返し式からCFGを生成
			final SortedSet<ExpressionInfo> iterators = forBlock
					.getIteratorExpressions();
			final SequentialExpressionsCFG iteratorsCFG = new SequentialExpressionsCFG(
					iterators, nodeFactory);
			this.nodes.addAll(iteratorsCFG.nodes);

			// for文の内部の処理
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					forBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);

			// for文の内部が空の場合
			if (statementsCFG.isEmpty()) {

				// 繰り返し式が空の場合
				if (iteratorsCFG.isEmpty()) {
					final CFGControlEdge edge = new CFGControlEdge(controlNode,
							controlNode, true);
					controlNode.addForwardEdge(edge);
				}

				// 繰り返し式が空でない場合
				else {
					{
						final CFGControlEdge edge = new CFGControlEdge(
								controlNode, iteratorsCFG.getEnterNode(), true);
						controlNode.addForwardEdge(edge);
					}
					for (final CFGNode<?> exitNode : iteratorsCFG
							.getExitNodes()) {

						// Return文の場合はexitノードに追加
						if (exitNode instanceof CFGReturnStatementNode) {
							this.exitNodes.add(exitNode);
						}

						// continue文の場合
						else if (exitNode instanceof CFGContinueStatementNode) {

							final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
									.getCore();
							final BlockInfo correspondingBlock = continueStatement
									.getCorrespondingBlock();
							// continue文の次に実行されるのが，このwhile文の条件式の時
							if (statement == correspondingBlock) {
								final CFGJumpEdge edge = new CFGJumpEdge(
										exitNode, controlNode);
								exitNode.addForwardEdge(edge);
							}

							// continue文の次に実行されるのが，このwhile文の条件式ではない時
							else {
								this.exitNodes.add(exitNode);
							}

						}

						else {
							final CFGNormalEdge edge = new CFGNormalEdge(
									exitNode, controlNode);
							exitNode.addForwardEdge(edge);
						}
					}
				}
			}

			// for文の内部が空でない場合
			else {

				{
					final CFGControlEdge edge = new CFGControlEdge(controlNode,
							statementsCFG.getEnterNode(), true);
					controlNode.addForwardEdge(edge);
				}

				// 繰り返し式が空の場合
				if (iteratorsCFG.isEmpty()) {

					for (final CFGNode<?> exitNode : statementsCFG
							.getExitNodes()) {

						// Return文の場合はexitノードに追加
						if (exitNode instanceof CFGReturnStatementNode) {
							this.exitNodes.add(exitNode);
						}

						// continue文の場合
						else if (exitNode instanceof CFGContinueStatementNode) {

							final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
									.getCore();
							final BlockInfo correspondingBlock = continueStatement
									.getCorrespondingBlock();
							// continue文の次に実行されるのが，このwhile文の条件式の時
							if (statement == correspondingBlock) {
								final CFGJumpEdge edge = new CFGJumpEdge(
										exitNode, controlNode);
								exitNode.addForwardEdge(edge);
							}

							// continue文の次に実行されるのが，このwhile文の条件式ではない時
							else {
								this.exitNodes.add(exitNode);
							}

						}

						else {
							final CFGNormalEdge edge = new CFGNormalEdge(
									exitNode, controlNode);
							exitNode.addForwardEdge(edge);
						}
					}
				}

				// 繰り返し式が空でない場合
				else {

					for (final CFGNode<?> exitNode : statementsCFG
							.getExitNodes()) {

						// Return文の場合はexitノードに追加
						if (exitNode instanceof CFGReturnStatementNode) {
							this.exitNodes.add(exitNode);
						}

						// continue文の場合
						else if (exitNode instanceof CFGContinueStatementNode) {

							final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
									.getCore();
							final BlockInfo correspondingBlock = continueStatement
									.getCorrespondingBlock();
							// continue文の次に実行されるのが，このwhile文の条件式の時
							if (statement == correspondingBlock) {
								final CFGJumpEdge edge = new CFGJumpEdge(
										exitNode, controlNode);
								exitNode.addForwardEdge(edge);
							}

							// continue文の次に実行されるのが，このwhile文の条件式ではない時
							else {
								this.exitNodes.add(exitNode);
							}

						}

						else {
							final CFGNormalEdge edge = new CFGNormalEdge(
									exitNode, iteratorsCFG.getEnterNode());
							exitNode.addForwardEdge(edge);
						}
					}

					for (final CFGNode<?> exitNode : iteratorsCFG
							.getExitNodes()) {

						// Return文の場合はexitノードに追加
						if (exitNode instanceof CFGReturnStatementNode) {
							this.exitNodes.add(exitNode);
						}

						// continue文の場合
						else if (exitNode instanceof CFGContinueStatementNode) {

							final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
									.getCore();
							final BlockInfo correspondingBlock = continueStatement
									.getCorrespondingBlock();
							// continue文の次に実行されるのが，このwhile文の条件式の時
							if (statement == correspondingBlock) {
								final CFGJumpEdge edge = new CFGJumpEdge(
										exitNode, controlNode);
								exitNode.addForwardEdge(edge);
							}

							// continue文の次に実行されるのが，このwhile文の条件式ではない時
							else {
								this.exitNodes.add(exitNode);
							}

						}

						else {
							final CFGNormalEdge edge = new CFGNormalEdge(
									exitNode, controlNode);
							exitNode.addForwardEdge(edge);
						}
					}
				}
			}
		}

		else if (statement instanceof ForeachBlockInfo) {

			// foreach文の変数，式からコントロールノードを生成
			final ForeachBlockInfo foreachBlock = (ForeachBlockInfo) statement;
			final ForeachConditionInfo condition = (ForeachConditionInfo) foreachBlock
					.getConditionalClause().getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.enterNode = controlNode;
			this.exitNodes.add(controlNode);
			this.nodes.add(controlNode);

			// foreach文内部の処理
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					foreachBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);

			// 内部が空でない場合は処理を行う
			if (!statementsCFG.isEmpty()) {
				{
					final CFGControlEdge edge = new CFGControlEdge(controlNode,
							statementsCFG.getEnterNode(), true);
					controlNode.addForwardEdge(edge);
				}
				for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {

					// return文の場合はexitノードに追加
					if (exitNode instanceof CFGReturnStatementNode) {
						this.exitNodes.add(exitNode);
					}

					// continue文の場合
					else if (exitNode instanceof CFGContinueStatementNode) {

						final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
								.getCore();
						final BlockInfo correspondingBlock = continueStatement
								.getCorrespondingBlock();

						// continue文のに対応しているのがこのwhile文の時
						if (statement == correspondingBlock) {
							final CFGJumpEdge edge = new CFGJumpEdge(exitNode,
									controlNode);
							exitNode.addForwardEdge(edge);
						}

						// continue文のに対応しているのがこのwhile文ではない時
						else {
							this.exitNodes.add(exitNode);
						}
					}

					else {
						final CFGNormalEdge edge = new CFGNormalEdge(exitNode,
								controlNode);
						exitNode.addForwardEdge(edge);
					}
				}
			}
		}

		// switch文の場合
		else if (statement instanceof SwitchBlockInfo) {

			// switch文の条件式からコントロールノードを生成
			final SwitchBlockInfo switchBlock = (SwitchBlockInfo) statement;
			final ConditionInfo condition = switchBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.enterNode = controlNode;
			this.exitNodes.add(controlNode);
			this.nodes.add(controlNode);

			// 空のCFGを取り除く処理
			final List<IntraProceduralCFG> statementCFGs = new ArrayList<IntraProceduralCFG>();
			for (final StatementInfo innerStatement : switchBlock
					.getStatementsWithoutSubsequencialBlocks()) {
				final IntraProceduralCFG innerStatementCFG = new IntraProceduralCFG(
						innerStatement, nodeFactory);
				this.nodes.addAll(innerStatementCFG.nodes);
				if (!innerStatementCFG.isEmpty()) {
					statementCFGs.add(innerStatementCFG);
				}
			}

			for (int i = 0; i < statementCFGs.size() - 1; i++) {

				final IntraProceduralCFG fromCFG = statementCFGs.get(i);
				final IntraProceduralCFG toCFG = statementCFGs.get(i + 1);

				for (final CFGNode<?> exitNode : fromCFG.getExitNodes()) {

					// Return文であれば，exitノードである
					if (exitNode instanceof CFGReturnStatementNode) {
						this.exitNodes.add(exitNode);
					}

					// 要素数が1であり，そればBreak文であれば，それはswitch文のbreakである
					else if (exitNode instanceof CFGBreakStatementNode
							&& 1 == fromCFG.getAllNodes().size()) {
						this.exitNodes.add(exitNode);
					}

					// それ以外のノードであれば，つなぐ
					else {
						final CFGNormalEdge edge = new CFGNormalEdge(exitNode,
								toCFG.getEnterNode());
						exitNode.addForwardEdge(edge);
					}
				}

				// fromCFGがcase文である場合は，switch文の条件式から依存辺を引く
				if (fromCFG.getElement() instanceof CaseEntryInfo) {
					final CFGControlEdge edge = new CFGControlEdge(controlNode,
							fromCFG.getEnterNode(), true);
					controlNode.addForwardEdge(edge);
				}

				// fromCFGがdefault文である場合は，switch文のexitNodesからcontrolNodeを除く
				if (toCFG.getElement() instanceof DefaultEntryInfo) {
					this.exitNodes.remove(controlNode);
				}
			}

			if (0 < statementCFGs.size()) {
				final IntraProceduralCFG lastCFG = statementCFGs
						.get(statementCFGs.size() - 1);
				this.exitNodes.addAll(lastCFG.getExitNodes());
			} else {
				this.exitNodes.add(controlNode);
			}
		}

		// try文の場合
		else if (statement instanceof TryBlockInfo) {

			final TryBlockInfo tryBlock = (TryBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					tryBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.enterNode = statementsCFG.getEnterNode();
			this.nodes.addAll(statementsCFG.nodes);

			final FinallyBlockInfo finallyBlock = tryBlock
					.getSequentFinallyBlock();
			// finallyブロックがない場合
			if (null == finallyBlock) {
				// try文の最後が，exitノードになりうる
				this.exitNodes.addAll(statementsCFG.getExitNodes());

				// 対応するcatch文のexitノードも，このtry文のexitノードとみなす
				for (final CatchBlockInfo catchBlock : tryBlock
						.getSequentCatchBlocks()) {
					final CFG catchBlockCFG = new IntraProceduralCFG(
							catchBlock, nodeFactory);
					this.exitNodes.addAll(catchBlockCFG.getExitNodes());
					this.nodes.addAll(catchBlockCFG.nodes);
				}
			}

			// finallyブロックがある場合
			else {

				final CFG finallyBlockCFG = new IntraProceduralCFG(
						finallyBlock, nodeFactory);
				this.nodes.addAll(finallyBlockCFG.nodes);

				// finallyブロックが空の場合は，finallyブロックがない場合と同様の処理
				if (finallyBlockCFG.isEmpty()) {

					// try文の最後が，exitノードになりうる
					this.exitNodes.addAll(statementsCFG.getExitNodes());

					// 対応するcatch文のexitノードも，このtry文のexitノードとみなす
					for (final CatchBlockInfo catchBlock : tryBlock
							.getSequentCatchBlocks()) {
						final CFG catchBlockCFG = new IntraProceduralCFG(
								catchBlock, nodeFactory);
						this.exitNodes.addAll(catchBlockCFG.getExitNodes());
						this.nodes.addAll(catchBlockCFG.nodes);
					}
				}

				// finallyブロックが空でない場合は，finallyブロックの最後がtryブロックの出口になる
				else {
					this.exitNodes.addAll(finallyBlockCFG.getExitNodes());

					// try文の内部からつなぐ
					for (final CFGNode<?> exitNode : statementsCFG
							.getExitNodes()) {
						final CFGNormalEdge edge = new CFGNormalEdge(exitNode,
								finallyBlockCFG.getEnterNode());
						exitNode.addForwardEdge(edge);
					}

					// 各catch文からつなぐ
					for (final CatchBlockInfo catchBlock : tryBlock
							.getSequentCatchBlocks()) {
						final CFG catchBlockCFG = new IntraProceduralCFG(
								catchBlock, nodeFactory);
						this.nodes.addAll(catchBlockCFG.nodes);
						for (final CFGNode<?> exitNode : catchBlockCFG
								.getExitNodes()) {
							final CFGNormalEdge edge = new CFGNormalEdge(
									exitNode, finallyBlockCFG.getEnterNode());
							exitNode.addForwardEdge(edge);
						}
					}
				}
			}
		}

		// catch文の場合
		else if (statement instanceof CatchBlockInfo) {

			final CatchBlockInfo catchBlock = (CatchBlockInfo) statement;
			final LocalVariableInfo exception = catchBlock.getCaughtException();
			exception.getDeclarationStatement();
			final VariableDeclarationStatementInfo declarationStatement;
			if (null == exception.getDeclarationStatement()) {
				final int fromLine = exception.getFromLine();
				final int fromColumn = exception.getFromColumn();
				final int toLine = exception.getToLine();
				final int toColumn = exception.getToColumn();
				final LocalVariableUsageInfo exceptionUsage = LocalVariableUsageInfo
						.getInstance(exception, false, true, catchBlock
								.getOwnerMethod(), fromLine, fromColumn,
								toLine, toColumn);
				declarationStatement = new VariableDeclarationStatementInfo(
						catchBlock, exceptionUsage, null, fromLine, fromColumn,
						toLine, toColumn);
			} else {
				declarationStatement = exception.getDeclarationStatement();
			}
			final CFG declarationStatementCFG = new IntraProceduralCFG(
					declarationStatement, nodeFactory);
			this.nodes.addAll(declarationStatementCFG.nodes);
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					catchBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);
			this.enterNode = declarationStatementCFG.getEnterNode();

			// 内部ステートメントが存在する場合は，例外のCFGと内部ステートメントの文をつなぐ
			if (!statementsCFG.isEmpty()) {
				for (final CFGNode<?> exitNode : declarationStatementCFG
						.getExitNodes()) {
					final CFGNormalEdge edge = new CFGNormalEdge(exitNode,
							statementsCFG.getEnterNode());
					exitNode.addForwardEdge(edge);
				}
				this.exitNodes.addAll(statementsCFG.getExitNodes());
			} else {
				this.exitNodes.addAll(declarationStatementCFG.getExitNodes());
			}
		}

		// finally文の場合
		else if (statement instanceof FinallyBlockInfo) {

			final FinallyBlockInfo finallyBlock = (FinallyBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					finallyBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.enterNode = statementsCFG.getEnterNode();
			this.exitNodes.addAll(statementsCFG.getExitNodes());
			this.nodes.addAll(statementsCFG.nodes);
		}

		// simple文の場合
		else if (statement instanceof SimpleBlockInfo) {

			final SimpleBlockInfo simpleBlock = (SimpleBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					simpleBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.enterNode = statementsCFG.getEnterNode();
			this.exitNodes.addAll(statementsCFG.getExitNodes());
			this.nodes.addAll(statementsCFG.nodes);
		}

		// synchorized文の場合
		else if (statement instanceof SynchronizedBlockInfo) {

			final SynchronizedBlockInfo synchronizedBlock = (SynchronizedBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					synchronizedBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.enterNode = statementsCFG.getEnterNode();
			this.exitNodes.addAll(statementsCFG.getExitNodes());
			this.nodes.addAll(statementsCFG.nodes);
		}

		else {
			assert false : "Here shouldn't be reached!";
		}
	}

	/**
	 * CFG構築対象要素を返す
	 * 
	 * @return CFG構築対象要素
	 */
	public Object getElement() {
		return this.element;
	}

	CFGNode<?> getFirstNode(final StatementInfo statement,
			final ICFGNodeFactory nodeFactory) {

		// 単文の場合
		if (statement instanceof SingleStatementInfo) {
			final CFGNormalNode<?> node = nodeFactory.makeNormalNode(statement);
			assert null != node : "node is null!";
			this.nodes.add(node);
			return node;
		}

		// caseエントリの場合
		else if (statement instanceof CaseEntryInfo) {

			final CaseEntryInfo caseEntry = (CaseEntryInfo) statement;
			final CFGNormalNode<?> node = nodeFactory.makeNormalNode(caseEntry);
			assert null != node : "node is null!";
			this.nodes.add(node);
			return node;
		}

		// Labelの場合
		else if (statement instanceof LabelInfo) {
			// 何もしなくていいはず
		}

		// if文の場合
		else if (statement instanceof IfBlockInfo) {

			// if文の条件式からコントロールノードを生成
			final IfBlockInfo ifBlock = (IfBlockInfo) statement;
			final ConditionInfo condition = ifBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.nodes.add(controlNode);
			return controlNode;
		}

		// while文の場合
		else if (statement instanceof WhileBlockInfo) {

			// while文の条件式からコントロールノードを生成
			final WhileBlockInfo whileBlock = (WhileBlockInfo) statement;
			final ConditionInfo condition = whileBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.nodes.add(controlNode);
			return controlNode;
		}

		// else 文の場合
		else if (statement instanceof ElseBlockInfo) {
			// 何もしなくていいはず
		}

		// do文の場合
		else if (statement instanceof DoBlockInfo) {

			final DoBlockInfo doBlock = (DoBlockInfo) statement;
			final SortedSet<StatementInfo> statements = doBlock
					.getStatementsWithoutSubsequencialBlocks();
			final CFGNode<?> firstNode = this.getFirstNode(statements.first(),
					nodeFactory);
			assert null != firstNode : "controlNode is null!";
			return firstNode;
		}

		// for文の場合
		else if (statement instanceof ForBlockInfo) {

			// for文の条件式からコントロールノードを生成
			final ForBlockInfo forBlock = (ForBlockInfo) statement;
			final ConditionInfo condition = forBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null";
			this.nodes.add(controlNode);

			// 初期化式からCFGを生成
			final SortedSet<ConditionInfo> initializers = forBlock
					.getInitializerExpressions();
			final SequentialExpressionsCFG initializersCFG = new SequentialExpressionsCFG(
					initializers, nodeFactory);
			this.nodes.addAll(initializersCFG.nodes);

			if (!initializersCFG.isEmpty()) {
				return initializersCFG.getEnterNode();
			} else {
				return controlNode;
			}
		}

		// switch文の場合
		else if (statement instanceof SwitchBlockInfo) {

			// switch文の条件式からコントロールノードを生成
			final SwitchBlockInfo switchBlock = (SwitchBlockInfo) statement;
			final ConditionInfo condition = switchBlock.getConditionalClause()
					.getCondition();
			final CFGControlNode controlNode = nodeFactory
					.makeControlNode(condition);
			assert null != controlNode : "controlNode is null!";
			this.nodes.add(controlNode);
			return controlNode;
		}

		// try文の場合
		else if (statement instanceof TryBlockInfo) {

			final TryBlockInfo tryBlock = (TryBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					tryBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);
			return statementsCFG.getEnterNode();
		}

		// catch文の場合
		else if (statement instanceof CatchBlockInfo) {

			final CatchBlockInfo catchBlock = (CatchBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					catchBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);
			return statementsCFG.getEnterNode();
		}

		// finally文の場合
		else if (statement instanceof FinallyBlockInfo) {

			final FinallyBlockInfo finallyBlock = (FinallyBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					finallyBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);
			return statementsCFG.getEnterNode();
		}

		// simple文の場合
		else if (statement instanceof SimpleBlockInfo) {

			final SimpleBlockInfo simpleBlock = (SimpleBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					simpleBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);
			return statementsCFG.getEnterNode();
		}

		// synchorized文の場合
		else if (statement instanceof SynchronizedBlockInfo) {

			final SynchronizedBlockInfo synchronizedBlock = (SynchronizedBlockInfo) statement;
			final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
					synchronizedBlock.getStatementsWithoutSubsequencialBlocks(),
					nodeFactory);
			this.nodes.addAll(statementsCFG.nodes);
			return statementsCFG.getEnterNode();
		}

		assert false : "Here shouldn't be reached!";
		return null;
	}

	/**
	 * EmptyExpressionやCaseEntryを削除
	 */
	private void optimizeCFG() {

		final Set<CFGNode<?>> removedNodes = new HashSet<CFGNode<?>>();

		// ノード間の関係を最適化
		for (final CFGNode<?> node : this.getAllNodes()) {

			final boolean removed = node.optimize();
			if (removed) {
				removedNodes.add(node);
			}
		}

		// ノードファクトリを最適化
		for (final CFGNode<?> removedNode : removedNodes) {
			final ExecutableElementInfo core = removedNode.getCore();
			this.nodeFactory.removeNode(core);
			this.nodes.remove(removedNode);
		}
	}

	private void dissolveCFG() {

		// 分解前の全てのノードを取得
		final Set<CFGNode<? extends ExecutableElementInfo>> preAllNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		preAllNodes.addAll(this.getAllNodes());

		// 分解前の全てのノードに対して，分解を実行
		for (final CFGNode<? extends ExecutableElementInfo> node : preAllNodes) {

			// ノードを分解
			final CFG dissolvedCFG = node.dissolve(this.nodeFactory);

			// 分解が行われた場合
			if (null != dissolvedCFG) {

				// 古いノードを取り除く
				this.nodes.remove(node);

				// exitNodeのとき
				if (this.exitNodes.contains(node)) {
					this.exitNodes.remove(node);
					this.exitNodes.addAll(dissolvedCFG.getExitNodes());
				}

				// enterNodeのとき
				if (this.enterNode == node) {
					this.enterNode = dissolvedCFG.getEnterNode();
				}

				// 新しいノードを追加
				this.nodes.addAll(dissolvedCFG.getAllNodes());
			}
		}
	}

	private void packCFG() {

		// 全てのノードを取得
		final Set<CFGNode<? extends ExecutableElementInfo>> allNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		allNodes.addAll(this.getAllNodes());

		for (final CFGNode<? extends ExecutableElementInfo> node : allNodes) {

			if (node instanceof CFGVariableDeclarationStatementNode) {
				final CFGVariableDeclarationStatementNode declarationNode = (CFGVariableDeclarationStatementNode) node;
				final boolean removed = declarationNode
						.removeIfPossible(this.nodeFactory);
				if (removed) {
					this.nodes.remove(node);
					if (node == this.enterNode) {
						this.enterNode = node.getForwardNodes().toArray(
								new CFGNode<?>[0])[0];
					}
				}
			}
		}
	}

	/**
	 * StatementInfoの列からCFGを作成するクラス
	 * 
	 * @author higo
	 * 
	 */
	private class SequentialStatementsCFG extends CFG {

		SequentialStatementsCFG(final SortedSet<StatementInfo> statements,
				final ICFGNodeFactory nodeFactory) {

			super(nodeFactory);

			// 空のCFG, catch文，finally文，else文のCFGを除去する処理
			final List<IntraProceduralCFG> statementCFGs = new ArrayList<IntraProceduralCFG>();
			for (final StatementInfo statement : statements) {

				if (statement instanceof CatchBlockInfo
						|| statement instanceof FinallyBlockInfo
						|| statement instanceof ElseBlockInfo) {
					continue;
				}

				final IntraProceduralCFG statementCFG = new IntraProceduralCFG(
						statement, nodeFactory);
				this.nodes.addAll(statementCFG.nodes);
				if (statementCFG.isEmpty()) {
					continue;
				}

				statementCFGs.add(statementCFG);
			}

			if (0 == statementCFGs.size()) {
				return;
			}

			// 最初の文からenterノードを生成
			{
				this.enterNode = statementCFGs.get(0).getEnterNode();
			}

			// 最後の文からexitノードを生成
			{

				// break文でなければexitノードに追加
				final StatementInfo lastStatement = statements.last();
				if (!(lastStatement instanceof BreakStatementInfo)) {
					final int lastIndex = statementCFGs.size() - 1;
					this.exitNodes.addAll(statementCFGs.get(lastIndex)
							.getExitNodes());
				}
			}

			// statementsから生成したCFGを順番につないでいく
			{
				for (int i = 0; i < statementCFGs.size() - 1; i++) {
					final IntraProceduralCFG fromCFG = statementCFGs.get(i);
					final IntraProceduralCFG toCFG = statementCFGs.get(i + 1);

					for (final CFGNode<?> exitNode : fromCFG.getExitNodes()) {

						// Return文の場合はexitNodesに追加
						if (exitNode instanceof CFGReturnStatementNode) {
							this.exitNodes.add(exitNode);
						}

						// continue文の場合
						else if (exitNode instanceof CFGContinueStatementNode) {

							final ContinueStatementInfo continueStatement = (ContinueStatementInfo) exitNode
									.getCore();
							final BlockInfo correspondingBlock = continueStatement
									.getCorrespondingBlock();
							final SortedSet<StatementInfo> innerStatements = LocalSpaceInfo
									.getAllStatements(correspondingBlock);

							final StatementInfo statement = (StatementInfo) toCFG
									.getElement();

							// statement が innerStatements
							// に含まれている場合は，continue文の支配下にある
							if (innerStatements.contains(statement)) {
								this.exitNodes.add(exitNode);
							} else {
								final CFGNormalEdge edge = new CFGNormalEdge(
										exitNode, toCFG.getEnterNode());
								exitNode.addForwardEdge(edge);
							}
						}

						// controlNodeの場合
						else if (exitNode instanceof CFGControlNode) {
							final CFGControlEdge edge = new CFGControlEdge(
									exitNode, toCFG.getEnterNode(), false);
							exitNode.addForwardEdge(edge);
						}

						else {
							final CFGNormalEdge edge = new CFGNormalEdge(
									exitNode, toCFG.getEnterNode());
							exitNode.addForwardEdge(edge);
						}
					}
				}
			}
		}
	}

	/**
	 * ExpressionInfoの列，またはCOnditionInfoの列からCFGを作成するクラス
	 * 
	 * @author higo
	 * 
	 */
	private class SequentialExpressionsCFG extends CFG {

		SequentialExpressionsCFG(
				final SortedSet<? extends ConditionInfo> expressions,
				final ICFGNodeFactory nodeFactory) {

			super(nodeFactory);

			if (0 == expressions.size()) {
				return;
			}

			// 最初の式からenterノードを生成
			{
				final ConditionInfo firstExpression = expressions.first();
				final CFGNormalNode<?> firstExpressionNode = nodeFactory
						.makeNormalNode(firstExpression);
				this.enterNode = firstExpressionNode;
				this.nodes.add(firstExpressionNode);
			}

			// 最後の式からexitノードを生成
			{
				final ConditionInfo lastExpression = expressions.last();
				final CFGNormalNode<?> lastExpressionNode = nodeFactory
						.makeNormalNode(lastExpression);
				this.exitNodes.add(lastExpressionNode);
				this.nodes.add(lastExpressionNode);
			}

			// expressions から生成したノードを順番につないでいく
			final ConditionInfo[] expressionArray = expressions
					.toArray(new ConditionInfo[0]);
			for (int i = 0; i < expressionArray.length - 1; i++) {
				final CFGNormalNode<?> fromNode = nodeFactory
						.makeNormalNode(expressionArray[i]);
				this.nodes.add(fromNode);
				final CFGNormalNode<?> toNode = nodeFactory
						.makeNormalNode(expressionArray[i + 1]);
				this.nodes.add(toNode);
				final CFGNormalEdge edge = new CFGNormalEdge(fromNode, toNode);
				fromNode.addForwardEdge(edge);
			}
		}
	}

	private static final ConcurrentMap<StatementInfo, CFG> statementCFG = new ConcurrentHashMap<StatementInfo, CFG>();

	static synchronized CFG getCFG(final StatementInfo statement,
			final ICFGNodeFactory nodeFactory) {

		CFG cfg = statementCFG.get(statement);
		if (null == cfg) {
			throw new IllegalStateException();
		}
		return cfg;
	}
}
