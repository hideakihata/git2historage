package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * superを利用した型を表すクラス
 * List<? super T> の ? super T　の部分を表す．
 *
 * @author higo
 *
 */
@SuppressWarnings("serial")
public class SuperTypeInfo implements TypeInfo {

    public SuperTypeInfo(final ReferenceTypeInfo superType) {
        this.superType = superType;
    }

    public ReferenceTypeInfo getSuperType() {
        return this.superType;
    }

    @Override
    public boolean equals(TypeInfo typeInfo) {

        if (!(typeInfo instanceof SuperTypeInfo)) {
            return false;
        }

        return this.getSuperType().equals(((SuperTypeInfo) typeInfo).getSuperType());
    }

    @Override
    public String getTypeName() {
        final StringBuilder text = new StringBuilder();
        text.append(ArbitraryTypeInfo.getInstance().getTypeName());
        text.append(" super ");
        text.append(this.getSuperType().getTypeName());
        return text.toString();
    }

    private final ReferenceTypeInfo superType;
}
