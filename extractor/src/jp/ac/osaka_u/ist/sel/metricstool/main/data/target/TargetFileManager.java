package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * 対象ファイルを格納するためのクラス． TargetFile を要素として持つ．
 * 
 * since 2006.11.12
 */
public final class TargetFileManager implements Iterable<TargetFile> {

    /**
     * 
     * @param targetFile 追加する対象ファイル (TargetFile)
     */
    public void add(final TargetFile targetFile) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == targetFile) {
            throw new NullPointerException();
        }

        final String filename = targetFile.getName();
        this.targetFiles.put(filename, targetFile);
    }

    @Override
    public Iterator<TargetFile> iterator() {
        return this.getFiles().iterator();
    }

    /**
     * 対象ファイルの数を返す
     * 
     * @return 対象ファイルの数
     */
    public int size() {
        return this.targetFiles.size();
    }

    /**
     * 対象ファイルをクリア
     */
    public void clear() {
        this.targetFiles.clear();
    }

    /**
     * 登録されている対象ファイルのSortedSetを返す
     * 
     * @return 登録されている対象ファイルのSortedSet
     */
    public SortedSet<TargetFile> getFiles() {
        final SortedSet<TargetFile> files = new TreeSet<TargetFile>();
        files.addAll(this.targetFiles.values());
        return files;
    }

    /**
     * 引数で与えられたパスのファイルを返す
     * 
     * @param filepath
     * @return
     */
    public TargetFile getFile(final String filepath) {
        return this.targetFiles.get(filepath);
    }

    /**
     * 
     * コンストラクタ． 
     * 以前は HashSet を用いていたが，同じディレクトリのファイルはまとめて返すほうがよいので，TreeSet に変更した．
     */
    public TargetFileManager() {
        this.targetFiles = new ConcurrentHashMap<String, TargetFile>();
    }

    /**
     * 
     * 対象ファイル (TargetFile) を格納する変数．
     */
    private final ConcurrentMap<String, TargetFile> targetFiles;
}
