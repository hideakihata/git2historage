package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 名前解決できない型を表すクラス．
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class UnknownTypeInfo implements TypeInfo {

    /**
     * このクラスの単一オブジェクトを返す
     * 
     * @return このクラスの単一オブジェクト
     */
    public static UnknownTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * 名前解決できない型の名前を返す．
     */
    public String getTypeName() {
        return UNKNOWN_STRING;
    }

    /**
     * 等しいかどうかのチェックを行う
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        return typeInfo instanceof UnknownTypeInfo;
    }

    /**
     * 外のクラスからアクセス不可にする
     */
    private UnknownTypeInfo() {
    }

    /**
     * 不明型の型名を表す定数
     */
    public static final String UNKNOWN_STRING = "UNKNOWN";

    /**
     * このクラスの単一オブジェクトを保存するための定数
     */
    private static final UnknownTypeInfo SINGLETON = new UnknownTypeInfo();
}
