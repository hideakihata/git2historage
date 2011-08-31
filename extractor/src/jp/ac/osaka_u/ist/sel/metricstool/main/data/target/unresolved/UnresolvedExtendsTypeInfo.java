package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExtendsTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedExtendsTypeInfo implements UnresolvedTypeInfo<ExtendsTypeInfo> {

    public UnresolvedExtendsTypeInfo(final UnresolvedReferenceTypeInfo<?> extendsType) {
        this.extendsType = extendsType;
    }

    public UnresolvedReferenceTypeInfo<?> getExtendsType() {
        return this.extendsType;
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    @Override
    public ExtendsTypeInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new IllegalStateException();
        }

        return this.resolvedInfo;
    }

    @Override
    public String getTypeName() {
        final StringBuilder text = new StringBuilder();
        text.append("? extends ");
        text.append(this.extendsType.getTypeName());
        return text.toString();
    }

    @Override
    public ExtendsTypeInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedReferenceTypeInfo<?> unresolvedExtendsType = this.getExtendsType();
        final ReferenceTypeInfo extendsType = unresolvedExtendsType.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ExtendsTypeInfo(extendsType);
        return this.resolvedInfo;
    }

    private final UnresolvedReferenceTypeInfo<?> extendsType;

    private ExtendsTypeInfo resolvedInfo;
}
