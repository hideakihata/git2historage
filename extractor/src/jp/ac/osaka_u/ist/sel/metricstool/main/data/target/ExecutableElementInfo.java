package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Set;


/**
 * 実行可能な単位を表す要素
 * 
 * @author higo
 *
 */
public interface ExecutableElementInfo extends Position, Serializable {

    /**
     * 変数の使用のSetを返す
     * 
     * @return 変数の使用のSet
     */
    Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();

    /**
     * 定義されている変数のSetを返す
     * 
     * @return 文の中で定義されている変数のSet
     */
    Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * メソッド呼び出しを返す
     * 
     * @return メソッド呼び出し
     */
    Set<CallInfo<? extends CallableUnitInfo>> getCalls();

    /**
     * オーナーメソッドを返す
     * 
     * @return オーナーメソッド
     */
    CallableUnitInfo getOwnerMethod();

    /**
     * 直接所有する空間を返す
     * 
     * @return 直接所有する空間
     */
    LocalSpaceInfo getOwnerSpace();

    /**
     * テキスト表現(String型)を返す
     * 
     * @return　テキスト表現(String型)を返す
     */
    String getText();

    /**
     * 投げられる可能性がある例外のSetを返す
     * 
     * @return　投げられる可能性がある例外のSet
     */
    Set<ReferenceTypeInfo> getThrownExceptions();

    /**
     * このプログラム要素のディープコピーを返す.
     * ただし，自分よりも下位に位置するオブジェクトのみディープコピー．
     * 自分の上位に位置するものについてはシャローコピー.
     * 
     * @return
     */
    ExecutableElementInfo copy();
}