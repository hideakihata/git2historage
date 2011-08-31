package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * extends を利用した型を表すクラス
 * List<? extends T> の ? extends T を表す．
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class ExtendsTypeInfo implements TypeInfo {

    public ExtendsTypeInfo(final ReferenceTypeInfo extendsType) {
        this.extendsType = extendsType;
    }

    public ReferenceTypeInfo getExtendsType() {
        return this.extendsType;
    }

    @Override
    public boolean equals(TypeInfo typeInfo) {

        if (!(typeInfo instanceof ExtendsTypeInfo)) {
            return false;
        }

        return this.getExtendsType().equals(((ExtendsTypeInfo) typeInfo).getExtendsType());
    }

    @Override
    public String getTypeName() {
        final StringBuilder text = new StringBuilder();
        text.append(ArbitraryTypeInfo.getInstance().getTypeName());
        text.append(" extends ");
        text.append(this.getExtendsType().getTypeName());
        return text.toString();
    }

    private final ReferenceTypeInfo extendsType;
}
