package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * TypeParameter‚ÌŒ^‚ð•\‚·ƒNƒ‰ƒX
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class TypeParameterTypeInfo implements ReferenceTypeInfo {

    public TypeParameterTypeInfo(final TypeParameterInfo referencedTypeParameter) {
        this.referencedTypeParameter = referencedTypeParameter;
    }

    @Override
    public String getTypeName() {
        return this.referencedTypeParameter.getName();
    }

    @Override
    public boolean equals(final TypeInfo type) {

        if (null == type) {
            return false;
        }

        if (!(type instanceof TypeParameterTypeInfo)) {
            return false;
        }

        return this.getReferncedTypeParameter().equals(
                ((TypeParameterTypeInfo) type).getReferncedTypeParameter());
    }

    public TypeParameterInfo getReferncedTypeParameter() {
        return this.referencedTypeParameter;
    }

    private final TypeParameterInfo referencedTypeParameter;
}
