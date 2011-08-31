package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * PluginXmlInterpreterクラスは，コンストラクタで引数として与えられたプラグイン定義XMLファイルを解析し，
 * その中で記述されているプラグイン情報を取得するpublicメソッド群を提供する．
 * <p>
 * プラグイン定義XMLファイルの記述形式が変更された場合は，このクラスを修正するか，
 * あるいはこのクラスのサブクラスを新たに作成して新形式に対応しなければならない．
 * 
 * @author kou-tngt
 */
public class DefaultPluginXmlInterpreter implements PluginXmlInterpreter{

    /**
     * PluginXmlInterpreterクラスの唯一のコンストラクタ．
     * 引数として与えられたXMLファイルを解析し，プラグインクラスとクラスパス指定情報を取得する．
     * @param pluginXml 解析対象とするプラグイン定義XMLファイル
     * @throws PluginLoadException pluginXmlのXMLの構文解析ができなかったとき，または，XML構文が誤っていた場合
     * @throws IOException pluginXml ファイルの読み込みに失敗した場合
     * @throws IllegalPluginXmlFormatException pluginXmlファイルがプラグイン定義XMLファイルの形式に違反している場合
     * @throws NullPointerException pluginXmlがnullの場合
     * @throws IlleagalArgumentException　pluginXmlが存在しない場合，ファイルではない場合，または拡張子がxmlではない場合
     * 
     */
    public DefaultPluginXmlInterpreter(final File pluginXml) throws PluginLoadException,
            FileNotFoundException, IOException, IllegalPluginXmlFormatException {
        if (null == pluginXml) {
            throw new NullPointerException();
        }
        if (!pluginXml.exists()) {
            throw new FileNotFoundException(pluginXml + " is not found.");
        }
        if (!pluginXml.isFile()) {
            throw new IllegalArgumentException(pluginXml + " is not file.");
        }
        if (!pluginXml.getName().endsWith(".xml")) {
            throw new IllegalArgumentException(pluginXml + " is not xml file.");
        }
        //引数チェックおしまい

        //主にエラー表示のために，ファイルを持っておく
        this.pluginXml = pluginXml;

        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            this.document = builder.parse(pluginXml);
        } catch (final ParserConfigurationException e) {
            //DocumentBuilderの構築に失敗した場合（どういう時に発生するかは不明）
            throw new PluginLoadException("Failed to interpret " + pluginXml.getAbsolutePath()
                    + ".", e);
        } catch (final SAXException e) {
            //XML構文が間違っていてDocumentが作れなかった場合）
            throw new IllegalPluginXmlFormatException("Syntax error : "
                    + pluginXml.getAbsolutePath(), e);
        }

        if (!this.document.getDocumentElement().getNodeName().equals(PLUGIN_TAG)) {
            //XMLファイルの中身が<plugin>から始まっていない＝形式がおかしい）
            throw new IllegalPluginXmlFormatException("Syntax error : The root tag must be <"
                    + PLUGIN_TAG + ">. at " + pluginXml.getAbsolutePath());
        }

