package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決コンストラクタを表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedConstructorInfo extends
        UnresolvedCallableUnitInfo<TargetConstructorInfo> {

    /**
     * 必要な情報を与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnresolvedConstructorInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * 名前解決を行う
     */
    @Override
    public TargetConstructorInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 修飾子，名前，返り値，行数を取得
        final Set<ModifierInfo> methodModifiers = this.getModifiers();
        final int constructorFromLine = this.getFromLine();
        final int constructorFromColumn = this.getFromColumn();
        final int constructorToLine = this.getToLine();
        final int constructorToColumn = this.getToColumn();

        this.resolvedInfo = new TargetConstructorInfo(methodModifiers, constructorFromLine,
                constructorFromColumn, constructorToLine, constructorToColumn);

        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(null, null,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(ownerClass);

        // 型パラメータを解決し，解決済みコンストラクタ情報に追加する
        // ここではextends節は解決しない
        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.resolve(ownerClass,
                    this.resolvedInfo, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addTypeParameter(typeParameter);
        }

        return this.resolvedInfo;
    }

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーなので true を返す
     */
    @Override
    public final boolean isInstanceMember() {
        return true;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーではないので false を返す
     */
    @Override
    public final boolean isStaticMember() {
        return false;
    }
}
