package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArbitraryTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


public class UnresolvedArbitraryTypeInfo implements UnresolvedTypeInfo<ArbitraryTypeInfo> {

    private UnresolvedArbitraryTypeInfo() {
        this.resolvedInfo = ArbitraryTypeInfo.getInstance();
    }

    @Override
    public boolean alreadyResolved() {
        return true;
    }

    @Override
    public ArbitraryTypeInfo getResolved() {
        return this.resolvedInfo;
    }

    @Override
    public String getTypeName() {
        return "*";
    }

    @Override
    public ArbitraryTypeInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
        return this.resolvedInfo;
    }

    public static final UnresolvedArbitraryTypeInfo getInstance() {
        return SINGLETON;
    }

    private static final UnresolvedArbitraryTypeInfo SINGLETON = new UnresolvedArbitraryTypeInfo();

    private final ArbitraryTypeInfo resolvedInfo;
}
