package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 for ブロックを表すクラス
 * 
 * @author higo
 */
public final class UnresolvedForBlockInfo extends UnresolvedConditionalBlockInfo<ForBlockInfo> {

    /**
     * 外側のブロック情報を与えて，for ブロック情報を初期化
     * 
     * @param outerSpace 外側のブロック
     */
    public UnresolvedForBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);

        this.initializerExpressions = new HashSet<UnresolvedConditionInfo<? extends ConditionInfo>>();
        this.iteratorExpressions = new HashSet<UnresolvedExpressionInfo<? extends ExpressionInfo>>();
    }

    /**
     * この未解決 for ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public ForBlockInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この for文の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ForBlockInfo(fromLine, fromColumn, toLine, toColumn);

        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        this.resolvedInfo.setOuterUnit(outerSpace);

        return this.resolvedInfo;
    }

    /**
     * このローカル領域のインナー領域を名前解決する
     * 
     * @param usingClass この領域が存在しているクラス
     * @param usingMethod この領域が存在しているメソッド
     * @param classInfoManager クラスマネージャ
     * @param fieldInfoManager フィールドマネージャ
     * @param methodInfoManager メソッドマネージャ
     */
    @Override
    public final void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        super.resolveInnerBlock(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);

        // 未解決初期化式情報を解決し，解決済みオブジェクトに追加
        for (final UnresolvedConditionInfo<? extends ConditionInfo> unresolvedInitializer : this.initializerExpressions) {
            final ConditionInfo initializer = unresolvedInitializer.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addInitializerExpressions(initializer);
        }

        // 未解決更新式情報を追加し，解決済みオブジェクトに追加
        for (final UnresolvedExpressionInfo<? extends ExpressionInfo> unresolvedUpdater : this.iteratorExpressions) {
            final ExpressionInfo update = unresolvedUpdater.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addIteratorExpressions(update);
        }
    }

    /**
     * 初期化式を追加するメソッド
     * 
     * @param initializerExpression 追加する初期化式
     */
    public final void addInitializerExpression(
            final UnresolvedConditionInfo<? extends ConditionInfo> initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == initializerExpression) {
            throw new IllegalArgumentException("initailizerExpression is null.");
        }

        this.initializerExpressions.add(initializerExpression);
    }

    /**
     * 更新式を追加するメソッド
     * 
     * @param iteratorExpression 追加する更新式
     */
    public final void addIteratorExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> iteratorExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null.");
        }

        this.iteratorExpressions.add(iteratorExpression);
    }

    public Set<UnresolvedConditionInfo<? extends ConditionInfo>> getInitializerExpressions() {
        return Collections.unmodifiableSet(this.initializerExpressions);
    }

    public Set<UnresolvedExpressionInfo<? extends ExpressionInfo>> getIteratorExpressions() {
        return Collections.unmodifiableSet(this.iteratorExpressions);
    }

    private final Set<UnresolvedConditionInfo<? extends ConditionInfo>> initializerExpressions;

    private final Set<UnresolvedExpressionInfo<? extends ExpressionInfo>> iteratorExpressions;

}
