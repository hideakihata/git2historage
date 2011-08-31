package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Random;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


public class CFGFieldInNode extends CFGDataNode<FieldUsageInfo> {

    public static CFGFieldInNode getInstance(final FieldInfo field, final CallableUnitInfo unit) {

        if (null == field || null == unit) {
            throw new IllegalArgumentException();
        }

        final Random generator = new Random();
        final int fromLine = unit.getFromLine();
        final int fromColumn = generator.nextInt(Integer.MAX_VALUE);
        final int toLine = unit.getFromLine();
        final int toColumn = generator.nextInt(Integer.MAX_VALUE);

        final ClassTypeInfo qualifierType = new ClassTypeInfo(field.getDefinitionUnit());
        final ExpressionInfo qualifierExpression = new ClassReferenceInfo(qualifierType, unit,
                fromLine, fromColumn, toLine, toColumn);

        final FieldUsageInfo fieldUsage = FieldUsageInfo.getInstance(qualifierExpression,
                qualifierType, field, false, true, unit, fromLine, fromColumn, toLine, toColumn);

        return new CFGFieldInNode(fieldUsage);
    }

    private CFGFieldInNode(final FieldUsageInfo fieldUsage) {
        super(fieldUsage);
    }

    @Override
    final ExpressionInfo getDissolvingTarget() {
        return null;
    }

    @Override
    FieldUsageInfo makeNewElement(final LocalSpaceInfo ownerSpace, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn,
            final ExpressionInfo... requiredExpressions) {
        return null;
    }

    @Override
    FieldUsageInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpressions) {
        return null;
    }
}
