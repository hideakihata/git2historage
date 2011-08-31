package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.FieldStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


public class FieldBuilder extends
        InitializableVariableBuilder<UnresolvedFieldInfo, UnresolvedClassInfo> {

    public FieldBuilder(BuildDataManager buildDataManager,
            final ExpressionElementManager expressionManager, ModifiersBuilder modifiersBuilder,
            TypeBuilder typeBuilder, NameBuilder nameBuilder) {
        super(buildDataManager, expressionManager, new FieldStateManager(), modifiersBuilder,
                typeBuilder, nameBuilder);

    }

    @Override
    protected UnresolvedFieldInfo buildVariable(final String[] name,
            final UnresolvedTypeInfo<? extends TypeInfo> type, final ModifierInfo[] modifiers,
            final UnresolvedClassInfo definitionClass, final int startLine, final int startColumn,
            final int endLine, final int endColumn) {
        String varName = "";
        if (name.length > 0) {
            varName = name[0];
        }

        if (null != definitionClass) {
            final UnresolvedFieldInfo field = new UnresolvedFieldInfo(varName, type,
                    definitionClass, this.builtInitializerStack.pop(), startLine, startColumn,
                    endLine, endColumn);
            for (ModifierInfo modifier : modifiers) {
                field.addModifier(modifier);
            }

            buildDataManager.addField(field);

            return field;
        }
        return null;
    }

    @Override
    protected UnresolvedClassInfo validateDefinitionSpace(
            final UnresolvedUnitInfo<? extends UnitInfo> definitionUnit) {
        return (definitionUnit instanceof UnresolvedClassInfo) ? (UnresolvedClassInfo) definitionUnit
                : null;
    }

}
