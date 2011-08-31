package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールド，引数，ローカル変数の共通の親クラス． 以下の情報を持つ．
 * <ul>
 * <li>変数名</li>
 * <li>型</li>
 * <li>修飾子</li>
 * <li>位置情報</li>
 * 
 * @author higo
 * @param <TUnit> この変数を宣言しているユニット 
 */
@SuppressWarnings("serial")
public abstract class VariableInfo<TUnit extends UnitInfo> extends UnitInfo implements Modifier {

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 変数名を返す
     * 
     * @return 変数名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 変数の型を返す
     * 
     * @return 変数の型
     */
    public final TypeInfo getType() {
        assert null != this.type : "variable type is not set.";
        return this.type;
    }

    /**
     * 変数の型を設定する
     * 
     * @param type 変数の型
     */
    public final void setType(final TypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new IllegalArgumentException();
        }

        this.type = type;
    }

    /**
     * 変数を宣言しているユニットを返す
     * 
     * @return 変数を宣言しているユニット
     */
    public final TUnit getDefinitionUnit() {
        return this.definitionUnit;
    }

    /**
     * 変数のハッシュコードを返す
     */
    @Override
    public final int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * 変数利用の一覧を返す．
     * どの変数も用いられていないので，空のsetが返される
     * 
     * @return 変数利用のSet
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 変数定義のSetを返す
     * 
     * @return 変数定義のSet
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(this);
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public final Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * 変数オブジェクトを初期化する
     * 
     * @param modifiers 修飾子の Set
     * @param name 変数名
     * @param type 変数の型
     * @param definitionUnit 宣言しているユニット
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    VariableInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final TUnit definitionUnit, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == modifiers) || (null == name) || (null == definitionUnit)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
        this.definitionUnit = definitionUnit;
    }

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * 変数名を表す変数
     */
    private final String name;

    /**
     * 変数の型を表す変数
     */
    private TypeInfo type;

    /**
     * この変数を宣言しているユニットを保存するための変数
     */
    private final TUnit definitionUnit;

    /**
     * 空の変数のSetを表す
     */
    public static final Set<VariableInfo<? extends UnitInfo>> EmptySet = Collections
            .unmodifiableSet(new HashSet<VariableInfo<? extends UnitInfo>>());

}