        //XMLを解析して情報を取得
        this.interpretPluginXml();
    }

    /**
     * 解析対象のxmlファイル中に記述されている，プラグインクラス名を返すメソッド
     * @return プラグインクラスを表す文字列
     */
    public String getPluginClassName() {
        assert (null != this.pluginClassName && 0 != this.pluginClassName.length()) : "Illegal state: this.pluginClassName is not initialized.";
        return this.pluginClassName;
    }

    /**
     * 解析対象のxmlファイル中に記述されている，ファイルへのクラスパス指定一覧を返すメソッド
     * @return ファイルへのクラスパス指定を表す文字列の配列
     */
    public String[] getClassPathFileNames() {
        assert (null != this.classPathsToFile) : "Illegal state: this.classPathsToFile is not initialized.";
        return this.classPathsToFile;
    }

    /**
     * 解析対象のxmlファイル中に記述されている，ディレクトリへのクラスパス指定一覧を返すメソッド
     * @return ディレクトリへのクラスパス指定を表す文字列の配列
     */
    public String[] getClassPathDirectoryNames() {
        assert (null != this.classPathsToDirectory) : "Illegal state: this.classPathsToDirectory is not initialized.";
        return this.classPathsToDirectory;
    }

    /**
     * 解析対象のxmlファイル中に記述されている，クラスパス指定一覧を返すメソッド
     * @return クラスパス指定を表す文字列の配列
     */
    public String[] getClassPathAttributeNames() {
        assert (null != this.classPathsToAttribute) : "Illegal state: this.classPathsToAttribute is not initialized.";
        return this.classPathsToAttribute;
    }

    /**
     * 解析対象のXMLファイルを表すDOMドキュメントを返すメソッド．
     * サブクラスからもドキュメントが参照できるようにするため，アクセスレベルはprotectedとする．
     * @return 解析対象XMLを表すDOMドキュメント
     * @see org.w3c.dom.Document
     */
    protected final Document getDocument() {
        return this.document;
    }

    /**
     * xmlを解析しプラグインクラスの情報を取得するメソッド．
     * このメソッドはコンストラクタから呼び出されるため，このクラスのpublic，またはprotectedなインスタンスメソッドを呼び出してはならない．
     */
    private void interpretPluginClassName() {
        final String className = this.document.getDocumentElement().getAttribute(CLASS_ATTRIBUTE);
        assert (null != className && 0 != className.length()) : "Failed to read plugin class name of "
                + this.pluginXml.getAbsolutePath();
        this.pluginClassName = className;
    }

    /**
     * xmlを解析しクラスパス情報を取得するメソッド．
     * このメソッドはコンストラクタから呼び出されるため，このクラスのpublic，またはprotectedなインスタンスメソッドを呼び出してはならない．
     */
    private void interpretClassPath() {
        //クラスパス情報を一時的に格納しておくセット
        final Set<String> classPathToFileSet = new LinkedHashSet<String>();
        final Set<String> classPathToDirectorySet = new LinkedHashSet<String>();

        //<plugin>タグ直下の子供達
        final NodeList rootChildren = this.document.getDocumentElement().getChildNodes();
        final int rootChildreNum = rootChildren.getLength();

        for (int i = 0; i < rootChildreNum; i++) {
            final Node rootChild = rootChildren.item(i);
            if (rootChild.getNodeName().equals(CLASSPATH_TAG)) {
                //<classpath>タグだった

                //<classpath>タグ直下の子供達
                final NodeList classpathNodes = rootChild.getChildNodes();
                final int classPathNum = classpathNodes.getLength();

                for (int j = 0; j < classPathNum; j++) {
                    final Node classPathNode = classpathNodes.item(j);
                    final String classPathTagName = classPathNode.getNodeName();

                    if (classPathTagName.equals(FILE_TAG)) {
                        //<file>タグだった
                        final String classPathToFile = this.getNodeAttribute(classPathNode,
                                PATH_ATTRIBUTE);
                        if (null != classPathToFile && 0 != classPathToFile.length()) {
                            //登録
                            classPathToFileSet.add(classPathToFile);
                        }
                    } else if (classPathTagName.equals(DIRECTORY_TAG)) {
                        //<dir>タグだった
                        final String classPathToDirectory = this.getNodeAttribute(classPathNode,
                                PATH_ATTRIBUTE);
                        if (null != classPathToDirectory && 0 != classPathToDirectory.length()) {
                            //登録
                            classPathToDirectorySet.add(classPathToDirectory);
                        }
                    } else if (classPathTagName.equals(TEXT_TAG)) {
                        //何もしない
                    } else {
                        //<file>でも<dir>でもないタグが<classpath>直下にあった
                        //実行上は無視すれば問題ないけど一応アサーションエラーを投げる
                        assert (false) : "Unknown tag is found under <classpath> tag: "
                                + classPathTagName + " in " + this.pluginXml.getAbsolutePath();
                    }
                }
            }
        }

        //<file>で指定されたクラスパスをフィールドの配列に保存
        final int fileNum = classPathToFileSet.size();
        this.classPathsToFile = new String[fileNum];
        classPathToFileSet.toArray(this.classPathsToFile);

        //<dir>で指定されたクラスパスをフィールドの配列に保存
        final int dirNum = classPathToDirectorySet.size();
        this.classPathsToDirectory = new String[dirNum];
        classPathToDirectorySet.toArray(this.classPathsToDirectory);

        //<file>と<dir>で指定されたクラスパスをフィールドの配列に保存
        this.classPathsToAttribute = new String[fileNum + dirNum];
        int i = 0;
        for (final String classPath : classPathToFileSet) {
            this.classPathsToAttribute[i++] = classPath;
        }
        for (final String classPath : classPathToDirectorySet) {
            this.classPathsToAttribute[i++] = classPath;
        }
    }

    /**
     * xmlを解析し情報を取得するメソッド．
     * このメソッドはコンストラクタから呼び出されるため，このクラスのpublic，またはprotectedなインスタンスメソッドを呼び出してはならない．
     */
    private void interpretPluginXml() {
        this.interpretPluginClassName();
        this.interpretClassPath();
    }

    /**
     * org.w3c.dom.Nodeから，attributeNameで指定した属性の値を取得するメソッド．
     * nodeが指定された名前の属性を持たない場合はnullを返す．
     * @param node 属性値を取得する対象ノード
     * @param attributeName 取得する属性名
     * @return 属性値を表す文字列．nodeが指定された名前の属性を持たない場合はnull．
     * @see org.w3c.dom.Node
     */
    private String getNodeAttribute(final Node node, final String attributeName) {
        final NamedNodeMap map = node.getAttributes();
        final Node attribute = map.getNamedItem(attributeName);

        if (null != attribute) {
            return attribute.getNodeValue();
        } else {
            return null;
        }
    }

    /**
     * 解析対象XMLファイルのDOMドキュメント
     */
    private final Document document;

    /**
     * 解析対象XMLファイル
     */
    private final File pluginXml;;

    /**
     * 解析対象のXMLファイルが指定するプラグインクラス名
     */
    private String pluginClassName;

    /**
     * 解析対象のXMLファイルが指定する，ファイルに対するクラスパス要求の配列
     */
    private String[] classPathsToFile;

    /**
     * 解析対象のXMLファイルが指定する，ディレクトリに対するクラスパス要求の配列
     */
    private String[] classPathsToDirectory;

    /**
     * 解析対象のXMLファイルが指定する，全てのクラスパス要求の配列
     */
    private String[] classPathsToAttribute;

    /**
     * プラグイン設定情報XMLファイルのルートタグ名pluginを表す文字列定数
     */
    private static final String PLUGIN_TAG = "plugin";

    /**
     * プラグイン設定情報XMLファイルのクラスパスタグ名classpathを表す文字列定数
     */
    private static final String CLASSPATH_TAG = "classpath";

    /**
     * プラグイン設定情報XMLファイルの，ファイルに対するクラスパス指定のタグ名fileを表す文字列定数
     */
    private static final String FILE_TAG = "file";

    /**
     * XMLファイルの，テキスト情報を表すタグ
     */
    private static final String TEXT_TAG = "#text";

    /**
     * プラグイン設定情報XMLファイルの，ディレクトリに対するクラスパス指定のタグ名dirを表す文字列定数
     */
    private static final String DIRECTORY_TAG = "dir";

    /**
     * プラグイン設定情報XMLファイルの，プラグインクラス名を値に持つ属性名を表す定数文字列
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * プラグイン設定情報XMLファイルの，クラスパスを値に持つ属性名を表す定数文字列
     */
    private static final String PATH_ATTRIBUTE = "path";
}
