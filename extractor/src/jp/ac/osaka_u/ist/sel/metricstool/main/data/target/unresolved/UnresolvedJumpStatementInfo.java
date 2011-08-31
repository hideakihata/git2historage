package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class UnresolvedJumpStatementInfo<T extends JumpStatementInfo> extends
        UnresolvedSingleStatementInfo<T> {

    public UnresolvedJumpStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace) {
        this(ownerSpace, 0, 0, 0, 0);
    }

    public UnresolvedJumpStatementInfo(
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> ownerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public T resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalSpaceInfo ownerSpace = this.getOuterLocalSpace().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final LabelInfo destinationLabel = null != this.getDestinationLabel() ? this
                .getDestinationLabel().resolve(usingClass, usingMethod, classInfoManager,
                        fieldInfoManager, methodInfoManager) : null;
        this.resolvedInfo = this.createResolvedInfo(ownerSpace, destinationLabel, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    protected abstract T createResolvedInfo(final LocalSpaceInfo ownerSpace,
            final LabelInfo destinationLabel, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn);

    public void setDestinationLabel(UnresolvedLabelInfo destinationLabel) {
        this.destinationLabel = destinationLabel;
    }

    public UnresolvedLabelInfo getDestinationLabel() {
        return destinationLabel;
    }

    private UnresolvedLabelInfo destinationLabel;
}
