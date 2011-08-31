package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ファイル情報を管理するクラス． FileInfo を要素として持つ．
 * 
 * @author higo
 * 
 */
public final class FileInfoManager {

    /**
     * 
     * @param fileInfo 追加するクラス情報
     */
    public void add(final FileInfo fileInfo, final Thread thread) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileInfo || null == thread) {
            throw new IllegalArgumentException();
        }

        final String filename = fileInfo.getName();
        this.fileInfos.put(filename, fileInfo);

        List<FileInfo> files = this.threadMap.get(thread);
        if (null == files) {
            files = new ArrayList<FileInfo>();
            this.threadMap.put(thread, files);
        }
        files.add(fileInfo);
    }

    /**
     * 現在解析中のファイル情報を返す
     * 
     * @return 現在解析中のファイル情報．解析が始まっていない場合はnull，解析が終了している場合は最後に解析したファイル
     */
    public FileInfo getCurrentFile(final Thread thread) {
        final List<FileInfo> files = this.threadMap.get(thread);
        return files == null ? null : files.get(files.size() - 1);
    }

    /**
     * ファイル情報の SortedSet を返す．
     * 
     * @return ファイル情報の SortedSet
     */
    public SortedSet<FileInfo> getFileInfos() {
        final SortedSet<FileInfo> files = new TreeSet<FileInfo>();
        files.addAll(this.fileInfos.values());
        return files;
    }

    /**
     * 引数で与えられたパスのファイルを返す
     * 
     * @param filepath
     * @return
     */
    public FileInfo getFile(final String filepath){
        return this.fileInfos.get(filepath);
    }
    
    /**
     * 情報を持っているファイルの個数を返す
     * 
     * @return ファイルの個数
     */
    public int getFileCount() {
        return this.fileInfos.size();
    }

    /**
     * 登録されているファイルの総行数を返す
     * 
     * @return 登録されているファイルの総行数
     */
    public int getTotalLOC() {
        int loc = 0;
        for (final FileInfo file : this.getFileInfos()) {
            loc += file.getLOC();
        }
        return loc;
    }

    /**
     * ファイル情報をクリア
     */
    public void clear() {
        this.fileInfos.clear();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    public FileInfoManager() {
        this.fileInfos = new ConcurrentHashMap<String, FileInfo>();
        this.threadMap = new ConcurrentHashMap<Thread, List<FileInfo>>();
    }

    /**
     * 
     * ファイル情報 (FileInfo) を格納する変数．
     */
    private final ConcurrentMap<String, FileInfo> fileInfos;

    /**
     * スレッドと登録されたファイルの対応関係を保存するための変数
     */
    private final ConcurrentMap<Thread, List<FileInfo>> threadMap;
}
