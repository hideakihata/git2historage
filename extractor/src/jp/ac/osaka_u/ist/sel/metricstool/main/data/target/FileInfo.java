package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * ファイルの情報を表すクラス．
 */
@SuppressWarnings("serial")
public final class FileInfo implements Comparable<FileInfo>, MetricMeasurable, Serializable {

    /**
     * 指定されたファイル名のオブジェクトを初期化する．
     * 
     * @param name ファイル名
     */
    public FileInfo(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
        this.definedClasses = new TreeSet<TargetClassInfo>();
        this.comments = new TreeSet<CommentInfo>();
    }

    /**
     * このファイルに定義されているクラスを追加する．
     * 
     * @param definedClass 定義されたクラス．
     */
    public void addDefinedClass(final TargetClassInfo definedClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedClass) {
            throw new NullPointerException();
        }

        this.definedClasses.add(definedClass);
    }

    /**
     * このクラスのインスタンス同士を比較するメソッド
     * 
     * @param o 比較対象のインスタンス
     * @return このインスタンスが比較対象のインスタンスより順序的に小さければ負の数，等しければ0，大きければ正の数.
     */
    public int compareTo(FileInfo o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * このファイルに定義されているクラスのSortedSetを返す
     * 
     * @return このファイルに定義されているクラスのSortedSet
     */
    public SortedSet<TargetClassInfo> getDefinedClasses() {
        return Collections.unmodifiableSortedSet(this.definedClasses);
    }

    /**
     * 引数とこのファイルが等しいかを判定する．判定には，変数nameを用いる．
     * 
     * @param o 比較対象ファイル
     * @return 等しい場合は true, 等しくない場合は false
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (!(o instanceof FileInfo)) {
            return false;
        }

        String thisName = this.getName();
        String correspondName = ((FileInfo) o).getName();
        return thisName.equals(correspondName);
    }

    /**
     * 行数を返す．
     * 
     * @return 行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * メトリクス計測対象としての名前を返す
     * 
     * @return メトリクス計測対象としての名前
     */
    public String getMeasuredUnitName() {
        return this.getName();
    }

    /**
     * ファイル名を返す． 現在フルパスで返すが，ディレクトリとファイル名を分けた方が良いかも．
     * 
     * @return ファイル名
     */
    public String getName() {
        return this.name;
    }

    /**
     * ファイルのハッシュコードを返す．ハッシュコードはファイル名（フルパス）を用いて計算される
     * 
     * @return このファイルのハッシュコード
     */
    @Override
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * 変数 loc の setter．行数情報をセットする．
     * 
     * @param loc 行数
     */
    public void setLOC(final int loc) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (loc < 0) {
            throw new IllegalArgumentException("LOC must be 0 or more!");
        }

        this.loc = loc;
    }

    /**
     * コメントを追加する
     * 
     * @param comment
     */
    public void addComment(final CommentInfo comment) {

        if (null == comment) {
            throw new IllegalArgumentException();
        }

        this.comments.add(comment);
    }

    /**
     * コメントのセットを追加する
     * @param commentSet
     */
    public void addAllComments(final SortedSet<CommentInfo> commentSet){
        this.comments.addAll(commentSet);
    }
    
    /**
     * コメント一覧を返す
     * 
     * @return
     */
    public Set<CommentInfo> getComments() {
        return Collections.unmodifiableSet(this.comments);
    }

    /**
     * ファイルの行数を表す変数．
     */
    private int loc;

    /**
     * ファイル名を表す変数. ハッシュコードの計算に使っている．
     */
    private final String name;

    /**
     * このファイルで宣言されているクラス一覧を保存するための変数
     */
    private final SortedSet<TargetClassInfo> definedClasses;

    /**
     * このファイル内のコメント一覧を保存するための変数
     */
    private final SortedSet<CommentInfo> comments;

    // TODO importしているクラスの情報を追加
    // TODO includeしているファイルの情報を追加
}
