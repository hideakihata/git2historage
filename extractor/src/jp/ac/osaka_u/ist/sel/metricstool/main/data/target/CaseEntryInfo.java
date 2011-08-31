package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch 文の case エントリを表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class CaseEntryInfo extends UnitInfo implements StatementInfo {

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param ownerSwitchBlock この case エントリが属する switch ブロック
     * @param label この case エントリのラベル
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final ExpressionInfo label,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerSwitchBlock) || (null == label)) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = label;

        this.label.setOwnerExecutableElement(this);
    }

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param ownerSwitchBlock この case エントリが属する switch ブロック
     * @param breakStatement この case エントリが break 文を持つかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    protected CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = null;
    }

    /**
     * この文（case エントリ）で用いられている変数利用の一覧を返す．
     * どの変数も用いられていないので，空のsetが返される
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * 変数定義のSetを返す
     * 
     * @return 変数定義のSetを返す
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return CallInfo.EmptySet;
    }

    /**
     * このcaseエントリのテキスト表現（String型）を返す
     * 
     * @return このcaseエントリのテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("case ");

        final ExpressionInfo label = this.getLabel();
        sb.append(label.getText());

        sb.append(":");

        return sb.toString();
    }

    /**
     * この case エントリが属する switch ブロックを返す
     * 
     * @return この case エントリが属する switch ブロック
     */
    public final SwitchBlockInfo getOwnerSwitchBlock() {
        return this.ownerSwitchBlock;
    }

    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.getOwnerSwitchBlock();
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.getOwnerSwitchBlock().getOwnerMethod();
    }

    /**
     * この case エントリののラベルを返す
     * 
     * @return この case エントリのラベル
     */
    public final ExpressionInfo getLabel() {
        return this.label;
    }

    /**
     * caseエントリのハッシュコードを返す
     */
    @Override
    public final int hashCode() {
        return this.getOwnerSwitchBlock().hashCode()
                + ((null != this.getLabel()) ? this.getLabel().hashCode() : 0);
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    @Override
    public ExecutableElementInfo copy() {

        final SwitchBlockInfo ownerBlock = this.getOwnerSwitchBlock();
        final ExpressionInfo label = (ExpressionInfo) this.getLabel().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final CaseEntryInfo newCaseEntry = new CaseEntryInfo(ownerBlock, label, fromLine,
                fromColumn, toLine, toColumn);

        return newCaseEntry;
    }

    /**
     * この case エントリが属する switch ブロックを保存するための変数
     */
    private final SwitchBlockInfo ownerSwitchBlock;

    /**
     * この case エントリのラベルを保存する変数
     */
    private ExpressionInfo label;
}
