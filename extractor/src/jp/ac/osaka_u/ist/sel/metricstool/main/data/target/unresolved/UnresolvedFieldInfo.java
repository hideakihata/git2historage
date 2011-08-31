package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticOrInstance;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ASTパースで取得したフィールド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author higo
 * 
 */
public final class UnresolvedFieldInfo extends
        UnresolvedVariableInfo<TargetFieldInfo, UnresolvedClassInfo> implements Visualizable,
        StaticOrInstance {

    /**
     * Unresolvedフィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     * @param definitionClass フィールドを定義しているクラス
     * @param initializer フィールドの初期化式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo<?> type,
            final UnresolvedClassInfo definitionClass,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(name, type, definitionClass, fromLine, fromColumn, toLine, toColumn);

        if (null == definitionClass) {
            throw new NullPointerException();
        }

        this.ownerClass = definitionClass;
        this.initializer = initializer;
    }

    @Override
    public TargetFieldInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 所有クラスを解決
        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // 修飾子，名前，可視性，インスタンスメンバーかどうかを取得
        // 型のみここでは解決しない
        final Set<ModifierInfo> modifiers = this.getModifiers();
        final String fieldName = this.getName();
        final boolean instance = this.isInstanceMember();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new TargetFieldInfo(modifiers, fieldName, ownerClass, instance,
                fromLine, fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    public TargetFieldInfo resolveType(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final TargetFieldInfo resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        final UnresolvedTypeInfo<?> unresolvedType = this.getType();
        final TypeInfo type = unresolvedType
                .resolve(ownerClass, null, classInfoManager, null, null);
        resolved.setType(type);

        return resolved;
    }

    /**
     * このフィールドを定義している未解決クラス情報を返す
     * 
     * @return このフィールドを定義している未解決クラス情報
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このフィールドを定義している未解決クラス情報をセットする
     * 
     * @param ownerClass このフィールドを定義している未解決クラス情報
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    @Override
    public boolean isInheritanceVisible() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isInheritanceVisible(this
                .getModifiers());
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    @Override
    public boolean isNamespaceVisible() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isNamespaceVisible(this
                .getModifiers());
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    @Override
    public boolean isPublicVisible() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isPublicVisible(getModifiers());
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    @Override
    public boolean isInstanceMember() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? false : ModifierInfo
                .isInstanceMember(this.getModifiers());
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    @Override
    public boolean isStaticMember() {
        final UnresolvedClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isStaticMember(this.getModifiers());
    }

    /**
     * 変数の初期化式を返す
     * 
     * @return 変数の初期化式．初期化されていない場合はnull
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitilizer() {
        return this.initializer;
    }

    /**
     * このフィールドを定義しているクラスを保存するための変数
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * 変数の初期化式を表す変数
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializer;

}
