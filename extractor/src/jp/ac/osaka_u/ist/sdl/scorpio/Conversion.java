package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scorpio.settings.CALL_NORMALIZATION;
import jp.ac.osaka_u.ist.sdl.scorpio.settings.Configuration;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode.PseudoConditionInfo;

/**
 * プログラム要素の文字列変換をするためのクラス
 * 
 * @author higo
 * 
 */
public class Conversion {

	/**
	 * オブジェクト型を変換するためのメソッド
	 * 
	 * @param o
	 * @return
	 */
	public static String getNormalizedElement(final Object o) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(o);
		if (null != converted) {
			return converted;
		}

		if (o instanceof SingleStatementInfo) {
			converted = getNormalizedStatement((SingleStatementInfo) o);
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;

		} else if (o instanceof ExpressionInfo) {
			converted = getNormalizedExpression((ExpressionInfo) o);
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;

		} else if (o instanceof ConditionInfo) {
			converted = getNormalizedCondition((ConditionInfo) o);
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;

		} else if (o instanceof CaseEntryInfo) {

			final StringBuilder text = new StringBuilder();
			if (o instanceof DefaultEntryInfo) {
				text.append("default:");
			} else {
				text.append("case ");

				final ExpressionInfo label = ((CaseEntryInfo) o).getLabel();
				final String labelString = getNormalizedExpression(label);
				text.append(labelString);

				text.append(":");
			}

			converted = text.toString();
			ORIGINAL_TO_CONVERTED_MAP.put(o, converted);
			return converted;
		}

