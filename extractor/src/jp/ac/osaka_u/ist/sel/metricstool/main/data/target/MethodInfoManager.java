package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッド情報を管理するクラス． methodInfo を要素として持つ．
 * 
 * @author higo
 * 
 */
public final class MethodInfoManager {

    /**
     * 
     * @param methodInfo 追加するメソッド情報
     */
    public void add(final TargetMethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.targetMethodInfos.add(methodInfo);
    }

    /**
     * 
     * @param methodInfo 追加するメソッド情報
     */
    public void add(final ExternalMethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.externalMethodInfos.add(methodInfo);
    }

    /**
     * 
     * @param constructorInfo 追加するコンストラクタ情報
     */
    public void add(final TargetConstructorInfo constructorInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == constructorInfo) {
            throw new NullPointerException();
        }

        this.targetConstructorInfos.add(constructorInfo);
    }

    /**
     * 
     * @param constructorInfo 追加するコンストラクタ情報
     */
    public void add(final ExternalConstructorInfo constructorInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == constructorInfo) {
            throw new NullPointerException();
        }

        this.externalConstructorInfos.add(constructorInfo);
    }

    /**
     * 対象メソッド情報のSortedSetを返す．
     * 
     * @return 対象メソッド情報のSortedSet
     */
    public SortedSet<TargetMethodInfo> getTargetMethodInfos() {
        return Collections.unmodifiableSortedSet(this.targetMethodInfos);
    }

    /**
     * 外部メソッド情報のSortedSetを返す．
     * 
     * @return 外部メソッド情報のSortedSet
     */
    public SortedSet<ExternalMethodInfo> getExternalMethodInfos() {
        return Collections.unmodifiableSortedSet(this.externalMethodInfos);
    }

    /**
     * 持っている対象メソッド情報の個数を返す.
     * 
     * @return 対象メソッドの個数
     */
    public int getTargetMethodCount() {
        return this.targetMethodInfos.size();
    }

    /**
     * 持っている外部メソッド情報の個数を返す.
     * 
     * @return 外部メソッドの個数
     */
    public int getExternalMethodCount() {
        return this.externalMethodInfos.size();
    }

    /**
     * 対象コンストラクタ情報のSortedSetを返す．
     * 
     * @return 対象コンストラクタ情報のSortedSet
     */
    public SortedSet<TargetConstructorInfo> getTargetConstructorInfos() {
        return Collections.unmodifiableSortedSet(this.targetConstructorInfos);
    }

    /**
     * 外部コンストラクタ情報のSortedSetを返す．
     * 
     * @return 外部コンストラクタ情報のSortedSet
     */
    public SortedSet<ExternalConstructorInfo> getExternalConstructorInfos() {
        return Collections.unmodifiableSortedSet(this.externalConstructorInfos);
    }

    /**
     * 対象コンストラクタ情報の個数を返す.
     * 
     * @return 対象コンストラクタの個数
     */
    public int getTargetConstructorCount() {
        return this.targetConstructorInfos.size();
    }

    /**
     * 外部コンストラクタ情報の個数を返す.
     * 
     * @return 外部コンストラクタの個数
     */
    public int getExternalConstructorCount() {
        return this.externalConstructorInfos.size();
    }

    /**
     * メソッド情報をクリア
     */
    public void clear() {
        this.targetMethodInfos.clear();
        this.targetConstructorInfos.clear();
        this.externalMethodInfos.clear();
        this.externalConstructorInfos.clear();
    }

    /**
     * 
     * コンストラクタ． 
     */
    public MethodInfoManager() {
        this.targetMethodInfos = new TreeSet<TargetMethodInfo>();
        this.externalMethodInfos = new TreeSet<ExternalMethodInfo>();
        this.targetConstructorInfos = new TreeSet<TargetConstructorInfo>();
        this.externalConstructorInfos = new TreeSet<ExternalConstructorInfo>();
    }

    /**
     * 
     * 対象メソッド情報を格納する変数．
     */
    private final SortedSet<TargetMethodInfo> targetMethodInfos;

    /**
     * 
     * 外部メソッド情報を格納する変数．
     */
    private final SortedSet<ExternalMethodInfo> externalMethodInfos;

    /**
     * 対象コンストラクタ情報を格納する変数
     */
    private final SortedSet<TargetConstructorInfo> targetConstructorInfos;

    /**
     * 外部コンストラクタ情報を格納する変数
     */
    private final SortedSet<ExternalConstructorInfo> externalConstructorInfos;
}
