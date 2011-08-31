package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 条件文の条件節の未解決情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedConditionalClauseInfo extends UnresolvedUnitInfo<ConditionalClauseInfo> {

    /**
     * 条件節に対応する条件文の未解決情報を与えて初期化
     * 
     * @param ownerConditionalBlockInfo 条件節に対応する条件文の未解決情報
     * @param condition 条件文
     */
    public UnresolvedConditionalClauseInfo(
            final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerConditionalBlockInfo,
            final UnresolvedConditionInfo<? extends ConditionInfo> condition) {
        super();

        if (null == ownerConditionalBlockInfo) {
            throw new IllegalArgumentException("conditionalBlock is null.");
        }

        this.condition = condition;
        this.ownerConditionalBlock = ownerConditionalBlockInfo;
    }

    @Override
    public ConditionalClauseInfo resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final ConditionalBlockInfo ownerConditionalBlock = this.ownerConditionalBlock.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final ConditionInfo condition = null != this.condition ? this.condition.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager) : null;

        this.resolvedInfo = new ConditionalClauseInfo(ownerConditionalBlock, condition, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    public UnresolvedConditionInfo<? extends ConditionInfo> getCondition() {
        return this.condition;
    }

    /**
     * 条件文の条件節の未解決情報を表す変数
     */
    private final UnresolvedConditionalBlockInfo<? extends ConditionalBlockInfo> ownerConditionalBlock;

    /**
     * 条件節に記述されている条件の未解決情報を表す変数
     */
    private final UnresolvedConditionInfo<? extends ConditionInfo> condition;

}
