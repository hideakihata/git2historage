package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedTypeParameterTypeInfo implements
        UnresolvedReferenceTypeInfo<TypeParameterTypeInfo> {

    public UnresolvedTypeParameterTypeInfo(final UnresolvedTypeParameterInfo typeParameter) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameter = typeParameter;
        this.resolvedInfo = null;
    }

    public UnresolvedTypeParameterInfo getReferencedTypeParameter() {
        return this.typeParameter;
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public TypeParameterTypeInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public String getTypeName() {
        return this.getReferencedTypeParameter().getName();
    }

    @Override
    public TypeParameterTypeInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedTypeParameterInfo unresolvedTypeParameter = this
                .getReferencedTypeParameter();
        final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        for (final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType : unresolvedTypeParameter
                .getExtendsTypes()) {
            final ReferenceTypeInfo extendsType = unresolvedExtendsType.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            typeParameter.addExtendsType(extendsType);
        }
        this.resolvedInfo = new TypeParameterTypeInfo(typeParameter);
        return this.resolvedInfo;
    }

    final private UnresolvedTypeParameterInfo typeParameter;

    private TypeParameterTypeInfo resolvedInfo;
}