		assert false : "Here shouldn't be reached!";
		return null;
	}

	/**
	 * 単文を変換するためのメソッド
	 * 
	 * @param statement
	 * @return
	 */
	public static String getNormalizedStatement(
			final SingleStatementInfo statement) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(statement);
		if (null != converted) {
			return converted;
		}

		final StringBuilder text = new StringBuilder();

		if (statement instanceof AssertStatementInfo) {

			text.append("assert ");

			final ExpressionInfo expression = ((AssertStatementInfo) statement)
					.getAssertedExpression();
			final String expressionString = Conversion
					.getNormalizedExpression(expression);
			text.append(expressionString);

			text.append(";");

		} else if (statement instanceof BreakStatementInfo) {

			text.append("break;");

		} else if (statement instanceof ContinueStatementInfo) {

			text.append("continue;");

		} else if (statement instanceof ExpressionStatementInfo) {

			final ExpressionInfo expression = ((ExpressionStatementInfo) statement)
					.getExpression();
			text.append(Conversion.getNormalizedExpression(expression));

			text.append(";");

		} else if (statement instanceof ReturnStatementInfo) {

			text.append("return ");

			final ExpressionInfo expression = ((ReturnStatementInfo) statement)
					.getReturnedExpression();
			final String expressionString = Conversion
					.getNormalizedExpression(expression);
			text.append(expressionString);

			text.append(";");

		} else if (statement instanceof ThrowStatementInfo) {

			text.append("throw ");

			final ExpressionInfo expression = ((ThrowStatementInfo) statement)
					.getThrownExpression();
			final String expressionString = Conversion
					.getNormalizedExpression(expression);
			text.append(expressionString);

			text.append(";");

		} else if (statement instanceof VariableDeclarationStatementInfo) {

			text.append(Conversion
					.getNormalizedCondition((ConditionInfo) statement));

			text.append(";");

		} else {
			assert false : "Here shouldn't be reached!";
		}

		converted = text.toString();
		ORIGINAL_TO_CONVERTED_MAP.put(statement, converted);
		return converted;
	}

	/**
	 * 条件式を変換するためのメソッド
	 * 
	 * @param condition
	 * @return
	 */
	public static String getNormalizedCondition(final ConditionInfo condition) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(condition);
		if (null != converted) {
			return converted;
		}

		if (condition instanceof VariableDeclarationStatementInfo) {

			final StringBuilder text = new StringBuilder();
			final VariableDeclarationStatementInfo declarationStatement = (VariableDeclarationStatementInfo) condition;
			final LocalVariableInfo variable = declarationStatement
					.getDeclaredLocalVariable();
			final TypeInfo variableType = variable.getType();

			switch (Configuration.INSTANCE.getPV()) {
			case NO: // 正規化レベル0，変数名をそのまま使う
				text.append(variable.getType().getTypeName());
				text.append(" ");
				text.append(variable.getName());
				break;

			case TYPE: // 正規化レベル1，変数は型名に正規化する．変数名が異なっていても，型が同じであれば，クローンとして検出する
				text.append(variableType.getTypeName());
				break;

			case ALL: // 正規化レベル2，全ての変数を同一字句に正規化する．
				text.append("TOKEN");
				break;

			default:
				assert false : "Here shouldn't be reached!";
			}

			if (declarationStatement.isInitialized()) {

				text.append("=");
				final ExpressionInfo expression = declarationStatement
						.getInitializationExpression();
				final String expressionString = Conversion
						.getNormalizedExpression(expression);
				text.append(expressionString);
			}

			converted = text.toString();
			ORIGINAL_TO_CONVERTED_MAP.put(condition, converted);
			return converted;

		} else if (condition instanceof ExpressionInfo) {

			converted = Conversion
					.getNormalizedExpression((ExpressionInfo) condition);
			ORIGINAL_TO_CONVERTED_MAP.put(condition, converted);
			return converted;

		} else if (condition instanceof PseudoConditionInfo) {

			converted = "PseudoConditionInfo";
			ORIGINAL_TO_CONVERTED_MAP.put(condition, converted);
			return converted;
		}

		assert false : "Here shouldn't be reached!";
		return null;
	}

	/**
	 * 式を変換するためのメソッド
	 * 
	 * @param expression
	 * @return
	 */
	public static String getNormalizedExpression(final ExpressionInfo expression) {

		String converted = ORIGINAL_TO_CONVERTED_MAP.get(expression);
		if (null != converted) {
			return converted;
		}

		final StringBuilder text = new StringBuilder();

		if (expression instanceof ArrayElementUsageInfo) {

			final ExpressionInfo ownerExpression = ((ArrayElementUsageInfo) expression)
					.getQualifierExpression();
			final String ownerExpressionString = Conversion
					.getNormalizedExpression(ownerExpression);
			text.append(ownerExpressionString);

			text.append("[");
			final ExpressionInfo indexExpression = ((ArrayElementUsageInfo) expression)
					.getIndexExpression();
			final String indexExpressionString = Conversion
					.getNormalizedExpression(indexExpression);
			text.append(indexExpressionString);
			text.append("]");

		} else if (expression instanceof ArrayInitializerInfo) {

			final ArrayInitializerInfo initializer = (ArrayInitializerInfo) expression;

			text.append("{");

			for (final ExpressionInfo element : initializer
					.getElementInitializers()) {
				text.append(Conversion.getNormalizedExpression(element));
				text.append(",");
			}

			text.deleteCharAt(text.length() - 1);
			text.append("}");

		} else if (expression instanceof ArrayTypeReferenceInfo) {

			final ArrayTypeReferenceInfo reference = (ArrayTypeReferenceInfo) expression;
			final ArrayTypeInfo arrayType = (ArrayTypeInfo) reference.getType();
			text.append(arrayType.getTypeName());

		} else if (expression instanceof BinominalOperationInfo) {

			final BinominalOperationInfo operation = (BinominalOperationInfo) expression;

			switch (Configuration.INSTANCE.getPO()) {

			case NO: // 演算をそのまま用いる
				final ExpressionInfo firstOperand = operation.getFirstOperand();
				final String firstOperandString = Conversion
						.getNormalizedExpression(firstOperand);
				text.append(firstOperandString);

				final OPERATOR operator = operation.getOperator();
				final String operatorString = operator.getToken();
				text.append(operatorString);

				final ExpressionInfo secondOperand = operation
						.getSecondOperand();
				final String secondOperandString = Conversion
						.getNormalizedExpression(secondOperand);
				text.append(secondOperandString);
				break;

			case TYPE: // 演算をその型に正規化する
				text.append(operation.getType().getTypeName());
				break;

			case ALL: // 全ての演算を同一の字句に正規化する
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof MethodCallInfo) {

			final MethodCallInfo methodCall = (MethodCallInfo) expression;
			final MethodInfo method = methodCall.getCallee();
			final ExpressionInfo qualifier = methodCall
					.getQualifierExpression();

			if (CALL_NORMALIZATION.FQN == Configuration.INSTANCE.getPI()) {
//				final ClassInfo ownerClass = methodCall.getOwnerMethod()
//						.getOwnerClass();
//				if ((qualifier instanceof ClassReferenceInfo)
//						&& (((ClassReferenceInfo) qualifier)
//								.getReferencedClass().equals(ownerClass))) {
//				} else {
					text.append(Conversion.getNormalizedExpression(qualifier));
					text.append(".");
//				}
			}			

			switch (Configuration.INSTANCE.getPI()) {

			case NO: // 正規化レベル0，メソッド名はそのまま，引数情報も用いる
				text.append(method.getMethodName());
				text.append("(");
				for (final ExpressionInfo argument : methodCall.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < methodCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITH_ARG: // 正規化レベル1，メソッド名を返り値の型名に正規化する，引数情報も用いる
				text.append(method.getReturnType().getTypeName());
				text.append("(");
				for (final ExpressionInfo argument : methodCall.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < methodCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITHOUT_ARG: // 正規化レベル1，メソッド名を返り値の型名に正規化する，引数情報は用いない
				text.append(method.getReturnType().getTypeName());
				break;
			case ALL: // 正規化レベル1，全てのメソッドを同一字句に正規化する．引数情報は用いない
				text.append("TOKEN");
				break;
			default:
				assert false : "Here shouldn't be reached!";
			}

			text.append("");

		} else if (expression instanceof ConstructorCallInfo<?>) {

			final ConstructorCallInfo<?> constructorCall = ((ConstructorCallInfo<?>) expression);

			switch (Configuration.INSTANCE.getPI()) {

			case NO: // 正規化レベル0，コンストラクタ名はそのまま，引数情報も用いる
				text.append("new ");
				text.append(constructorCall.getType().getTypeName());
				text.append("(");

				for (final ExpressionInfo argument : constructorCall
						.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < constructorCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITH_ARG: // 正規化レベル1，コンストラクタ呼び出しを型に正規化する(new
				// 演算子を取る)，引数情報は用いる
				text.append(constructorCall.getType().getTypeName());
				text.append("(");
				for (final ExpressionInfo argument : constructorCall
						.getArguments()) {
					final String argumentString = Conversion
							.getNormalizedExpression(argument);
					text.append(argumentString);
					text.append(",");
				}
				if (0 < constructorCall.getArguments().size()) {
					text.deleteCharAt(text.length() - 1);
				}
				text.append(")");
				break;
			case TYPE_WITHOUT_ARG: // 正規化レベル2，コンストラクタ呼び出しを型に正規化する（new
				// 演算子を取る），引数情報は用いない
				text.append(constructorCall.getType().getTypeName());
				break;
			case ALL: // 正規化レベル3，全てのコンストラクタ呼び出しを同一字句に正規化する，引数情報は用いない
				text.append("TOKEN");
				break;
			default:
				assert false : "Here shouldn't be reached!";
			}

		} else if (expression instanceof CastUsageInfo) {

			final CastUsageInfo cast = (CastUsageInfo) expression;

			switch (Configuration.INSTANCE.getPC()) {
			case NO:
				text.append("(");

				text.append(cast.getType().getTypeName());

				text.append(")");

				final ExpressionInfo castedExpression = cast.getCastedUsage();
				final String castedExpressionString = Conversion
						.getNormalizedExpression(castedExpression);
				text.append(castedExpressionString);
				break;

			case TYPE:
				text.append(cast.getType().getTypeName());
				break;

			case ALL:
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof ClassReferenceInfo) {

			switch (Configuration.INSTANCE.getPR()) {

			case NO: // クラス参照を正規化しない
				final ClassReferenceInfo reference = (ClassReferenceInfo) expression;
				text.append(reference.getType().getTypeName());
				break;
			case ALL: // すべて同一トークンに正規化
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof EmptyExpressionInfo) {

			// 何もする必要がない

		} else if (expression instanceof ForeachConditionInfo) {

			final ForeachConditionInfo foreach = (ForeachConditionInfo) expression;
			text.append(Conversion.getNormalizedCondition(foreach
					.getIteratorVariable()));
			text.append(":");
			text.append(Conversion.getNormalizedExpression(foreach
					.getIteratorExpression()));

		} else if (expression instanceof LiteralUsageInfo) {

			final LiteralUsageInfo literal = (LiteralUsageInfo) expression;

			switch (Configuration.INSTANCE.getPL()) {

			case NO: // リテラルをそのまま用いる
				text.append(literal.getLiteral());
				break;
			case TYPE: // リテラルをその型の正規化する
				text.append(literal.getType().getTypeName());
				break;
			case ALL: // 全てのリテラルを同一の字句に正規化する
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof MonominalOperationInfo) {

			final MonominalOperationInfo operation = (MonominalOperationInfo) expression;

			switch (Configuration.INSTANCE.getPO()) {

			case NO: // 演算をそのまま用いる
				final OPERATOR operator = operation.getOperator();
				text.append(operator.getToken());

				final ExpressionInfo operand = ((MonominalOperationInfo) expression)
						.getOperand();
				final String operandString = Conversion
						.getNormalizedExpression(operand);
				text.append(operandString);
				break;

			case TYPE: // 演算をその型に正規化する
				text.append(operation.getType().getTypeName());
				break;

			case ALL: // 全ての演算を同一の字句に正規化する
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof NullUsageInfo) {

			text.append("NULL");

		} else if (expression instanceof ParenthesesExpressionInfo) {

			text.append("(");

			final ParenthesesExpressionInfo parentheses = (ParenthesesExpressionInfo) expression;
			text.append(Conversion.getNormalizedExpression(parentheses
					.getParnentheticExpression()));

			text.append(")");

		} else if (expression instanceof TernaryOperationInfo) {

			final TernaryOperationInfo operation = (TernaryOperationInfo) expression;

			switch (Configuration.INSTANCE.getPO()) {

			case NO: // 演算をそのまま用いる

				final ConditionInfo condition = operation.getCondition();
				final String conditionExpressionString = Conversion
						.getNormalizedCondition(condition);
				text.append(conditionExpressionString);

				text.append("?");

				final ExpressionInfo trueExpression = operation
						.getTrueExpression();
				String trueExpressionString = Conversion
						.getNormalizedExpression(trueExpression);
				text.append(trueExpressionString);

				text.append(":");

				final ExpressionInfo falseExpression = operation
						.getFalseExpression();
				String falseExpressionString = Conversion
						.getNormalizedExpression(falseExpression);
				text.append(falseExpressionString);
				break;

			case TYPE: // 演算をその型に正規化する
				text.append(operation.getType().getTypeName());
				break;

			case ALL: // 全ての演算を同一の字句に正規化する
				text.append("TOKEN");
				break;
			}

		} else if (expression instanceof UnknownEntityUsageInfo) {

			text.append("UNKNOWN");

		} else if (expression instanceof VariableUsageInfo<?>) {

			final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) expression)
					.getUsedVariable();
			final TypeInfo variableType = usedVariable.getType();

			switch (Configuration.INSTANCE.getPV()) {
			case NO: // 正規化レベル0，変数名をそのまま使う
				text.append(usedVariable.getName());
				break;

			case TYPE: // 正規化レベル1，変数は型名に正規化する．変数名が異なっていても，型が同じであれば，クローンとして検出する
				text.append(variableType.getTypeName());
				break;

			case ALL: // 正規化レベル2，全ての変数を同一字句に正規化する．
				text.append("TOKEN");
				break;

			default:
				assert false : "Here shouldn't be reached!";
			}
		}

		else {
			assert false : "Here shouldn't be reached!";
		}

		converted = text.toString();
		ORIGINAL_TO_CONVERTED_MAP.put(expression, converted);
		return converted;
	}

	private static final ConcurrentMap<Object, String> ORIGINAL_TO_CONVERTED_MAP = new ConcurrentHashMap<Object, String>();
}
