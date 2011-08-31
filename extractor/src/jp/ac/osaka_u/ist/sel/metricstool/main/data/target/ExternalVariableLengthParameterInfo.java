package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


@SuppressWarnings("serial")
public class ExternalVariableLengthParameterInfo extends ExternalParameterInfo implements
        VariableLengthParameterInfo {

    /**
     * 引数の型を指定してオブジェクトを初期化．外部定義のメソッド名なので引数名は不明．
     * 
     * @param type 引数の型
     * @param definitionMethod 宣言しているメソッド
     */
    public ExternalVariableLengthParameterInfo(final TypeInfo type,
            final CallableUnitInfo definitionMethod) {
        super(new ArrayTypeInfo(type, 1), definitionMethod);
    }
}
