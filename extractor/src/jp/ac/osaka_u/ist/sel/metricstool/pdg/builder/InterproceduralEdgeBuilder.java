package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;

import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.InterProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class InterproceduralEdgeBuilder {
/*
	public InterproceduralEdgeBuilder(final InterProceduralPDG pdg) {
		this.pdg = pdg;
	}

	public void addEdges() {

		final CallableUnitInfo unit = this.pdg.getMethodInfo();
		for (final StatementInfo statement : unit.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final StatementInfo statement) {

		if (statement instanceof CatchBlockInfo) {
			this.addEdges((CatchBlockInfo) statement);
		} else if (statement instanceof DoBlockInfo) {
			this.addEdges((DoBlockInfo) statement);
		} else if (statement instanceof ElseBlockInfo) {
			this.addEdges((ElseBlockInfo) statement);
		} else if (statement instanceof FinallyBlockInfo) {
			this.addEdges((FinallyBlockInfo) statement);
		} else if (statement instanceof ForBlockInfo) {
			this.addEdges((ForBlockInfo) statement);
		} else if (statement instanceof ForeachBlockInfo) {
			this.addEdges((ForeachBlockInfo) statement);
		} else if (statement instanceof IfBlockInfo) {
			this.addEdges((IfBlockInfo) statement);
		} else if (statement instanceof SimpleBlockInfo) {
			this.addEdges((SimpleBlockInfo) statement);
		} else if (statement instanceof SwitchBlockInfo) {
			this.addEdges((SwitchBlockInfo) statement);
		} else if (statement instanceof SynchronizedBlockInfo) {
			this.addEdges((SynchronizedBlockInfo) statement);
		} else if (statement instanceof TryBlockInfo) {
			this.addEdges((TryBlockInfo) statement);
		} else if (statement instanceof WhileBlockInfo) {
			this.addEdges((WhileBlockInfo) statement);
		} else if (statement instanceof CaseEntryInfo) {
			this.addEdges((CaseEntryInfo) statement);
		} else if (statement instanceof LabelInfo) {
			this.addEdges((LabelInfo) statement);
		} else if (statement instanceof SingleStatementInfo) {
			this.addEdges((SingleStatementInfo) statement);
		} else if (statement instanceof ConditionInfo) {
			this.addEdges((ConditionInfo) statement);
		} else {
			throw new IllegalStateException();
		}
	}

	private void addEdges(final CatchBlockInfo catchBlock) {
		for (final StatementInfo statement : catchBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final DoBlockInfo doBlock) {
		for (final StatementInfo statement : doBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final ElseBlockInfo elseBlock) {
		for (final StatementInfo statement : elseBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final FinallyBlockInfo finallyBlock) {
		for (final StatementInfo statement : finallyBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final ForBlockInfo forBlock) {
		for (final ConditionInfo expression : forBlock
				.getInitializerExpressions()) {
			this.addEdges(expression);
		}
		this.addEdges(forBlock.getConditionalClause().getCondition());
		for (final ExpressionInfo expression : forBlock
				.getIteratorExpressions()) {
			this.addEdges(expression);
		}
		for (final StatementInfo statement : forBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final ForeachBlockInfo foreachBlock) {
		this.addEdges(foreachBlock.getConditionalClause().getCondition());
		for (final StatementInfo statement : foreachBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final IfBlockInfo ifBlock) {
		this.addEdges(ifBlock.getConditionalClause().getCondition());
		for (final StatementInfo statement : ifBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final SimpleBlockInfo simpleBlock) {
		for (final StatementInfo statement : simpleBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final SwitchBlockInfo switchBlock) {
		this.addEdges(switchBlock.getConditionalClause().getCondition());
		for (final StatementInfo statement : switchBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final SynchronizedBlockInfo synchronizedBlock) {
		this.addEdges(synchronizedBlock.getSynchronizedExpression());
		for (final StatementInfo statement : synchronizedBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final TryBlockInfo tryBlock) {
		for (final StatementInfo statement : tryBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final WhileBlockInfo whileBlock) {
		this.addEdges(whileBlock.getConditionalClause().getCondition());
		for (final StatementInfo statement : whileBlock.getStatements()) {
			this.addEdges(statement);
		}
	}

	private void addEdges(final CaseEntryInfo caseEntry) {
	}

	private void addEdges(final LabelInfo label) {
	}

	private void addEdges(final SingleStatementInfo statement) {
		for (final CallInfo<?> call : statement.getCalls()) {
			final CallableUnitInfo callee = call.getCallee();
			final Set<CallableUnitInfo> callees = new HashSet<CallableUnitInfo>();
			callees.add(callee);
			if (callee instanceof MethodInfo) {
				callees.addAll(((MethodInfo) callee).getOverriders());
			}
			for (final CallableUnitInfo unit : callees) {
				if (unit instanceof TargetMethodInfo
						|| unit instanceof TargetConstructorInfo) {
					if (((TargetClassInfo) unit.getOwnerClass()).isInterface()) {
						continue;
					}
					final InterProceduralPDG pdg = InterProceduralPDG.PDG_MAP
							.get(unit);
					assert null != pdg : "Illegal State!";

					// Call DependenceÅ@Çí«â¡
					{
						final PDGNode<?> fromNode = this.pdg.getNodeFactory()
								.getNode(statement);
						final PDGNode<?> toNode = pdg.getMethodEnterNode();
						fromNode.addCallDependingNode(toNode, call);
					}

					// Return Dependence Çí«â¡
					{
						final PDGNode<?> toNode = this.pdg.getNodeFactory()
								.getNode(statement);
						for (final PDGNode<?> fromNode : pdg.getExitNodes()) {
							fromNode.addReturnDependingNode(toNode);
						}
					}
				}
			}

		}
	}

	private void addEdges(final ConditionInfo condition) {
		for (final CallInfo<?> call : condition.getCalls()) {
			final CallableUnitInfo callee = call.getCallee();
			final Set<CallableUnitInfo> callees = new HashSet<CallableUnitInfo>();
			callees.add(callee);
			if (callee instanceof MethodInfo) {
				callees.addAll(((MethodInfo) callee).getOverriders());
			}
			for (final CallableUnitInfo unit : callees) {
				if (unit instanceof TargetMethodInfo
						|| unit instanceof TargetConstructorInfo) {
					if (((TargetClassInfo) unit.getOwnerClass()).isInterface()) {
						continue;
					}
					final InterProceduralPDG pdg = InterProceduralPDG.PDG_MAP
							.get(unit);
					assert null != pdg : "Illegal State!";

					// Call DependenceÅ@Çí«â¡
					{
						final PDGNode<?> fromNode = this.pdg.getNodeFactory()
								.getNode(condition);
						final PDGNode<?> toNode = pdg.getMethodEnterNode();
						fromNode.addCallDependingNode(toNode, call);
					}

					// Return Dependence Çí«â¡
					{
						final PDGNode<?> toNode = this.pdg.getNodeFactory()
								.getNode(condition);
						for (final PDGNode<?> fromNode : pdg.getExitNodes()) {
							fromNode.addReturnDependingNode(toNode);
						}
					}
				}
			}
		}
	}

	private final InterProceduralPDG pdg;*/
}
