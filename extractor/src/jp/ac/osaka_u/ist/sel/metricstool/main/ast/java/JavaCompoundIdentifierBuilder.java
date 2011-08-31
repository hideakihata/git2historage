package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.CompoundIdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.FieldOrMethodElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.IdentifierElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.InstanceSpecificElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.UsageElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFullQualifiedNameClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


/**
 * @author kou-tngt, t-miyake
 *
 */
public class JavaCompoundIdentifierBuilder extends CompoundIdentifierBuilder {
    public JavaCompoundIdentifierBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildManager) {
        super(expressionManager, buildManager);
        this.buildDataManager = buildManager;
    }

    @Override
    protected void buildCompoundIdentifierElement(ExpressionElement[] elements)
            throws ASTParseException {

        assert (2 == elements.length) : "Illega state: two element must be usable.";

        ExpressionElement left = elements[0];
        ExpressionElement right = elements[1];
        if (right instanceof JavaExpressionElement && ((JavaExpressionElement) right).isClass()) {
            UnresolvedClassReferenceInfo classReference = UnresolvedClassReferenceInfo
                    .createClassReference(JAVA_LANG_CLASS, this.buildDataManager.getCurrentUnit(),
                            right.getFromLine(), right.getFromColumn(), right.getToLine(),
                            right.getToColumn());
            pushElement(new UsageElement(classReference));
        } else if (right instanceof InstanceSpecificElement) {

            // 右側の要素がthisで左側に識別子がある場合，外部クラスのインスタンスの参照
            if (((InstanceSpecificElement) right).isThisInstance()) {
                UnresolvedClassInfo classInfo = getSpecifiedOuterClass((IdentifierElement) left);

                if (classInfo != null) {
                    pushElement(new UsageElement(classInfo.getClassReference(
                            this.buildDataManager.getCurrentUnit(), right.getFromLine(),
                            right.getFromColumn(), right.getToLine(), right.getToColumn())));
                } else {
                    assert (false) : "Illegal state: specified this class "
                            + ((IdentifierElement) left).getName()
                            + " was not found from outer classes.";
                }
            }

        } else if (left instanceof JavaExpressionElement
                && ((JavaExpressionElement) left).isSuper()) {
            if (right instanceof IdentifierElement) {
                final IdentifierElement rightIdentifier = (IdentifierElement) right;

                UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
                UnresolvedClassTypeInfo superClassType = classInfo.getSuperClasses().iterator()
                        .next();
                UnresolvedClassReferenceInfo superClassReference = UnresolvedClassReferenceInfo
                        .createClassReference(superClassType,
                                this.buildDataManager.getCurrentUnit(), left.getFromLine(),
                                left.getFromColumn(), left.getToLine(), left.getToColumn());

                final FieldOrMethodElement fieldOrMethod = new FieldOrMethodElement(
                        superClassReference, rightIdentifier.getName(),
                        rightIdentifier.getFromLine(), rightIdentifier.getFromColumn(),
                        rightIdentifier.getToLine(), rightIdentifier.getToColumn());
                pushElement(fieldOrMethod);
            }
        } else if (right instanceof JavaExpressionElement
                && ((JavaExpressionElement) right).isSuper()) {
            UnresolvedClassInfo classInfo = null;
            if (left instanceof IdentifierElement) {
                //まず変数名.super()というコンストラクタ呼び出しかどうかを確認する
                IdentifierElement identifier = (IdentifierElement) left;
                UnresolvedExpressionInfo<? extends ExpressionInfo> ownerUsage = identifier
                        .resolveReferencedEntityIfPossible(buildDataManager);
                UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> variable = null;
                if (null != ownerUsage && ownerUsage instanceof UnresolvedVariableUsageInfo) {
                    UnresolvedVariableUsageInfo<?> variableUsage = (UnresolvedVariableUsageInfo<?>) ownerUsage;
                    variable = buildDataManager.getCurrentScopeVariable(variableUsage
                            .getUsedVariableName());
                }

                if (null != variable) {
                    //変数が見つかった

                    boolean match = false;
                    UnresolvedClassInfo currentClass = buildDataManager.getCurrentClass();
                    UnresolvedClassTypeInfo currentSuperClass = currentClass.getSuperClasses()
                            .iterator().next();
                    String[] names = null;
                    if (null != currentSuperClass) {
                        //names = currentSuperClass.getFullReferenceName();
                        names = currentSuperClass.getReferenceName();
                    }
                    if (null != names && variable.getType() instanceof UnresolvedClassTypeInfo) {
                        // TODO UnresolvedReferenceTypeにすべきかも 要テスト
                        UnresolvedClassTypeInfo variableType = (UnresolvedClassTypeInfo) variable
                                .getType();
                        for (String name : names) {
                            if (name.equals(variableType.getTypeName())) {
                                match = true;
                                break;
                            }
                        }
                    }

                    if (match) {
                        classInfo = currentClass;
                    }
                }

                if (null == classInfo) {
                    //変数名.superという呼び出しとして解決しようとしてみたけど無理だったので
                    //OuterClass.super.method()というメソッド呼び出しのようだ
                    classInfo = getSpecifiedOuterClass((IdentifierElement) left);
                }

                if (null == classInfo) {
                    // 該当クラスが見当たらないのでとりあえず現在のクラスのスーパークラスと判断する
                    classInfo = buildDataManager.getCurrentClass();
                }
            } else if (left.getUsage() instanceof UnresolvedFullQualifiedNameClassReferenceInfo) {
                classInfo = ((UnresolvedFullQualifiedNameClassReferenceInfo) left.getUsage())
                        .getReferencedClass();
            } else {
                classInfo = buildDataManager.getCurrentClass();
            }

            final UnresolvedClassTypeInfo superClassType = classInfo.getSuperClasses().iterator()
                    .next();
            if (superClassType != null) {
                pushElement(new UsageElement(superClassType.getUsage(
                        this.buildDataManager.getCurrentUnit(), right.getFromLine(),
                        right.getFromColumn(), right.getToLine(), right.getToColumn())));
            }
        } else {
            super.buildCompoundIdentifierElement(elements);
        }
    }

    private UnresolvedClassInfo getSpecifiedOuterClass(IdentifierElement identifier)
            throws ASTParseException {
        String name = identifier.getName();
        UnresolvedClassInfo currentClass = buildDataManager.getCurrentClass();
        while (null != currentClass && !name.equals(currentClass.getClassName())) {
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit = currentClass.getOuterUnit();
            if (outerUnit instanceof UnresolvedClassInfo) {
                currentClass = (UnresolvedClassInfo) outerUnit;
            } else if (null == outerUnit) {
                currentClass = null;
            } else if (outerUnit instanceof UnresolvedCallableUnitInfo<?>) {
                UnresolvedCallableUnitInfo<?> outerCallable = (UnresolvedCallableUnitInfo<CallableUnitInfo>) outerUnit;
                currentClass = outerCallable.getOwnerClass();
            } else {
                // TODO 今のところメソッド内で宣言されたクラスのコンストラクタ内で"識別子.super()"という構文はサポートしていない
                throw new ASTParseException("unsupported super constructor call");
            }
        }
        return currentClass;
    }

    private final static UnresolvedClassTypeInfo JAVA_LANG_CLASS = new UnresolvedClassTypeInfo(
            new LinkedList<UnresolvedClassImportStatementInfo>(), new String[] { "java", "lang",
                    "Class" });

    private final BuildDataManager buildDataManager;

}
