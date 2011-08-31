package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッドを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class MethodInfo extends CallableUnitInfo implements MetricMeasurable, Member,
        StaticOrInstance {

    /**
     * メソッドオブジェクトを初期化する
     * 
     * @param modifiers 修飾子のSet
     * @param methodName メソッド名
     * @param instance インスタンスかスタティックか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    MethodInfo(final Set<ModifierInfo> modifiers, final String methodName, final boolean instance,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodName) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.returnType = null;

        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();

        this.instance = instance;
    }

    /**
     * このメソッドが，引数で与えられた情報を使って呼び出すことができるかどうかを判定する．
     * 
     * @param methodName メソッド名
     * @param actualParameters 実引数のリスト
     * @return 呼び出せる場合は true，そうでない場合は false
     */
    public final boolean canCalledWith(final String methodName,
            final List<ExpressionInfo> actualParameters) {

        if ((null == methodName) || (null == actualParameters)) {
            throw new IllegalArgumentException();
        }

        // メソッド名が等しくない場合は該当しない
        if (!methodName.equals(this.getMethodName())) {
            return false;
        }

        return super.canCalledWith(actualParameters);
    }

    /**
     * このメソッドが引数で与えられたオブジェクト（メソッド）と等しいかどうかを判定する
     * 
     * @param o 比較対象オブジェクト（メソッド）
     * @return 等しい場合は true, 等しくない場合は false
     */
    @Override
    public final boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof MethodInfo)) {
            return false;
        }

        return 0 == this.compareTo((MethodInfo) o);
    }

    /**
     * メトリクス計測対象としての名前を返す
     * 
     * @return メトリクス計測対象としての名前
     */
    @Override
    public final String getMeasuredUnitName() {

        final StringBuilder sb = new StringBuilder();

        final String fullQualifiedName = this.getOwnerClass().getFullQualifiedName(
                Settings.getInstance().getLanguage().getNamespaceDelimiter());
        sb.append(fullQualifiedName);

        sb.append("#");

        final String methodName = this.getMethodName();
        sb.append(methodName);

        sb.append("(");

        if (this.getParameters().size() > 0) {
            for (final ParameterInfo parameter : this.getParameters()) {
                final TypeInfo parameterType = parameter.getType();
                sb.append(parameterType.getTypeName());
                sb.append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }

    @Override
    public final String getSignatureText() {

        final StringBuilder text = new StringBuilder();

        text.append(this.getReturnType().getTypeName());
        text.append(" ");
        text.append(this.getMethodName());

        text.append("(");
        for (final ParameterInfo parameter : this.getParameters()) {
            text.append(parameter.getType().getTypeName());
            text.append(",");
        }
        if (0 < this.getParameterNumber()) {
            text.deleteCharAt(text.length() - 1);
        }
        text.append(")");

        return text.toString();
    }

    /**
     * このメソッドの名前を返す
     * 
     * @return メソッド名
     */
    public final String getMethodName() {
        return this.methodName;
    }

    /**
     * このメソッドの返り値の型を返す
     * 
     * @return 返り値の型
     */
    public final TypeInfo getReturnType() {

        if (null == this.returnType) {
            throw new NullPointerException();
        }

        return this.returnType;
    }

    /**
     * このメソッドの返り値をセットする．
     * 
     * @param returnType このメソッドの返り値
     */
    public void setReturnType(final TypeInfo returnType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * このメソッドがオーバーライドしているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param overridee 追加するオーバーライドされているメソッド
     */
    public void addOverridee(final MethodInfo overridee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overridee) {
            throw new NullPointerException();
        }

        this.overridees.add(overridee);
    }

    /**
     * このメソッドをオーバーライドしているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param overrider 追加するオーバーライドしているメソッド
     * 
     */
    public void addOverrider(final MethodInfo overrider) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overrider) {
            throw new NullPointerException();
        }

        this.overriders.add(overrider);
    }

    /**
     * このメソッドがオーバーライドしているメソッドの SortedSet を返す．
     * 
     * @return このメソッドがオーバーライドしているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getOverridees() {
        return Collections.unmodifiableSortedSet(this.overridees);
    }

    /**
     * このメソッドをオーバーライドしているメソッドの SortedSet を返す．
     * 
     * @return このメソッドをオーバーライドしているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getOverriders() {
        return Collections.unmodifiableSortedSet(this.overriders);
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    @Override
    public final boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    @Override
    public final boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * メソッド名を保存するための変数
     */
    private final String methodName;

    /**
     * 返り値の型を保存するための変数
     */
    private TypeInfo returnType;

    /**
     * このメソッドがオーバーライドしているメソッド一覧を保存するための変数
     */
    protected final SortedSet<MethodInfo> overridees;

    /**
     * オーバーライドされているメソッドを保存するための変数
     */
    protected final SortedSet<MethodInfo> overriders;

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private final boolean instance;
}
