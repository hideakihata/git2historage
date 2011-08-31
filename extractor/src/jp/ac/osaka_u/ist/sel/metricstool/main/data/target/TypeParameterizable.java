package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.List;


/**
 * 型パラメータが定義可能であることを表すインターフェース
 */
public interface TypeParameterizable {

    /**
     * 型パラメータ定義を追加する
     * 
     * @param typeParameter 追加する型パラメータ定義
     */
    void addTypeParameter(TypeParameterInfo typeParameter);

    /**
     * 引数で与えられたインデックスの型パラメータを返す
     * 
     * @param index 型パラメータを指定するためのインデックス
     * @return 引数で与えられたインデックスの型パラメータ
     */
    TypeParameterInfo getTypeParameter(int index);

    /**
     * 引数で与えられた型引数がこのユニットで定義されているかを返す
     * @param typeParameter
     * @return
     */
    boolean isDefined(TypeParameterInfo typeParameter);
    
    /**
     * 型パラメータのリストを返す
     * 
     * @return 型パラメータのリスト
     */
    List<TypeParameterInfo> getTypeParameters();

    /**
     * 外側にある，型パラメータを定義可能なユニットを返す．
     * 
     * @return 外側にある，型パラメータを定義可能なユニット
     */
    TypeParameterizable getOuterTypeParameterizableUnit();
}
