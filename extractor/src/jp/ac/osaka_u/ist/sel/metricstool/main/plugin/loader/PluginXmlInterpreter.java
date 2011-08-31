package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


/**
 * プラグイン定義XMLファイルを解析し，プラグイン定義XMLファイルを解析し，
 * その中で記述されているプラグイン情報を取得するメソッド群を提供する．
 * <p>
 * プラグイン定義XMLファイルの記述形式が変更された場合はこのインタフェースにアクセサメソッドを追加する必要がある．
 * @author kou-tngt
 *
 */
public interface PluginXmlInterpreter {
    /**
     * 解析対象のxmlファイル中に記述されている，プラグインクラス名を返すメソッド
     * @return プラグインクラスを表す文字列
     */
    public String getPluginClassName();

    /**
     * 解析対象のxmlファイル中に記述されている，ファイルへのクラスパス指定一覧を返すメソッド
     * @return ファイルへのクラスパス指定を表す文字列の配列
     */
    public String[] getClassPathFileNames();

    /**
     * 解析対象のxmlファイル中に記述されている，ディレクトリへのクラスパス指定一覧を返すメソッド
     * @return ディレクトリへのクラスパス指定を表す文字列の配列
     */
    public String[] getClassPathDirectoryNames();

    /**
     * 解析対象のxmlファイル中に記述されている，クラスパス指定一覧を返すメソッド
     * @return クラスパス指定を表す文字列の配列
     */
    public String[] getClassPathAttributeNames();
}
