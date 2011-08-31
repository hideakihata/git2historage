package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

public class CFGUtility {

	/**
	 * このメソッドが，張り付いているオブジェクトの状態を変更しているかを返す． 現在のところ，変更しているのは下記のいずれかの条件を満たすとき
	 * 1.フィールドに対して代入処理を行っている． 2. フィールドに張り付いたメソッド呼び出しがオブジェクトの状態を変更している．
	 * 
	 * @return　変更しているときはtrue, 変更していない場合はfalse．
	 */
	static public boolean stateChange(final MethodInfo method) {

		final ClassInfo ownerClass = method.getOwnerClass();
		final SortedSet<FieldInfo> fields = ownerClass.getDefinedFields();

		// フィールドに対して代入処理があるかどうかを調べる
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variableUsage.isAssignment() && fields.contains(variable)) {
				return true;
			}
		}

		// メソッド呼び出しについて，オブジェクトの内容が変化しているかを調べる
		final Set<CallableUnitInfo> checkedMethods = new HashSet<CallableUnitInfo>();
		checkedMethods.add(method);
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (fields.contains(usedVariable)) {
						final MethodInfo callee = methodCall.getCallee();
						if (stateChange(callee, checkedMethods)) {
							return true;
						}

						for (final MethodInfo overrider : callee
								.getOverriders()) {
							if (stateChange(overrider, checkedMethods)) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * 第一引数のメソッドが，第二引数で指定されたメソッドの引数の状態を変更しているかを返す．
	 * 
	 * @param method
	 * @param index
	 * @return
	 */
	static public boolean stateChange(final CallableUnitInfo method,
			final int index) {

		// method がExternalで引数がない場合は，解析をあきらめる
		if ((method instanceof ExternalMethodInfo)
				|| (method instanceof ExternalConstructorInfo)) {
			if (0 == method.getParameterNumber()) {
				return false;
			}
		}

		// 指定された引数を取得（可変長引数への対応を含む）
		final ParameterInfo parameter;
		if (index < method.getParameters().size()) {
			parameter = method.getParameters().get(index);
		} else {
			parameter = method.getParameters().get(
					method.getParameters().size() - 1);
		}

		// 参照型でない場合は「状態を変更」というのはありえない
		if (!(parameter.getType() instanceof ClassTypeInfo)) {
			return false;
		}

		// メソッド呼び出しについて，オブジェクトの内容が変化しているかを調べる
		final Set<CallableUnitInfo> checkedMethods = new HashSet<CallableUnitInfo>();
		checkedMethods.add(method);
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {

			// quantifiler を調べる
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (parameter.equals(usedVariable)) {
						final MethodInfo callee = methodCall.getCallee();
						if (stateChange(callee, checkedMethods)) {
							return true;
						}

						for (final MethodInfo overrider : callee
								.getOverriders()) {
							if (stateChange(overrider, checkedMethods)) {
								return true;
							}
						}
					}
				}
			}

			// parameter を調べる
			if (call instanceof MethodCallInfo
					|| call instanceof ClassConstructorCallInfo) {
				final List<ExpressionInfo> arguments = call.getArguments();
				for (int i = 0; i < arguments.size(); i++) {
					if (arguments.get(i) instanceof VariableUsageInfo<?>) {
						final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) arguments
								.get(i)).getUsedVariable();
						if (parameter.equals(usedVariable)) {
							final CallableUnitInfo callee = call.getCallee();
							if (stateChange(callee, i, checkedMethods)) {
								return true;
							}

							if (callee instanceof MethodInfo) {
								for (final MethodInfo overrider : ((MethodInfo) callee)
										.getOverriders()) {
									if (stateChange(overrider, i,
											checkedMethods)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	static private boolean stateChange(final MethodInfo method,
			final Set<CallableUnitInfo> checkedMethods) {

		if (checkedMethods.contains(method)) {
			return false;
		} else {
			checkedMethods.add(method);
		}

		final ClassInfo ownerClass = method.getOwnerClass();
		final SortedSet<FieldInfo> fields = ownerClass.getDefinedFields();

		// フィールドに対して代入処理があるかどうかを調べる
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variableUsage.isAssignment() && fields.contains(variable)) {
				return true;
			}
		}

		// メソッド呼び出しについて，オブジェクトの内容が変化しているかを調べる
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (fields.contains(usedVariable)) {
						final MethodInfo callee = methodCall.getCallee();
						if (stateChange(callee, checkedMethods)) {
							return true;
						}

						for (final MethodInfo overrider : callee
								.getOverriders()) {
							if (stateChange(overrider, checkedMethods)) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}

	static private boolean stateChange(final CallableUnitInfo method,
			final int index, final Set<CallableUnitInfo> checkedMethods) {

		if (checkedMethods.contains(method)) {
			return false;
		} else {
			checkedMethods.add(method);
		}

		// method がExternalで引数がない場合は，解析をあきらめる
		if ((method instanceof ExternalMethodInfo)
				|| (method instanceof ExternalConstructorInfo)) {
			if (0 == method.getParameterNumber()) {
				return false;
			}
		}

		// 指定された引数を取得（可変長引数への対応を含む）
		final ParameterInfo parameter;
		if (index < method.getParameters().size()) {
			parameter = method.getParameters().get(index);
		} else {
			parameter = method.getParameters().get(
					method.getParameters().size() - 1);
		}

		// 参照型でない場合は「状態を変更」というのはありえない
		if (!(parameter.getType() instanceof ClassTypeInfo)) {
			return false;
		}

		// メソッド呼び出しについて，オブジェクトの内容が変化しているかを調べる
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {

			// quantifiler を調べる
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) qualifier)
							.getUsedVariable();
					if (parameter.equals(usedVariable)) {
						if (stateChange(methodCall.getCallee(), checkedMethods)) {
							return true;
						}
					}
				}
			}

			// parameter を調べる
			if (call instanceof MethodCallInfo
					|| call instanceof ClassConstructorCallInfo) {
				final List<ExpressionInfo> arguments = call.getArguments();
				for (int i = 0; i < arguments.size(); i++) {
					if (arguments.get(i) instanceof VariableUsageInfo<?>) {
						final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) arguments
								.get(i)).getUsedVariable();
						if (parameter.equals(usedVariable)) {
							final CallableUnitInfo callee = call.getCallee();
							if (stateChange(callee, i, checkedMethods)) {
								return true;
							}

							if (callee instanceof MethodInfo) {
								for (final MethodInfo overrider : ((MethodInfo) callee)
										.getOverriders()) {
									if (stateChange(overrider, i,
											checkedMethods)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * 引数で与えられたexpressionが分解されるべきものであればtrue, そうでない場合はfalseを返す
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isDissolved(final ExpressionInfo expression) {
		if (expression instanceof VariableUsageInfo<?>) {
			return false;
		} else if (expression instanceof ClassReferenceInfo) {
			return false;
		} else if (expression instanceof ArrayTypeReferenceInfo) {
			return false;
		} else if (expression instanceof EmptyExpressionInfo) {
			return false;
		} else if (expression instanceof LiteralUsageInfo) {
			return false;
		} else if (expression instanceof NullUsageInfo) {
			return false;
		} else if (expression instanceof UnknownEntityUsageInfo) {
			return false;
		} else {
			return true;
		}
	}

	private static final Random NATULAL_VALUE_GENERATOR = new Random();

	// public static int getRandomNaturalValue() {
	// return NATULAL_VALUE_GENERATOR.nextInt(Integer.MAX_VALUE);
	// }
}
