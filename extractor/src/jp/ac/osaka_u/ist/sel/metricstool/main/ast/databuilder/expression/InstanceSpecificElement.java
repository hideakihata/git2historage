package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedNullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


public class InstanceSpecificElement extends ExpressionElement {

    private enum SPECIFIC_ELEMENT_TYPE {
        NULL, THIS
    }

    private InstanceSpecificElement(final UnresolvedExpressionInfo<? extends ExpressionInfo> usage,
            final SPECIFIC_ELEMENT_TYPE elementType) {
        // TODO 0‚Å‚È‚¢‚Ì‚ð‚¢‚ê‚é‚×‚«?
        super(usage);

        this.elementType = elementType;
    }

    public static InstanceSpecificElement getThisInstanceType(BuildDataManager buildManager,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        final UnresolvedClassReferenceInfo thisInstance = buildManager.getCurrentClass()
                .getClassReference(buildManager.getCurrentUnit(), fromLine, fromColumn, toLine,
                        toColumn);

        return new InstanceSpecificElement(thisInstance, SPECIFIC_ELEMENT_TYPE.THIS);
    }

    public static InstanceSpecificElement getNullElement(
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedNullUsageInfo nullUsage = new UnresolvedNullUsageInfo();
        nullUsage.setOuterUnit(outerUnit);
        nullUsage.setFromLine(fromLine);
        nullUsage.setFromColumn(fromColumn);
        nullUsage.setToLine(toLine);
        nullUsage.setToColumn(toColumn);

        return new InstanceSpecificElement(nullUsage, SPECIFIC_ELEMENT_TYPE.NULL);
    }

    public final boolean isNullElement() {
        return this.elementType.equals(SPECIFIC_ELEMENT_TYPE.NULL);
    }

    public final boolean isThisInstance() {
        return this.elementType.equals(SPECIFIC_ELEMENT_TYPE.THIS);
    }

    private final SPECIFIC_ELEMENT_TYPE elementType;
}
