package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;


public class UnresolvedThisConstructorCallInfo extends UnresolvedClassConstructorCallInfo {

    public UnresolvedThisConstructorCallInfo(final UnresolvedClassTypeInfo classType) {
        super(classType);
    }

    public UnresolvedThisConstructorCallInfo(final UnresolvedClassTypeInfo classType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(classType, outerUnit, fromLine, fromColumn, toLine, toColumn);
    }
}
