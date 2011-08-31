package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * メソッド呼び出しを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class MethodCallInfo extends CallInfo<MethodInfo> {

    /**
     * 呼び出されるメソッドを与えてオブジェクトを初期化
     *
     * @param qualifierType メソッド呼び出しの親の型
     * @param qualifierExpression メソッド呼び出しの親エンティティ
     * @param callee 呼び出されているメソッド
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public MethodCallInfo(final TypeInfo qualifierType, final ExpressionInfo qualifierExpression,
            final MethodInfo callee, final TypeInfo type, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(callee, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if ((null == qualifierType) || (null == callee) || (null == type)
                || (null == qualifierExpression)) {
            throw new NullPointerException();
        }

        this.qualifierType = qualifierType;
        this.qualifierExpression = qualifierExpression;
        this.type = type;
    }

    /**
     * このメソッド呼び出しの型を返す
     */
    @Override
    public TypeInfo getType() {
        return this.type;
        //        final MethodInfo callee = this.getCallee();
        //        final TypeInfo returnType = callee.getReturnType();
        //
        //        // 定義の返り値が型パラメータでなければそのまま返せる
        //        if (!(returnType instanceof TypeParameterTypeInfo)) {
        //            return returnType;
        //        }
        //
        //        //　型パラメータの場合
        //        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getQualifierType();
        //        final List<TypeInfo> typeArguments = callOwnerType.getTypeArguments();
        //
        //        // 型引数がある場合は，その型を返す
        //        if (0 < typeArguments.size()) {
        //            final int typeParameterIndex = ((TypeParameterTypeInfo) returnType)
        //                    .getReferncedTypeParameter().getIndex();
        //            final TypeInfo typeArgument = typeArguments.get(typeParameterIndex);
        //            return typeArgument;
        //
        //            // 型引数がない場合は，特殊な型を返す
        //        } else {
        //
        //            // Java　の場合 (型パラメータは1.5から導入された)
        //            if (Settings.getInstance().getLanguage().equals(LANGUAGE.JAVA15)) {
        //                final ClassInfo referencedClass = DataManager.getInstance().getClassInfoManager()
        //                        .getClassInfo(new String[] { "java", "lang", "Object" });
        //                final TypeInfo classType = new ClassTypeInfo(referencedClass);
        //                return classType;
        //            }
        //        }
        //
        //        assert false : "Here shouldn't be reached!";
        //        return null;
    }

    @Override
    public void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement) {
        super.setOwnerExecutableElement(ownerExecutableElement);
        this.qualifierExpression.setOwnerExecutableElement(ownerExecutableElement);
    }

    /**
     * このメソッド呼び出しがくっついている型を返す
     * 
     * @return このメソッド呼び出しがくっついている型
     */
    public TypeInfo getQualifierType() {
        return this.qualifierType;
    }

    /**
     * この式（メソッド呼び出し）における変数利用の一覧を返すクラス
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        variableUsages.addAll(quantifierExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();
        calls.add(this);
        final ExpressionInfo quantifierExpression = this.getQualifierExpression();
        calls.addAll(quantifierExpression.getCalls());
        return Collections.unmodifiableSet(calls);
    }

    /**
     * このメソッド呼び出しのテキスト表現（型）を返す
     * 
     * @return このメソッド呼び出しのテキスト表現（型）を返す
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo ownerExpression = this.getQualifierExpression();
        sb.append(ownerExpression.getText());

        sb.append(".");

        final MethodInfo method = this.getCallee();
        sb.append(method.getMethodName());

        sb.append("(");

        for (final ExpressionInfo argument : this.getArguments()) {
            sb.append(argument.getText());
            sb.append(",");
        }
        if (0 < this.getArguments().size()) {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        thrownExceptions.addAll(super.getThrownExceptions());
        thrownExceptions.addAll(this.getQualifierExpression().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExceptions);
    }

    @Override
    public ExecutableElementInfo copy() {

        final TypeInfo qualifierType = this.getQualifierType();
        final ExpressionInfo qualifierExpression = this.getQualifierExpression();
        final MethodInfo callee = this.getCallee();
        final TypeInfo type = this.getType();
        final CallableUnitInfo ownerMethod = this.getOwnerMethod();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final MethodCallInfo newCall = new MethodCallInfo(qualifierType, qualifierExpression,
                callee, type, ownerMethod, fromLine, fromColumn, toLine, toColumn);
        for (final ExpressionInfo argument : this.getArguments()) {
            newCall.addArgument((ExpressionInfo) argument.copy());
        }

        final ExecutableElementInfo owner = this.getOwnerExecutableElement();
        newCall.setOwnerExecutableElement(owner);

        final ConditionalBlockInfo ownerConditionalBlock = this.getOwnerConditionalBlock();
        if (null != ownerConditionalBlock) {
            newCall.setOwnerConditionalBlock(ownerConditionalBlock);
        }

        return newCall;
    }

    /**
     * このメソッド呼び出しの親，つまりこのメソッド呼び出しがくっついている要素を返す
     * 
     * @return このメソッド呼び出しの親
     */
    public final ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    private final TypeInfo qualifierType;

    private final ExpressionInfo qualifierExpression;

    private final TypeInfo type;
}
