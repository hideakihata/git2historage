package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ローカル領域(メソッドやメソッド内ブロック)を表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public abstract class LocalSpaceInfo extends UnitInfo implements HavingOuterUnit {

    /**
     * 必要な情報を与えてオブジェクトを居幾何
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn終了列
     */
    LocalSpaceInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.outerUnit = null;
        this.statements = new TreeSet<StatementInfo>();
    }

    /**
     * このローカルスペース内で定義された変数のSetを返す
     * 
     * @return このローカルスペース内で定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        for (final StatementInfo statement : this.getStatements()) {
            definedVariables.addAll(statement.getDefinedVariables());
        }
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * このローカル領域に文を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param statement 追加する文
     */
    public void addStatement(final StatementInfo statement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new IllegalArgumentException();
        }

        this.statements.add(statement);
    }

    /**
     * このローカル領域から文を削除する．
     * 
     * @param statement 削除する文
     */
    public void removeStatement(final StatementInfo statement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new IllegalArgumentException();
        }

        this.statements.remove(statement);
    }

    /**
     * メソッドおよびコンストラクタ呼び出し一覧を返す
     * 
     * @return メソッドおよびコンストラクタ呼び出し
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();
        for (final StatementInfo statement : this.getStatements()) {
            calls.addAll(statement.getCalls());
        }
        return Collections.unmodifiableSet(calls);
    }

    /**
     * このローカル領域の変数利用のSetを返す
     * 
     * @return このローカル領域の変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();
        for (final StatementInfo statement : this.getStatements()) {
            variableUsages.addAll(statement.getVariableUsages());
        }
        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * このローカルスペースの直内の文情報の SortedSet を返す．
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfoなど，SubsequentialBlockInfoは含まない
     * 
     * @return このローカルスペースの内のSubsequentialBlockを含む文情報の SortedSet
     */
    public SortedSet<StatementInfo> getStatements() {
        return this.getStatementsWithoutSubsequencialBlocks();
    }

    /**
     * このローカルスペースの直内の文情報の SortedSet を返す．
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfoなど，SubsequentialBlockInfoを含む
     * 
     * @return このローカルスペースの内のSubsequentialBlockを含む文情報の SortedSet
     */
    public SortedSet<StatementInfo> getStatementsWithSubsequencialBlocks() {
        final SortedSet<StatementInfo> statements = new TreeSet<StatementInfo>();
        for (final StatementInfo statement : this.statements) {

            statements.add(statement);

            if (statement instanceof IfBlockInfo) {
                final IfBlockInfo ifBlock = (IfBlockInfo) statement;
                if (ifBlock.hasElseBlock()) {
                    statements.add(ifBlock.getSequentElseBlock());
                }
            }

            else if (statement instanceof TryBlockInfo) {
                final TryBlockInfo tryBlock = (TryBlockInfo) statement;
                for (final CatchBlockInfo catchBlock : tryBlock.getSequentCatchBlocks()) {
                    statements.add(catchBlock);
                }
                if (tryBlock.hasFinallyBlock()) {
                    statements.add(tryBlock.getSequentFinallyBlock());
                }
            }
        }

        return statements;
    }

    /** 
     * このローカルスペースの直内の文情報の SortedSet を返す．
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfoは含まれない．
     * 
     * @return このローカルスペースの直内の文情報の SortedSet
     */
    public SortedSet<StatementInfo> getStatementsWithoutSubsequencialBlocks() {
        final SortedSet<StatementInfo> statements = new TreeSet<StatementInfo>();
        statements.addAll(this.statements);
        return statements;
    }

    /**
     * 所属しているクラスを返す
     * 
     * @return 所属しているクラス
     */
    public final ClassInfo getOwnerClass() {
        return this.getOuterClass();
    }

    /**
     * 与えられたLocalSpace内に存在している全てのStatementInfoのSortedSetを返す
     * これにはElseBlockInfo, CatchBlockInfo, FinallyBlockInfoが含まれる．
     * 
     * @param localSpace ローカルスペース
     * @return 与えられたLocalSpace内に存在している全てのStatementInfoのSortedSet
     */
    @Deprecated
    public static SortedSet<StatementInfo> getAllStatements(final LocalSpaceInfo localSpace) {

        if (null == localSpace) {
            throw new IllegalArgumentException("localSpace is null.");
        }

        if (localSpace instanceof ExternalMethodInfo
                || localSpace instanceof ExternalConstructorInfo) {
            throw new IllegalArgumentException("localSpace is an external local space.");
        }

        final SortedSet<StatementInfo> allStatements = new TreeSet<StatementInfo>();
        for (final StatementInfo innerStatement : localSpace.getStatements()) {
            allStatements.add(innerStatement);
            if (innerStatement instanceof BlockInfo) {
                allStatements.addAll(LocalSpaceInfo.getAllStatements((BlockInfo) innerStatement));
                /*                
                                // Else, Catch, Finally の特別処理
                                // FIXME 共通の親クラスを作るなどして体対処すべき
                                if (innerStatement instanceof IfBlockInfo) {
                                    final ElseBlockInfo elseStatement = ((IfBlockInfo) innerStatement).getSequentElseBlock();
                                    allStatements.add(elseStatement);
                                    allStatements.addAll(LocalSpaceInfo.getAllStatements(elseStatement));
                                } else if (innerStatement instanceof TryBlockInfo) {
                                    final TryBlockInfo parentTryStatement = (TryBlockInfo)innerStatement;
                                    for (final CatchBlockInfo catchStatement : parentTryStatement.getSequentCatchBlocks()){
                                        allStatements.add(catchStatement);
                                        allStatements.addAll(LocalSpaceInfo.getAllStatements(catchStatement));
                                    }
                                    final FinallyBlockInfo finallyStatement = parentTryStatement.getSequentFinallyBlock();
                                    allStatements.add(finallyStatement);
                                    allStatements.addAll(LocalSpaceInfo.getAllStatements(finallyStatement));
                                }
                */
            }
        }
        return allStatements;
    }

    /**
     * 外側のユニットを返す
     */
    @Override
    public UnitInfo getOuterUnit() {
        assert null != this.outerUnit : "outerUnit is null!";
        return this.outerUnit;
    }

    /**
     * 外側のユニットを設定する
     * 
     * @param 外側のユニット
     */
    @Override
    public void setOuterUnit(final UnitInfo outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * 外側のクラスを返す.
     * 
     * @return　外側のクラス
     */
    @Override
    public final ClassInfo getOuterClass() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            // インナークラスなのでかならず外側のクラスがある
            if (null == outer) {
                throw new IllegalStateException();
            }

            if (outer instanceof ClassInfo) {
                return (ClassInfo) outer;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * 外側のメソッドを返す.
     * 
     * @return　外側のメソッド
     */
    @Override
    public final CallableUnitInfo getOuterCallableUnit() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            if (null == outer) {
                return null;
            }

            if (outer instanceof CallableUnitInfo) {
                return (CallableUnitInfo) outer;
            }

            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * このローカルスコープの直内の文情報一覧を保存するための変数
     */
    private final SortedSet<StatementInfo> statements;

    /**
     * 外側のユニットを保存するための変数
     */
    private UnitInfo outerUnit;
}
