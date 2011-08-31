package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.HavingOuterUnit;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ローカル領域(メソッドとメソッド内のブロック)を表すクラス
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class UnresolvedLocalSpaceInfo<T extends LocalSpaceInfo> extends
        UnresolvedUnitInfo<T> implements UnresolvedHavingOuterUnit {

    /**
     * 位置情報を与えて初期化
     */
    public UnresolvedLocalSpaceInfo(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.calls = new HashSet<UnresolvedCallInfo<?>>();
        this.variableUsages = new HashSet<UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.statements = new TreeSet<UnresolvedStatementInfo<?>>();
        this.outerUnit = outerUnit;
    }

    /**
     * メソッドまたはコンストラクタ呼び出しを追加する
     * 
     * @param call メソッドまたはコンストラクタ呼び出し
     */
    public final void addCall(final UnresolvedCallInfo<?> call) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == call) {
            throw new NullPointerException();
        }

        this.calls.add(call);
    }

    /**
     * 変数使用を追加する
     * 
     * @param variableUsage 変数使用
     */
    public final void addVariableUsage(
            final UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == variableUsage) {
            throw new NullPointerException();
        }

        this.variableUsages.add(variableUsage);
    }

    /**
     * ローカル変数を追加する
     * 
     * @param localVariable ローカル変数
     */
    public final void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * 未解決文を追加する
     * 
     * @param statement 未解決文
     */
    public void addStatement(final UnresolvedStatementInfo<?> statement) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new NullPointerException();
        }

        // Catch, Finally, Elseブロックのときは，追加しない
        if (statement instanceof UnresolvedCatchBlockInfo
                || statement instanceof UnresolvedFinallyBlockInfo
                || statement instanceof UnresolvedElseBlockInfo) {
            return;
        }

        this.statements.add(statement);
    }

    /**
     * TODO 名前を変える
     * インナーブロックを追加する
     * 
     * @param innerLocalInfo 追加するインナーブロック
     */
    public void addChildSpaceInfo(final UnresolvedLocalSpaceInfo<?> innerLocalInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerLocalInfo) {
            throw new NullPointerException();
        }

        this.variableUsages.addAll(innerLocalInfo.variableUsages);
        this.localVariables.addAll(innerLocalInfo.localVariables);
        this.calls.addAll(innerLocalInfo.calls);
    }

    /**
     * このブロック内で行われている未解決メソッド呼び出しおよびコンストラクタ呼び出しの Set を返す
     * 
     * @return このブロック内で行われている未解決メソッド呼び出しおよびコンストラクタ呼び出しの Set
     */
    public final Set<UnresolvedCallInfo<?>> getCalls() {
        return Collections.unmodifiableSet(this.calls);
    }

    /**
     * このブロック内で行われている未解決変数使用の Set を返す
     * 
     * @return このブロック内で行われている未解決変数使用の Set
     */
    public final Set<UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>> getVariableUsages() {
        return Collections.unmodifiableSet(this.variableUsages);
    }

    /**
     * このブロック内で定義されている未解決ローカル変数の Set を返す
     * 
     * @return このブロック内で定義されている未解決ローカル変数の Set
     */
    public final Set<UnresolvedLocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSet(this.localVariables);
    }

    /**
     * このブロック内の未解決内部ブロックの Set を返す
     * else, catch, finallyブロックは含まれない
     * 
     * @return このブロック内の未解決内部ブロックの Set
     */
    public final Set<UnresolvedStatementInfo<? extends StatementInfo>> getStatements() {
        return Collections.unmodifiableSet(this.statements);
    }

    /**
     * このローカルスペースの直内の文情報の SortedSet を返す．
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfoなど，SubsequentialBlockInfoを含む
     * 
     * @return このローカルスペースの内のSubsequentialBlockを含む文情報の SortedSet
     */
    public final SortedSet<UnresolvedStatementInfo<? extends StatementInfo>> getStatementsWithSubsequencialBlocks() {
        return Collections.unmodifiableSortedSet(this.statements);
    }

    /** 
     * このローカルスペースの直内の文情報の SortedSet を返す．
     * ElseBlockInfo, CatchBlockInfo, FinallyBlockInfoは含まれない．
     * 
     * @return このローカルスペースの直内の文情報の SortedSet
     */
    public final SortedSet<UnresolvedStatementInfo<? extends StatementInfo>> getStatementsWithOutSubsequencialBlocks() {
        final SortedSet<UnresolvedStatementInfo<? extends StatementInfo>> statements = new TreeSet<UnresolvedStatementInfo<? extends StatementInfo>>();
        for (final UnresolvedStatementInfo<? extends StatementInfo> statementInfo : this.statements) {
            if (!(statementInfo instanceof UnresolvedSubsequentialBlockInfo<?>)) {
                statements.add(statementInfo);
            }
        }

        return Collections.unmodifiableSortedSet(this.statements);
    }

    /**
     * このブロック内の未解決内部ブロック
     * 
     */

    /**
     * このローカル領域のインナー領域を名前解決する
     * 
     * @param usingClass この領域が存在しているクラス
     * @param usingMethod この領域が存在しているメソッド
     * @param classInfoManager クラスマネージャ
     * @param fieldInfoManager フィールドマネージャ
     * @param methodInfoManager メソッドマネージャ
     */
    public void resolveInnerBlock(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new IllegalArgumentException();
        }

        for (final UnresolvedStatementInfo<?> unresolvedStatement : this.getStatements()) {

            // ステートメントがブロックの時            
            if (unresolvedStatement instanceof UnresolvedBlockInfo<?>) {

                final UnresolvedBlockInfo<?> unresolvedBlock = (UnresolvedBlockInfo<?>) unresolvedStatement;
                final StatementInfo block = unresolvedStatement.resolve(usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager);
                this.resolvedInfo.addStatement(block);

                unresolvedBlock.resolveInnerBlock(usingClass, usingMethod, classInfoManager,
                        fieldInfoManager, methodInfoManager);
            }

            // ステートメントが single ステートメントの時
            else {
                final StatementInfo statement = unresolvedStatement.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                this.resolvedInfo.addStatement(statement);
            }
        }
    }

    @Override
    public final UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit() {
        assert null != this.outerUnit : "outerUnit is null!";
        return this.outerUnit;
    }

    @Override
    public final void setOuterUnit(UnresolvedUnitInfo<? extends UnitInfo> outerUnit) {

        // 不正な呼び出しでないかをチェック
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
    public final UnresolvedClassInfo getOuterClass() {

        UnresolvedUnitInfo<? extends UnitInfo> outer = this.getOuterUnit();

        while (true) {

            // インナークラスなのでかならず外側のクラスがある
            if (null == outer) {
                throw new IllegalStateException();
            }

            if (outer instanceof UnresolvedClassInfo) {
                return (UnresolvedClassInfo) outer;
            }

            outer = ((UnresolvedHavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * 外側のメソッドを返す.
     * 
     * @return　外側のメソッド
     */
    @Override
    public final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getOuterCallableUnit() {

        UnresolvedUnitInfo<? extends UnitInfo> outer = this.getOuterUnit();

        while (true) {

            if (null == outer) {
                return null;
            }

            if (outer instanceof UnresolvedCallableUnitInfo<?>) {
                return (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) outer;
            }

            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }

            outer = ((UnresolvedHavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * メソッドまたはコンストラクタ呼び出しを保存する変数
     */
    protected final Set<UnresolvedCallInfo<?>> calls;

    /**
     * フィールド使用を保存する変数
     */
    protected final Set<UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>> variableUsages;

    /**
     * このローカル領域内で定義されているローカル変数を保存する変数
     */
    protected final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * このローカル領域内で定義された未解決文を保存する変数
     */
    protected final SortedSet<UnresolvedStatementInfo<?>> statements;

    private UnresolvedUnitInfo<? extends UnitInfo> outerUnit;
}
