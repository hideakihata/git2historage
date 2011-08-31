package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThisConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedClassConstructorCallInfo extends
        UnresolvedConstructorCallInfo<UnresolvedClassTypeInfo, ClassConstructorCallInfo> {

    public UnresolvedClassConstructorCallInfo(final UnresolvedClassTypeInfo classType) {
        super(classType);
    }

    public UnresolvedClassConstructorCallInfo(final UnresolvedClassTypeInfo classType,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, 
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(classType, outerUnit, fromLine, fromColumn, toLine, toColumn);
    }
    
    /**
     * 名前解決を行う
     */
    @Override
    public ClassConstructorCallInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // コンストラクタのシグネチャを取得
        final List<ExpressionInfo> actualParameters = super.resolveArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final List<ReferenceTypeInfo> typeArguments = super.resolveTypeArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //　コンストラクタの型を解決
        final UnresolvedClassTypeInfo unresolvedReferenceType = this.getReferenceType();
        final ClassTypeInfo classType = (ClassTypeInfo) unresolvedReferenceType.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final List<ConstructorInfo> constructors = NameResolver.getAvailableConstructors(classType);

        for (final ConstructorInfo constructor : constructors) {

            if (constructor.canCalledWith(actualParameters)) {
                if (this instanceof UnresolvedThisConstructorCallInfo) {
                    this.resolvedInfo = new ThisConstructorCallInfo(classType, constructor,
                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                } else if (this instanceof UnresolvedSuperConstructorCallInfo) {
                    this.resolvedInfo = new SuperConstructorCallInfo(classType, constructor,
                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                } else {
                    this.resolvedInfo = new ClassConstructorCallInfo(classType, constructor,
                            usingMethod, fromLine, fromColumn, toLine, toColumn);
                }
                this.resolvedInfo.addArguments(actualParameters);
                this.resolvedInfo.addTypeArguments(typeArguments);
                return this.resolvedInfo;
            }
        }

        // 対象クラスに定義されたコンストラクタで該当するものがないので，外部クラスに定義されたコンストラクタを呼び出していることにする
        {
            ClassInfo classInfo = classType.getReferencedClass();
            if (classInfo instanceof TargetClassInfo) {
                classInfo = NameResolver.getExternalSuperClass(classInfo);
            }
            final ExternalConstructorInfo constructor = new ExternalConstructorInfo();
            if (null != classInfo) {
                constructor.setOuterUnit(classInfo);
            } else {
                constructor.setOuterUnit(ExternalClassInfo.UNKNOWN);
            }
            final List<ParameterInfo> externalParameters = ExternalParameterInfo.createParameters(
                    actualParameters, constructor);
            constructor.addParameters(externalParameters);
            this.resolvedInfo = new ClassConstructorCallInfo(classType, constructor, usingMethod,
                    fromLine, fromColumn, toLine, toColumn);
            this.resolvedInfo.addArguments(actualParameters);
            this.resolvedInfo.addTypeArguments(typeArguments);
            return this.resolvedInfo;
        }
    }
}
