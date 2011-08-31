package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolvedな変数の共通な親クラス.
 * <ul>
 * <li>変数名</li>
 * <li>型</li>
 * <li>修飾子</li>
 * <li>位置情報</li>
 * </ul>
 * 
 * @author higo
 * @param <TVar> 解決済みの型
 * @param <TUnit> この変数を宣言しているユニット
 */
public abstract class UnresolvedVariableInfo<TVar extends VariableInfo<? extends UnitInfo>, TUnit extends UnresolvedUnitInfo<? extends UnitInfo>>
        extends UnresolvedUnitInfo<TVar> implements ModifierSetting {

    /**
     * 変数名を返す
     * 
     * @return 変数名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 変数名をセットする
     * 
     * @param name 変数名
     */
    public final void setName(final String name) {

        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * 変数の型を返す
     * 
     * @return 変数の型
     */
    public final UnresolvedTypeInfo<?> getType() {
        return this.type;
    }

    /**
     * 変数の型をセットする
     * 
     * @param type 変数の型
     */
    public final void setType(final UnresolvedTypeInfo<?> type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * この変数を宣言しているユニットを返す
     * 
     * @return この変数を宣言しているユニット
     */
    public final TUnit getDefinitionUnit() {
        return this.definitionUnit;
    }

    /**
     * 修飾子を追加する
     * 
     * @param modifier 追加する修飾子
     */
    public final void addModifier(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * 変数オブジェクトを初期化する．
     * 
     * @param name 変数名
     * @param type 変数の型
     * @param definitionUnit 宣言している空間
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    UnresolvedVariableInfo(final String name, final UnresolvedTypeInfo<?> type,
            final TUnit definitionUnit, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super();

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == type) || (null == definitionUnit)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
        this.definitionUnit = definitionUnit;

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * 変数名を表す変数
     */
    private String name;

    /**
     * 変数の型を表す変数
     */
    private UnresolvedTypeInfo<?> type;

    /**
     * 変数の修飾子を表す変数
     */
    private Set<ModifierInfo> modifiers;

    /**
     * 変数を宣言しているユニットを表す変数
     */
    private final TUnit definitionUnit;

}
