package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッド呼び出し，コンストラクタ呼び出しの共通の親クラス
 * 
 * @param <T> 呼び出されるユニットの型
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class CallInfo<T extends CallableUnitInfo> extends ExpressionInfo {

    /**
     * 呼び出しのCollectionから呼び出されているユニットのSetを返す
     * 
     * @param calls 呼び出しのCollection
     * @return 呼び出されているユニットのSet
     */
    public static Set<CallableUnitInfo> getCallees(
            Collection<CallInfo<? extends CallableUnitInfo>> calls) {

        final Set<CallableUnitInfo> callees = new HashSet<CallableUnitInfo>();
        for (final CallInfo<?> call : calls) {
            final CallableUnitInfo callee = call.getCallee();
            callees.add(callee);
        }
        return Collections.unmodifiableSet(callees);
    }

    /**
     * @param callee 呼ばれているオブジェクト，この呼び出しが，配列のコンストラクタの場合はnullが入っている．
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    CallInfo(final T callee, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.arguments = new LinkedList<ExpressionInfo>();
        this.typeArguments = new LinkedList<ReferenceTypeInfo>();

        this.callee = callee;

        // メソッド呼び出し関係を構築
        if (null != callee) {
            this.callee.addCaller(ownerMethod);
        }
    }

    /**
     * このメソッド呼び出しの実引数を追加．プラグインからは呼び出せない．
     * 
     * @param argument 追加する実引数
     */
    public final void addArgument(final ExpressionInfo argument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == argument) {
            throw new NullPointerException();
        }

        this.arguments.add(argument);
        argument.setOwnerExecutableElement(this);
    }

    /**
     * この呼び出しの実引数を追加．プラグインからは呼び出せない．
     * 
     * @param arguments 追加する実引数
     */
    public final void addArguments(final List<ExpressionInfo> arguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arguments) {
            throw new NullPointerException();
        }

        this.arguments.addAll(arguments);

        for (final ExpressionInfo argument : arguments) {
            argument.setOwnerExecutableElement(this);
        }
    }

    /**
     * このメソッド呼び出しの型引数を追加．プラグインからは呼び出せない
     * 
     * @param typeArgument 追加する型引数
     */
    public final void addTypeArgument(final ReferenceTypeInfo typeArgument) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * この呼び出しの型引数を追加．プラグインからは呼び出せない．
     * 
     * @param typeArguments 追加する型引数
     */
    public final void addTypeArguments(final List<ReferenceTypeInfo> typeArguments) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArguments) {
            throw new NullPointerException();
        }

        this.typeArguments.addAll(typeArguments);
    }

    /**
     * この呼び出しの実引数のListを返す．
     * 
     * @return　この呼び出しの実引数のList
     */
    public List<ExpressionInfo> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * この呼び出しの型引数のListを返す．
     * 
     * @return この呼び出しの型引数のList
     */
    public List<ReferenceTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * この呼び出しにおける変数使用群を返す
     * 
     * @return この呼び出しにおける変数使用群
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        for (final ExpressionInfo parameter : this.getArguments()) {
            variableUsages.addAll(parameter.getVariableUsages());
        }
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * この呼び出しで投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        final Set<ReferenceTypeInfo> thrownExceptions = new HashSet<ReferenceTypeInfo>();
        for (final ExpressionInfo parameter : this.getArguments()) {
            thrownExceptions.addAll(parameter.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExceptions);
    }

    /**
     * この呼び出しで呼び出されているものを返す
     * 
     * @return この呼び出しで呼び出されているもの
     */
    public final T getCallee() {
        return this.callee;
    }

    private final T callee;

    private final List<ExpressionInfo> arguments;

    private final List<ReferenceTypeInfo> typeArguments;

    /**
     * 空の呼び出しのSetを表す
     */
    public static final SortedSet<CallInfo<? extends CallableUnitInfo>> EmptySet = Collections
            .unmodifiableSortedSet(new TreeSet<CallInfo<? extends CallableUnitInfo>>());
}
