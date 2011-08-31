package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 任意の型を表すクラス．
 * List<?>の?を表す．
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class ArbitraryTypeInfo implements TypeInfo {

    private ArbitraryTypeInfo() {
    }

    @Override
    public boolean equals(TypeInfo typeInfo) {
        return typeInfo instanceof ArbitraryTypeInfo;
    }

    @Override
    public String getTypeName() {
        return "?";
    }

    public static ArbitraryTypeInfo getInstance() {
        return SINGLETON;
    }

    private final static ArbitraryTypeInfo SINGLETON = new ArbitraryTypeInfo();
}
