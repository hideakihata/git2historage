package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


/**
 * @author kou-tngt
 *
 */
public class TypeElement extends ExpressionElement {

    public TypeElement getArrayDimensionInclementedInstance(
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        UnresolvedTypeInfo<? extends TypeInfo> newType = null;
        if (this.type instanceof UnresolvedArrayTypeInfo) {
            newType = ((UnresolvedArrayTypeInfo) this.type).getDimensionInclementedArrayType();
        } else {
            newType = UnresolvedArrayTypeInfo.getType(this.type, 1);
        }

        return new TypeElement(newType, outerUnit, fromLine, fromColumn, toLine, toColumn);
    }

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return this.type;
    }

    public TypeElement(final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> type,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        this((UnresolvedTypeInfo<? extends TypeInfo>) type, outerUnit, fromLine, fromColumn,
                toLine, toColumn);
    }

    private TypeElement(final UnresolvedTypeInfo<? extends TypeInfo> type,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == type) {
            throw new NullPointerException("type is null.");
        }
        this.type = type;

        if (this.type instanceof PrimitiveTypeInfo) {
            this.usage = new UnresolvedLiteralUsageInfo("", (PrimitiveTypeInfo) this.type);
            this.usage.setOuterUnit(outerUnit);
        }
    }

    public TypeElement(final UnresolvedLiteralUsageInfo literal) {
        if (null == literal) {
            throw new IllegalArgumentException();
        }

        this.type = literal.getType();
        this.usage = literal;
    }

    public static final TypeElement getBuiltinTypeElement(final BuiltinTypeToken token,
            final UnresolvedUnitInfo<?> outerUnit) {
        if (BUILTIN_TYPE_CACHE.containsKey(token)) {
            return BUILTIN_TYPE_CACHE.get(token);
        } else {
            final TypeElement builtinTypeElement = new TypeElement(token.getType(), outerUnit, 0,
                    0, 0, 0);
            BUILTIN_TYPE_CACHE.put(token, builtinTypeElement);
            return builtinTypeElement;
        }
    }

    private static final ConcurrentMap<BuiltinTypeToken, TypeElement> BUILTIN_TYPE_CACHE = new ConcurrentHashMap<BuiltinTypeToken, TypeElement>();

    private final UnresolvedTypeInfo<? extends TypeInfo> type;

}
