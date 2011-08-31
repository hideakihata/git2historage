package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 *　可変長引数を表すクラス
 *
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class TargetVariableLengthParameterInfo extends TargetParameterInfo implements
        VariableLengthParameterInfo {

    /**
     * 引数名，引数の型を与えてオブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param name 引数名
     * @param type 引数の型
     * @param index 何番目の引数かを表す
     * @param definitionMethod 宣言しているメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetVariableLengthParameterInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final int index, final CallableUnitInfo definitionMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(modifiers, name, new ArrayTypeInfo(type, 1), index, definitionMethod, fromLine,
                fromColumn, toLine, toColumn);
    }
}
