package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MemberImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * staticインポートを表すクラス
 * 
 * @author higo
 *
 */
public class UnresolvedMemberImportStatementInfo extends
        UnresolvedImportStatementInfo<MemberImportStatementInfo> {

    public static List<UnresolvedMemberImportStatementInfo> getMemberImportStatements(
            final Collection<UnresolvedImportStatementInfo<?>> importStatements) {

        final List<UnresolvedMemberImportStatementInfo> memberImportStatements = new LinkedList<UnresolvedMemberImportStatementInfo>();
        for (final UnresolvedImportStatementInfo<?> importStatement : importStatements) {
            if (importStatement instanceof UnresolvedMemberImportStatementInfo) {
                memberImportStatements.add((UnresolvedMemberImportStatementInfo) importStatement);
            }
        }
        return Collections.unmodifiableList(memberImportStatements);
    }

    /**
     * クラス名とそれ以下staticメンバー全てが利用可能かどうかを表すbooleanを与えてオブジェクトを初期化.
     * <p>
     * import aaa.bbb.CCC.DDD； // new UnresolvedMemberImportStatementInfo({"aaa","bbb","CCC","DDD"}, false); <br>
     * import aaa.bbb.CCC.*; // new AvailableNamespace({"aaa","bbb","CCC"},true); <br>
     * </p>
     * 
     * @param namespace 利用可能名前空間名
     * @param allMembers 全てのクラスが利用可能かどうか
     */
    public UnresolvedMemberImportStatementInfo(final String[] namespace, final boolean allMembers) {
        super(namespace, allMembers);
    }

    @Override
    public MemberImportStatementInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // fix import name to original text
        final String[] importName = this.getImportName();
        String[] lawImportName;
        if (this.isAll()) {
            final int length = importName.length;
            lawImportName = new String[length + 1];
            System.arraycopy(importName, 0, lawImportName, 0, length);
            lawImportName[length] = "*";
        } else {
            lawImportName = importName;
        }
        this.resolvedInfo = new MemberImportStatementInfo(lawImportName, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }
}
