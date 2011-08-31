package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * catch ブロック情報を表すクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class CatchBlockInfo extends BlockInfo implements SubsequentialBlockInfo<TryBlockInfo> {

    /**
     * ExecutableElementと例外を入力として，そのExecutableElementを含むtry文に対応して，exceptionをキャッチするcatchBlockを返す．
     * もし，そのようなキャッチ節がない場合はnull を返す．
     * 
     * @param element
     * @param exception
     * @return
     */
    public static CatchBlockInfo getCorrespondingCatchBlock(final ExecutableElementInfo element,
            final ReferenceTypeInfo exception) {

        for (LocalSpaceInfo ownerSpace = element.getOwnerSpace(); ownerSpace instanceof BlockInfo; ownerSpace = ((BlockInfo) ownerSpace)
                .getOwnerSpace()) {

            if (ownerSpace instanceof TryBlockInfo) {
                for (final CatchBlockInfo catchBlock : ((TryBlockInfo) ownerSpace)
                        .getSequentCatchBlocks()) {
                    final VariableInfo<?> caughtVariable = catchBlock.getCaughtException();
                    if (exception.equals(caughtVariable.getType())) {
                        return catchBlock;
                    }
                }

            }
        }

        return null;
    }

    /**
     * 対応する try ブロック情報を与えて catch ブロックを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public CatchBlockInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * 対応する try ブロックを返す
     * このメソッドは将来廃止されるため，使用は推奨されない
     * {@link CatchBlockInfo#getOwnerBlock()} を使用すべきである．
     * 
     * @return 対応する try ブロック
     * @deprecated
     */
    public final TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    @Override
    public TryBlockInfo getOwnerBlock() {
        assert null != this.ownerTryBlock : "this.ownerTryBlock must not be null!";
        return this.ownerTryBlock;
    }

    @Override
    public void setOwnerBlock(final TryBlockInfo ownerTryBlock) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        if (null != this.ownerTryBlock) {
            throw new IllegalStateException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * catchする例外を表す変数の情報を返す
     * @return catchする例外を表す変数の情報
     */
    public final LocalVariableInfo getCaughtException() {
        assert null != this.caughtException : "this.caughtException must not be null!";
        return this.caughtException;
    }

    /**
     * このcatch節で受ける例外をセットする
     *  
     * @param caughtException このcatch節で受ける例外
     */
    public void setCaughtException(final LocalVariableInfo caughtException) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caughtException) {
            throw new NullPointerException();
        }

        if (null != this.caughtException) {
            throw new IllegalStateException();
        }

        this.caughtException = caughtException;
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>(
                super.getDefinedVariables());
        definedVariables.add(this.caughtException);
        return Collections.unmodifiableSet(definedVariables);

    }

    /**
     * このcatch　文のテキスト表現（String型）を返す
     * 
     * @return このcatch　文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("catch (");

        final LocalVariableInfo caughtException = this.getCaughtException();
        sb.append(caughtException.getType().getTypeName());
        sb.append(" ");
        sb.append(caughtException.getName());

        sb.append(") {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }

    @Override
    public ExecutableElementInfo copy() {

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final CatchBlockInfo newCatchBlock = new CatchBlockInfo(fromLine, fromColumn, toLine,
                toColumn);

        final TryBlockInfo ownerTryBlock = this.getOwnerBlock();
        newCatchBlock.setOwnerBlock(ownerTryBlock);

        final LocalVariableInfo caughtException = this.getCaughtException();
        newCatchBlock.setCaughtException(caughtException);

        final UnitInfo outerUnit = this.getOuterUnit();
        newCatchBlock.setOuterUnit(outerUnit);

        for (final StatementInfo statement : this.getStatementsWithoutSubsequencialBlocks()) {
            newCatchBlock.addStatement((StatementInfo) statement.copy());
        }

        return newCatchBlock;
    }

    private TryBlockInfo ownerTryBlock;

    private LocalVariableInfo caughtException;
}
