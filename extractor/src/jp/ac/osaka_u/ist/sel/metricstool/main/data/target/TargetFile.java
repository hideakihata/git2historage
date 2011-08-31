package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 
 * @author higo
 * 
 * 対象ファイルのデータを格納するクラス
 * 
 * since 2006.11.12
 */
public final class TargetFile implements Comparable<TargetFile> {

    /**
     * 
     * @param name 対象ファイルのパス
     * 
     * 対象ファイルのパスを用いて初期化
     */
    public TargetFile(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.correctSyntax = false;
        this.name = name;
    }

    /**
     * このオブジェクトと対象オブジェクトの順序関係を返す．
     * 
     * @param targetFile 比較対象オブジェクト
     * @return 順序関係
     */
    public int compareTo(final TargetFile targetFile) {

        if (null == targetFile) {
            throw new NullPointerException();
        }

        String name = this.getName();
        String correspondName = targetFile.getName();
        return name.compareTo(correspondName);
    }

    /**
     * 
     * @param o 比較対象ファイル
     * @return この対象ファイルと比較対象ファイルのファイルパスが等しい場合は true，そうでなければ false
     * 
     * この対象ファイルのファイルパスと，引数で与えられた対象ファイルのパスが等しいかをチェックする．等しい場合は true を返し，そうでない場合は false を返す．
     * 
     */
    @Override
    public boolean equals(Object o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (!(o instanceof TargetFile)) {
            return false;
        }

        String thisName = this.getName();
        String correspondName = ((TargetFile) o).getName();
        return thisName.equals(correspondName);
    }

    /**
     * 
     * @return 対象ファイルのパス
     * 
     * 対象ファイルのパスを返す
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 対象ファイルのハッシュコードを返す
     * 
     * @return 対象ファイルのハッシュコード
     * 
     */
    @Override
    public int hashCode() {
        String name = this.getName();
        return name.hashCode();
    }

    /**
     * 対象ファイルが文法が正しいかを返す
     * 
     * @return 文法が正しい場合は true, 正しくない場合は false
     */
    public boolean isCorrectSyntax() {
        return this.correctSyntax;
    }

    /**
     * 対象ファイルの文法が正しいかどうかを保存する
     * 
     * @param correctSyntax 対象ファイルの文法の正しさ．正しい場合は true，正しくない場合は false
     */
    public void setCorrectSytax(final boolean correctSyntax) {
        this.correctSyntax = correctSyntax;
    }

    /**
     * 対象ファイルの構文が正しいかを保存するための変数
     */
    private boolean correctSyntax;

    /**
     * 
     * 対象ファイルのパスを保存するための変数
     */
    private final String name;

}
