package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールド情報を管理するクラス． FieldInfo を要素として持つ．
 * 
 * @author higo
 * 
 */
public final class FieldInfoManager {

    /**
     * 対象フィールドを追加する
     * 
     * @param fieldInfo 追加する対象フィールド情報
     */
    public void add(final TargetFieldInfo fieldInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        this.targetFieldInfos.add(fieldInfo);
    }

    /**
     * 外部フィールドを追加する
     * 
     * @param fieldInfo 追加する外部フィールド
     */
    public void add(final ExternalFieldInfo fieldInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        this.externalFieldInfos.add(fieldInfo);
    }

    /**
     * 対象フィールドの SortedSet を返す．
     * 
     * @return 対象フィールドの SortedSet
     */
    public SortedSet<TargetFieldInfo> getTargetFieldInfos() {
        return Collections.unmodifiableSortedSet(this.targetFieldInfos);
    }

    /**
     * 外部フィールドの SortedSet を返す．
     * 
     * @return 外部フィールドの SortedSet
     */
    public SortedSet<ExternalFieldInfo> getExternalFieldInfos() {
        return Collections.unmodifiableSortedSet(this.externalFieldInfos);
    }

    /**
     * 対象フィールドの個数を返す
     * 
     * @return 対象フィールドの個数
     */
    public int getTargetFieldCount() {
        return this.targetFieldInfos.size();
    }

    /**
     * 外部フィールドの個数を返す
     * 
     * @return 外部フィールドの個数
     */
    public int getExternalFieldCount() {
        return this.externalFieldInfos.size();
    }

    /**
     * フィールド情報をクリア
     */
    public void clear() {
        this.targetFieldInfos.clear();
        this.externalFieldInfos.clear();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    public FieldInfoManager() {
        this.targetFieldInfos = new TreeSet<TargetFieldInfo>();
        this.externalFieldInfos = new TreeSet<ExternalFieldInfo>();
    }

    /**
     * 
     * 対象フィールド情報を格納する変数．
     */
    private final SortedSet<TargetFieldInfo> targetFieldInfos;

    /**
     * 
     * 外部フィールド情報を格納する変数．
     */
    private final SortedSet<ExternalFieldInfo> externalFieldInfos;
}
