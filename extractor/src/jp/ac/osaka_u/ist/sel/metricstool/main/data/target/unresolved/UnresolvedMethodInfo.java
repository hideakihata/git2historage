package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 一度目のASTパースで取得したメソッド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedMethodInfo extends UnresolvedCallableUnitInfo<TargetMethodInfo>
        implements StaticOrInstance {

    /**
     * 未解決メソッド定義情報オブジェクトを初期化
     * @param ownerClass このメソッドを宣言しているクラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedMethodInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        this.methodName = null;
        this.returnType = null;
        this.resolvedInfo = null;
    }

    /**
     * 未解決メソッド情報を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決メソッド情報の定義があるクラス
     * @param usingMethod 未解決メソッド情報の定義があるメソッド（このメソッドが呼ばれる場合は通常 null がセットされているはず）
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済みメソッド情報
     */
    @Override
    public TargetMethodInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 修飾子，名前，行数，可視性，インスタンスメンバーかどうかを取得
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final String methodName = this.getMethodName();

        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // MethodInfo オブジェクトを生成する．
        this.resolvedInfo = new TargetMethodInfo(methodModifiers, methodName, instance, fromLine,
                fromColumn, toLine, toColumn);

        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(ownerClass);

        // 型パラメータを解決し，解決済みメソッド情報に追加する
        // 型パラメータのextends節はここでは解決しない
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(ownerClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        return this.resolvedInfo;
    }

    /**
     * 未解決返り値情報を解決する
     * すでにresolveメソッドが呼び出された状態で用いなければならない
     * 
     * @param classInfoManager
     * @return
     */
    public final TargetMethodInfo resolveReturnType(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetMethodInfo resolved = this.getResolved();
        final TargetClassInfo owernClass = this.getOwnerClass().getResolved();

        final UnresolvedTypeInfo<? extends TypeInfo> unresolvedType = this.getReturnType();
        final TypeInfo type = unresolvedType.resolve(owernClass, resolved, classInfoManager, null,
                null);
        resolved.setReturnType(type);

        return resolved;

    }

    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * メソッド名をセットする
     * 
     * @param methodName メソッド名
     */
    public void setMethodName(final String methodName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodName) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
    }

    /**
     * メソッドの返り値の型を返す
     * 
     * @return メソッドの返り値の型
     */
    public UnresolvedTypeInfo<?> getReturnType() {
        return this.returnType;
    }

    /**
     * メソッドの返り値をセットする
     * 
     * @param returnType メソッドの返り値
     */
    public void setReturnType(final UnresolvedTypeInfo<?> returnType) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
    }

    /**
     * メソッド名を保存するための変数
     */
    private String methodName;

    /**
     * メソッドの返り値を保存するための変数
     */
    private UnresolvedTypeInfo<?> returnType;
}
