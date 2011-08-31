package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

public class CFGVariableDeclarationStatementNode extends
		CFGStatementNode<VariableDeclarationStatementInfo> {

	CFGVariableDeclarationStatementNode(
			final VariableDeclarationStatementInfo statement) {
		super(statement);
	}

	@Override
	ExpressionInfo getDissolvingTarget() {
		final VariableDeclarationStatementInfo statement = this.getCore();
		if (statement.isInitialized()) {
			return (ExpressionInfo) statement.getInitializationExpression();
		} else {
			return null;
		}
	}

	@Override
	VariableDeclarationStatementInfo makeNewElement(
			final LocalSpaceInfo ownerSpace, final int fromLine,
			final int fromColumn, final int toLine, final int toColumn,
			final ExpressionInfo... requiredExpression) {

		if (1 != requiredExpression.length) {
			throw new IllegalArgumentException();
		}

		final VariableDeclarationStatementInfo statement = this.getCore();
		final LocalVariableUsageInfo variableDeclaration = (LocalVariableUsageInfo) statement
				.getDeclaration().copy();

		final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
				ownerSpace, variableDeclaration, requiredExpression[0],
				fromLine, fromColumn, toLine, toColumn);
		return newStatement;
	}

	@Override
	VariableDeclarationStatementInfo makeNewElement(
			final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpression) {

		if (1 != requiredExpression.length) {
			throw new IllegalArgumentException();
		}

		final VariableDeclarationStatementInfo statement = this.getCore();
		final int fromLine = statement.getFromLine();
		final int fromColumn = statement.getFromColumn();
		final int toLine = statement.getToLine();
		final int toColumn = statement.getToColumn();

		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpression);
	}

	public boolean removeIfPossible(final ICFGNodeFactory nodeFactory) {

		// 右辺がメソッド呼び出しだった場合は集約せずにこのメソッドを抜ける
		final VariableDeclarationStatementInfo statement = this.getCore();
		final LocalVariableInfo variable = statement.getDeclaration()
				.getUsedVariable();
		final ExpressionInfo initializer = statement
				.getInitializationExpression();
		if (null != initializer && initializer instanceof MethodCallInfo) {
			return false;
		}

		final Set<CFGNode<?>> referenceNodes = new HashSet<CFGNode<?>>();
		final Set<CFGNode<?>> checkedNodes = new HashSet<CFGNode<?>>();
		searchNodes(this, variable, referenceNodes, checkedNodes);

		// 参照しているノードが1つのとき，そのノードにこのノードを組み込む
		if (1 == referenceNodes.size()) {

			// このノードを削除し，バックワードノードとフォワードノードを直接つなぐ
			nodeFactory.removeNode(statement);
			this.remove();
			for (final CFGNode<?> backwardNode : this.getBackwardNodes()) {
				for (final CFGNode<?> forwardNode : this.getForwardNodes()) {
					// TODO 本来はEdgeの種類を調べるべきである
					final CFGEdge edge = new CFGNormalEdge(backwardNode,
							forwardNode);
					backwardNode.addForwardEdge(edge);
					forwardNode.addBackwardEdge(edge);
				}
			}

			final CFGNode<? extends ExecutableElementInfo> referenceNode = referenceNodes
					.toArray(new CFGNode<?>[0])[0];
			final ExecutableElementInfo element = (ExecutableElementInfo) referenceNode
					.getCore();
			final ExecutableElementInfo newElement = createPackedElement(
					element, variable, initializer);
			referenceNode.setCore(newElement);

			return true;
		}

		return false;
	}

	private static ExecutableElementInfo createPackedElement(
			final ExecutableElementInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		if (null == element) {
			return null;
		}

		// MethodCallInfoのときは集約してはいけないので，そのまま返す
		// removeIfPossibleでちゃんとはじいているのであれば，この条件はいらないはず
		else if (expression instanceof MethodCallInfo) {
			return element;
		}

		else if (element instanceof AssertStatementInfo) {
			return createPackedAssertStatement((AssertStatementInfo) element,
					variable, expression);
		}

		else if (element instanceof JumpStatementInfo) {
			return createPackedJumpStatement((JumpStatementInfo) element,
					variable, expression);
		}

		else if (element instanceof ExpressionStatementInfo) {
			return createPackedExpressionStatement(
					(ExpressionStatementInfo) element, variable, expression);
		}

		else if (element instanceof ReturnStatementInfo) {
			return createPackedReturnStatement((ReturnStatementInfo) element,
					variable, expression);
		}

		else if (element instanceof ThrowStatementInfo) {
			return createPackedThrowStatement((ThrowStatementInfo) element,
					variable, expression);
		}

		else if (element instanceof VariableDeclarationStatementInfo) {
			return createPackedVariableDeclarationStatement(
					(VariableDeclarationStatementInfo) element, variable,
					expression);
		}

		else if (element instanceof ArrayElementUsageInfo) {
			return createPackedArrayElementUsage(
					(ArrayElementUsageInfo) element, variable, expression);
		}

		else if (element instanceof ArrayInitializerInfo) {
			return createPackedArrayInitializer((ArrayInitializerInfo) element,
					variable, expression);
		}

		else if (element instanceof ArrayTypeReferenceInfo) {
			return createPackedArrayTypeReference(
					(ArrayTypeReferenceInfo) element, variable, expression);
		}

		else if (element instanceof BinominalOperationInfo) {
			return createPackedBinominalOperation(
					(BinominalOperationInfo) element, variable, expression);
		}

		else if (element instanceof ArrayConstructorCallInfo) {
			return createPackedArrayConstructorCall(
					(ArrayConstructorCallInfo) element, variable, expression);
		}

		else if (element instanceof ClassConstructorCallInfo) {
			return createPackedClassConstructorCall(
					(ClassConstructorCallInfo) element, variable, expression);
		}

		else if (element instanceof MethodCallInfo) {
			return createPackedMethodCall((MethodCallInfo) element, variable,
					expression);
		}

		else if (element instanceof CastUsageInfo) {
			return createPackedCastUsage((CastUsageInfo) element, variable,
					expression);
		}

		else if (element instanceof ClassReferenceInfo) {
			return createPackedClassReference((ClassReferenceInfo) element,
					variable, expression);
		}

		else if (element instanceof EmptyExpressionInfo) {
			return createPackedEmptyExpression((EmptyExpressionInfo) element,
					variable, expression);
		}

		else if (element instanceof ForeachConditionInfo) {
			return createPackedForeachCondition((ForeachConditionInfo) element,
					variable, expression);
		}

		else if (element instanceof LiteralUsageInfo) {
			return createPackedLiteralUsage((LiteralUsageInfo) element,
					variable, expression);
		}

		else if (element instanceof MonominalOperationInfo) {
			return createPackedMonominalOperation(
					(MonominalOperationInfo) element, variable, expression);
		}

		else if (element instanceof NullUsageInfo) {
			return createPackedNullUsage((NullUsageInfo) element, variable,
					expression);
		}

		else if (element instanceof ParenthesesExpressionInfo) {
			return createPackedParenthesesExpression(
					(ParenthesesExpressionInfo) element, variable, expression);
		}

		else if (element instanceof TernaryOperationInfo) {
			return createPackedTernaryOperation((TernaryOperationInfo) element,
					variable, expression);
		}

		else if (element instanceof UnknownEntityUsageInfo) {
			return createPackedUnknownEntityUsage(
					(UnknownEntityUsageInfo) element, variable, expression);
		}

		else if (element instanceof VariableUsageInfo<?>) {
			return createPackedVariableUsage((VariableUsageInfo<?>) element,
					variable, expression);
		}

		else {
			assert false : "Here shouldn't be reached!";
			return null;
		}
	}

	private static AssertStatementInfo createPackedAssertStatement(
			final AssertStatementInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo assertedExpression = element
				.getAssertedExpression();
		final ExpressionInfo messageExpression = element.getMessageExpression();

		final ExpressionInfo newAssertExpression = (ExpressionInfo) createPackedElement(
				assertedExpression, variable, expression);
		final ExpressionInfo newMessageExpression = (ExpressionInfo) createPackedElement(
				messageExpression, variable, expression);

		return new AssertStatementInfo(element.getOwnerSpace(),
				newAssertExpression, newMessageExpression, element
						.getFromLine(), element.getFromColumn(), element
						.getToLine(), element.getToColumn());
	}

	private static JumpStatementInfo createPackedJumpStatement(
			final JumpStatementInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		return element;
	}

	private static ExpressionStatementInfo createPackedExpressionStatement(
			final ExpressionStatementInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo content = element.getExpression();
		final ExpressionInfo newContent = (ExpressionInfo) createPackedElement(
				content, variable, expression);

		return new ExpressionStatementInfo(element.getOwnerSpace(), newContent,
				element.getFromLine(), element.getFromColumn(), element
						.getToLine(), element.getToColumn());
	}

	private static ReturnStatementInfo createPackedReturnStatement(
			final ReturnStatementInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		// returnステートメントはオペランドが変数でなければならないので，なにもしない
		return element;
	}

	private static ThrowStatementInfo createPackedThrowStatement(
			final ThrowStatementInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo thrownExpression = element.getThrownExpression();
		final ExpressionInfo newThrownExpression = (ExpressionInfo) createPackedElement(
				thrownExpression, variable, expression);

		return new ThrowStatementInfo(element.getOwnerSpace(),
				newThrownExpression, element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn());
	}

	private static VariableDeclarationStatementInfo createPackedVariableDeclarationStatement(
			final VariableDeclarationStatementInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo initializerExpression = element
				.getInitializationExpression();
		final ExpressionInfo newInitializerExpression = (ExpressionInfo) createPackedElement(
				initializerExpression, variable, expression);

		return new VariableDeclarationStatementInfo(element.getOwnerSpace(),
				element.getDeclaration(), newInitializerExpression, element
						.getFromLine(), element.getFromColumn(), element
						.getToLine(), element.getToColumn());
	}

	private static ArrayElementUsageInfo createPackedArrayElementUsage(
			final ArrayElementUsageInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo indexExpression = element.getIndexExpression();
		final ExpressionInfo qualifierExpression = element
				.getQualifierExpression();

		final ExpressionInfo newIndexExpression = (ExpressionInfo) createPackedElement(
				indexExpression, variable, expression);
		final ExpressionInfo newQualifierExpression = (ExpressionInfo) createPackedElement(
				qualifierExpression, variable, expression);

		final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
				newIndexExpression, newQualifierExpression, element
						.getOwnerMethod(), element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn());
		newArrayElementUsage.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newArrayElementUsage.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newArrayElementUsage;
	}

	private static ArrayInitializerInfo createPackedArrayInitializer(
			final ArrayInitializerInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final List<ExpressionInfo> newElementInitializers = new ArrayList<ExpressionInfo>();
		for (final ExpressionInfo elementInitializer : element
				.getElementInitializers()) {
			final ExpressionInfo newElementInitializer = (ExpressionInfo) createPackedElement(
					elementInitializer, variable, expression);
			newElementInitializers.add(newElementInitializer);
		}

		final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(
				newElementInitializers, element.getOwnerMethod(), element
						.getFromLine(), element.getFromColumn(), element
						.getToLine(), element.getToColumn());
		newArrayInitializer.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newArrayInitializer.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newArrayInitializer;
	}

	private static ArrayTypeReferenceInfo createPackedArrayTypeReference(
			final ArrayTypeReferenceInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		return element;
	}

	private static BinominalOperationInfo createPackedBinominalOperation(
			final BinominalOperationInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo firstOperand = element.getFirstOperand();
		final ExpressionInfo secondOperand = element.getSecondOperand();

		final ExpressionInfo newFirstOperand = (ExpressionInfo) createPackedElement(
				firstOperand, variable, expression);
		final ExpressionInfo newSecondOperand = (ExpressionInfo) createPackedElement(
				secondOperand, variable, expression);

		final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
				element.getOperator(), newFirstOperand, newSecondOperand,
				element.getOwnerMethod(), element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn());
		newBinominalOperation.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newBinominalOperation.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newBinominalOperation;
	}

	private static ArrayConstructorCallInfo createPackedArrayConstructorCall(
			final ArrayConstructorCallInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ArrayConstructorCallInfo newArrayConstructorCall = new ArrayConstructorCallInfo(
				element.getType(), element.getOwnerMethod(), element
						.getFromLine(), element.getFromColumn(), element
						.getToLine(), element.getToColumn());
		newArrayConstructorCall.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newArrayConstructorCall.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newArrayConstructorCall;
	}

	private static ClassConstructorCallInfo createPackedClassConstructorCall(
			final ClassConstructorCallInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ClassConstructorCallInfo newConstructor = new ClassConstructorCallInfo(
				element.getType(), element.getCallee(), element
						.getOwnerMethod(), element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn());

		for (final ExpressionInfo argument : element.getArguments()) {
			final ExpressionInfo newArgument = (ExpressionInfo) createPackedElement(
					argument, variable, expression);
			newConstructor.addArgument(newArgument);
		}

		for (final ReferenceTypeInfo typeArgument : element.getTypeArguments()) {
			newConstructor.addTypeArgument(typeArgument);
		}

		newConstructor.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newConstructor.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newConstructor;
	}

	private static MethodCallInfo createPackedMethodCall(
			final MethodCallInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo qualifierExpression = element
				.getQualifierExpression();
		final ExpressionInfo newQualifierExpression = (ExpressionInfo) createPackedElement(
				qualifierExpression, variable, expression);

		final MethodCallInfo newMethodCall = new MethodCallInfo(
				newQualifierExpression.getType(), newQualifierExpression,
				element.getCallee(), element.getType(), element
						.getOwnerMethod(), element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn());

		for (final ExpressionInfo argument : element.getArguments()) {
			final ExpressionInfo newArgument = (ExpressionInfo) createPackedElement(
					argument, variable, expression);
			newMethodCall.addArgument(newArgument);
		}

		for (final ReferenceTypeInfo typeArgument : element.getTypeArguments()) {
			newMethodCall.addTypeArgument(typeArgument);
		}

		newMethodCall.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newMethodCall.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newMethodCall;
	}

	private static CastUsageInfo createPackedCastUsage(
			final CastUsageInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo castedUsage = element.getCastedUsage();
		final ExpressionInfo newCastedUsage = (ExpressionInfo) createPackedElement(
				castedUsage, variable, expression);

		final CastUsageInfo newCastUsage = new CastUsageInfo(element.getType(),
				newCastedUsage, element.getOwnerMethod(),
				element.getFromLine(), element.getFromColumn(), element
						.getToLine(), element.getToColumn());
		newCastUsage.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newCastUsage.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newCastUsage;
	}

	private static ClassReferenceInfo createPackedClassReference(
			final ClassReferenceInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		return element;
	}

	private static EmptyExpressionInfo createPackedEmptyExpression(
			final EmptyExpressionInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		return element;
	}

	private static ForeachConditionInfo createPackedForeachCondition(
			final ForeachConditionInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo iteratorExpression = element
				.getIteratorExpression();
		final ExpressionInfo newIteratorExpression = (ExpressionInfo) createPackedElement(
				iteratorExpression, variable, expression);

		final ForeachConditionInfo newForeachCondition = new ForeachConditionInfo(
				element.getOwnerMethod(), element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn(), element.getIteratorVariable(),
				newIteratorExpression);
		newForeachCondition.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newForeachCondition.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newForeachCondition;
	}

	private static LiteralUsageInfo createPackedLiteralUsage(
			final LiteralUsageInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		return element;
	}

	private static MonominalOperationInfo createPackedMonominalOperation(
			final MonominalOperationInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo operand = element.getOperand();
		final ExpressionInfo newOperand = (ExpressionInfo) createPackedElement(
				operand, variable, expression);

		final MonominalOperationInfo newMonominalOperation = new MonominalOperationInfo(
				newOperand, element.getOperator(), element.isPreposed(),
				element.getOwnerMethod(), element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn());
		newMonominalOperation.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newMonominalOperation.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newMonominalOperation;
	}

	private static NullUsageInfo createPackedNullUsage(
			final NullUsageInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		return element;
	}

	private static ParenthesesExpressionInfo createPackedParenthesesExpression(
			final ParenthesesExpressionInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo parentheticExpression = element
				.getParnentheticExpression();
		final ExpressionInfo newParentheticExpression = (ExpressionInfo) createPackedElement(
				parentheticExpression, variable, expression);

		final ParenthesesExpressionInfo newParenthesesExpression = new ParenthesesExpressionInfo(
				newParentheticExpression, element.getOwnerMethod(), element
						.getFromLine(), element.getFromColumn(), element
						.getToLine(), element.getToColumn());
		newParenthesesExpression.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newParenthesesExpression.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newParenthesesExpression;
	}

	private static TernaryOperationInfo createPackedTernaryOperation(
			final TernaryOperationInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final ExpressionInfo condition = (ExpressionInfo) element
				.getCondition();
		final ExpressionInfo trueExpression = element.getTrueExpression();
		final ExpressionInfo falseExpression = element.getFalseExpression();

		final ExpressionInfo newCondition = (ExpressionInfo) createPackedElement(
				condition, variable, expression);
		final ExpressionInfo newTrueExpression = (ExpressionInfo) createPackedElement(
				trueExpression, variable, expression);
		final ExpressionInfo newFalseExpression = (ExpressionInfo) createPackedElement(
				falseExpression, variable, expression);

		final TernaryOperationInfo newTernaryOperation = new TernaryOperationInfo(
				newCondition, newTrueExpression, newFalseExpression, element
						.getOwnerMethod(), element.getFromLine(), element
						.getFromColumn(), element.getToLine(), element
						.getToColumn());
		newTernaryOperation.setOwnerConditionalBlock(element
				.getOwnerConditionalBlock());
		newTernaryOperation.setOwnerExecutableElement(element
				.getOwnerExecutableElement());
		return newTernaryOperation;
	}

	private static UnknownEntityUsageInfo createPackedUnknownEntityUsage(
			final UnknownEntityUsageInfo element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		return element;
	}

	private static ExpressionInfo createPackedVariableUsage(
			final VariableUsageInfo<?> element,
			final VariableInfo<? extends UnitInfo> variable,
			final ExpressionInfo expression) {

		final VariableInfo<?> usedVariable = element.getUsedVariable();
		if (variable.equals(usedVariable)) {
			expression.setOwnerConditionalBlock(element
					.getOwnerConditionalBlock());
			expression.setOwnerExecutableElement(element
					.getOwnerExecutableElement());
			return expression;
		} else {
			return element;
		}
	}

	/**
	 * 第二引数で与えられた変数を使用しているCFGノードを第三引数に追加する．
	 * 
	 * @param variable
	 * @param nodes
	 */
	private static void searchNodes(final CFGNode<?> node,
			final VariableInfo<? extends UnitInfo> variable,
			final Set<CFGNode<?>> nodes, final Set<CFGNode<?>> checkedNodes) {

		if (checkedNodes.contains(node)) {
			return;
		} else {
			checkedNodes.add(node);
		}

		if (node.getReferencedVariables().contains(variable)) {
			nodes.add(node);
		}

		for (final CFGNode<? extends ExecutableElementInfo> forwardNode : node
				.getForwardNodes()) {
			searchNodes(forwardNode, variable, nodes, checkedNodes);
		}
	}
}
