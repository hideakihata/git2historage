package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * ラベル定義を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class LabelInfo extends UnitInfo implements StatementInfo {

    /**
     * 位置情報を与えてラベルオブジェクトを初期化
     * 
     * @param name ラベル名
     * @param labeledStatement このラベルが付いた文
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public LabelInfo(final String name, final StatementInfo labeledStatement, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        this.name = name;
        this.labeledStatement = labeledStatement;
    }

    /**
     * この文（ラベル定義）で用いられている変数利用の一覧を返す．
     * どの変数も用いられていないので，空のsetが返される
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
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
     * このラベル付き文のテキスト表現（型）を返す
     * 
     * @return このラベル付き文のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.getName());
        sb.append(" : ");
        return sb.toString();
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
     * このラベルが付いた文を返す
     * 
     * @return このラベルが付いた文
     */
    public StatementInfo getLabeledStatement() {
        return this.labeledStatement;
    }

    @Override
    public final LocalSpaceInfo getOwnerSpace() {
        return this.labeledStatement.getOwnerSpace();
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.labeledStatement.getOwnerMethod();
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

    /**
     * このラベルのハッシュコードを返す
     */
    @Override
    public final int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public ExecutableElementInfo copy() {

        final String name = this.getName();
        final StatementInfo statement = (StatementInfo) this.getLabeledStatement().copy();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LabelInfo newLabel = new LabelInfo(name, statement, fromLine,
                fromColumn, toLine, toColumn);

        return newLabel;
    }

    /**
     * ラベルの名前を表す変数
     */
    private final String name;

    /**
     * このラベルが付いた文を表す変数を返す
     */
    private final StatementInfo labeledStatement;
}
