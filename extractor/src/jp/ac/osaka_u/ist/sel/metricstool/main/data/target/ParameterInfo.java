package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 引数を表すためのクラス． 型を提供するのみ．
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public abstract class ParameterInfo extends VariableInfo<CallableUnitInfo> {

    /**
     * 引数オブジェクトを初期化する．名前と型が必要．
     * 
     * @param modifiers 修飾子の Set
     * @param name 引数名
     * @param type 引数の型
     * @param definitionMethod 宣言しているメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    ParameterInfo(final Set<ModifierInfo> modifiers, final String name, final TypeInfo type,
            final CallableUnitInfo definitionMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(modifiers, name, type, definitionMethod, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * 与えられた変数のSetに含まれている引数をSetとして返す
     * @param variables 変数のSet
     * @return 与えられた変数のSetに含まれる引数のSet
     */
    public static Set<ParameterInfo> getLocalVariables(Collection<VariableInfo<?>> variables) {
        final Set<ParameterInfo> parameters = new HashSet<ParameterInfo>();
        for (final VariableInfo<?> variable : variables) {
            if (variable instanceof ParameterInfo) {
                parameters.add((ParameterInfo) variable);
            }
        }
        return Collections.unmodifiableSet(parameters);
    }
}
