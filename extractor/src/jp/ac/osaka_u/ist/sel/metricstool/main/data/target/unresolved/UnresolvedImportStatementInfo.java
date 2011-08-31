package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * staticインポートを表すクラス
 * 
 * @author higo
 *
 */
public abstract class UnresolvedImportStatementInfo<T extends ImportStatementInfo<?>> extends
        UnresolvedUnitInfo<T> {

    /**
     * インポート文字列と全ての名前が利用可能かどうかを与えてオブジェクトを初期化
     * 
     * @param namespace 利用可能名前空間名
     * @param all 全ての名前が利用可能かどうか
     */
    public UnresolvedImportStatementInfo(final String[] namespace, final boolean all) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == namespace || ((!all) && namespace.length == 0)) {
            throw new IllegalArgumentException();
        }

        this.importName = Arrays.<String> copyOf(namespace, namespace.length);
        this.all = all;
    }

    public abstract T resolve(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager);

    /**
     * 対象オブジェクトと等しいかどうかを返す
     * 
     * @param o 対象オブジェクト
     * @return 等しい場合 true，そうでない場合 false
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof UnresolvedImportStatementInfo<?>)) {
            return false;
        }

        final String[] importName = this.getImportName();
        final String[] correspondImportName = ((UnresolvedImportStatementInfo<?>) o)
                .getImportName();
        if (importName.length != correspondImportName.length) {
            return false;
        }

        for (int i = 0; i < importName.length; i++) {
            if (!importName[i].equals(correspondImportName[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * 名前空間名を返す
     * 
     * @return 名前空間名
     */
    public final String[] getImportName() {
        return Arrays.<String> copyOf(this.importName, this.importName.length);
    }

    /**
     * 完全限定名を返す．
     * 
     * @return 完全限定名
     */
    public final String[] getFullQualifiedName() {

        final String[] importName = this.getImportName();
        if (this.isAll()) {
            return importName;
        }

        final String[] namespace = new String[importName.length];
        System.arraycopy(importName, 0, namespace, 0, importName.length);
        return namespace;
    }

    /**
     * このオブジェクトのハッシュコードを返す
     * 
     * @return このオブジェクトのハッシュコード
     */
    @Override
    public final int hashCode() {
        final String[] namespace = this.getFullQualifiedName();
        return Arrays.hashCode(namespace);
    }

    /**
     * 全てのクラスが利用可能かどうか
     * 
     * @return 利用可能である場合は true, そうでない場合は false
     */
    public final boolean isAll() {
        return this.all;
    }

    /**
     * 名前空間名を表す変数
     */
    private final String[] importName;

    /**
     * 全てのクラスが利用可能かどうかを表す変数
     */
    private final boolean all;
}
