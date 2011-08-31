package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


@SuppressWarnings("serial")
public class CaughtExceptionDeclarationStatementInfo implements ExecutableElementInfo {

    /**
     * 宣言されているパラメータ，位置情報を与えて初期化
     * 
     * @param catchBlock
     *            宣言されているパラメータ
     * @param fromLine
     *            開始行
     * @param fromColumn
     *            開始列
     * @param toLine
     *            終了行
     * @param toColumn
     *            終了列
     */
    public CaughtExceptionDeclarationStatementInfo(final CatchBlockInfo catchBlock,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        this.catchBlock = catchBlock;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        final TypeInfo type = this.getCaughtVariable().getType();
        text.append(type.getTypeName());

        text.append(" ");

        text.append(this.getCaughtVariable().getName());

        return text.toString();
    }

    @Override
    public Set<CallInfo<?>> getCalls() {
        return CallInfo.EmptySet;
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> variables = new HashSet<VariableInfo<? extends UnitInfo>>();
        variables.add(this.catchBlock.getCaughtException());
        return Collections.unmodifiableSet(variables);
    }

    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
    }

    public LocalVariableInfo getCaughtVariable() {
        return this.catchBlock.getCaughtException();
    }

    @Override
    public CallableUnitInfo getOwnerMethod() {
        return this.catchBlock.getOwnerMethod();
    }

    @Override
    public LocalSpaceInfo getOwnerSpace() {
        return this.catchBlock;
    }

    @Override
    public int getFromColumn() {
        return this.fromColumn;
    }

    @Override
    public int getFromLine() {
        return this.fromLine;
    }

    @Override
    public int getToColumn() {
        return this.toColumn;
    }

    @Override
    public int getToLine() {
        return this.toLine;
    }

    @Override
    public int compareTo(Position o) {
        return this.catchBlock.compareTo(o);
    }

    @Override
    public ExecutableElementInfo copy() {

        this.getOwnerMethod();
        final CatchBlockInfo outerUnit = (CatchBlockInfo) this.getOwnerSpace();
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final CaughtExceptionDeclarationStatementInfo newStatement = new CaughtExceptionDeclarationStatementInfo(
                outerUnit, fromLine, fromColumn, toLine, toColumn);

        return newStatement;
    }

    private final CatchBlockInfo catchBlock;

    private final int fromLine;

    private final int fromColumn;

    private final int toLine;

    private final int toColumn;
}
