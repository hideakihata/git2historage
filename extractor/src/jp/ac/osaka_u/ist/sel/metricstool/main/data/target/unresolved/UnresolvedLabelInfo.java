package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決ラベル定義を表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedLabelInfo extends UnresolvedUnitInfo<LabelInfo> implements
        UnresolvedStatementInfo<LabelInfo> {

    /**
     * ラベル名を与えて，オブジェクトを初期化
     * 
     * @param name ラベル名
     */
    public UnresolvedLabelInfo() {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.labeledStatement = null;
        this.resolvedInfo = null;
    }

    /**
     * 名前解決を行うメソッド
     */
    @Override
    public LabelInfo resolve(final TargetClassInfo usingClass, final CallableUnitInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // このラベルの位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // このラベルの名前を取得
        final String name = this.getName();

        // このラベルが付いた文を取得
        final UnresolvedStatementInfo<?> unresolvedLabeledStatement = this.getLabeledStatement();
        final StatementInfo labeledStatement = unresolvedLabeledStatement.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new LabelInfo(name, labeledStatement, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * このラベルの名前を返す
     * 
     * @return このラベルの名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * このラベルの名前をセットする
     * @param name このラベルの名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * このラベルが付いた文をセットする
     * 
     * @param labeledStatement このラベルが付いた文
     */
    public void setLabeledStatement(final UnresolvedStatementInfo<?> labeledStatement) {
        this.labeledStatement = labeledStatement;
    }

    /**
     * このラベルが付いた文を返す
     * 
     * @return このラベルが付いた文
     */
    public UnresolvedStatementInfo<?> getLabeledStatement() {
        return this.labeledStatement;
    }

    @Override
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {
        final UnresolvedLocalSpaceInfo<?> outerUnit = (UnresolvedLocalSpaceInfo<?>) this
                .getOuterUnit();
        return outerUnit instanceof UnresolvedCallableUnitInfo<?> ? (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outerUnit
                : outerUnit.getOuterCallableUnit();
    }

    @Override
    public UnresolvedClassInfo getOuterClass() {
        return this.getOuterCallableUnit().getOuterClass();
    }

    @Override
    public UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit() {
        return this.outerUnit;
    }

    @Override
    public void setOuterUnit(UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }
        this.outerUnit = outerUnit;
    }

    private String name;

    private UnresolvedStatementInfo<?> labeledStatement;

    private UnresolvedUnitInfo<? extends UnitInfo> outerUnit;
}
