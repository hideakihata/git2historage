package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * UnresolvedClassInfoManager を管理するクラス
 * 
 * @author higo
 * 
 */
public class UnresolvedClassInfoManager {

    /**
     * クラス情報を追加する
     * 
     * @param classInfo クラス情報
     */
    public synchronized void addClass(final UnresolvedClassInfo classInfo) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final String fqName = classInfo.getFullQualifiedName(".");

        if (this.classInfos.containsKey(fqName)) {
            final StringBuilder text = new StringBuilder();
            text.append(fqName);
            text.append(" : duplicate class registration!");
            throw new IllegalStateException(text.toString());
        }

        this.classInfos.put(fqName, classInfo);
    }

    /**
     * クラス情報のセットを返す
     * 
     * @return クラス情報のセット
     */
    public Collection<UnresolvedClassInfo> getClassInfos() {
        return Collections.unmodifiableCollection(this.classInfos.values());
    }

    public UnresolvedClassInfo getClassInfo(final String name) {

        // 同じクラス名を持つクラス一覧を取得        
        for(final UnresolvedClassInfo classInfo : this.getClassInfos()){
            if(classInfo.getClassName().equals(name)){
                return classInfo;
            }
        }
        return null;
    }
    
    /**
     * 引数なしコンストラクタ
     * 
     */
    public UnresolvedClassInfoManager() {
        this.classInfos = new ConcurrentHashMap<String, UnresolvedClassInfo>();
    }

    /**
     * UnresolvedClassInfo を保存するためのセット
     */
    private final ConcurrentMap<String, UnresolvedClassInfo> classInfos;
}
