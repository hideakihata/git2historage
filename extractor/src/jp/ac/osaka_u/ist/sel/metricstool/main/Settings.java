package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.UnavailableLanguageException;


/**
 * 
 * @author higo
 * 
 * 実行時の引数情報を格納するためのクラス
 * 
 */
public class Settings {

    private static Settings INSTANCE = null;

    public static Settings getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    private Settings() {
        this.verbose = false;
        this.targetDirectories = new HashSet<String>();
        this.listFiles = new HashSet<String>();
        this.language = null;
        this.metrics = null;
        this.fileMetricsFile = null;
        this.classMetricsFile = null;
        this.methodMetricsFile = null;
        this.fieldMetricsFile = null;
        this.statement = true;
        this.libraries = new LinkedList<String>();
        this.threadNumber = 1;
    }

    /**
     * 冗長出力を行うかどうかを返す
     * 
     * @return 行う場合は true, 行わない場合は false
     */
    public boolean isVerbose() {
        return this.verbose;
    }

    public void setVerbose(final boolean verbose) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.verbose = verbose;
    }

    /**
     * 
     * @return 解析対象ディレクトリ
     * 
     * 解析対象ディレクトリを返す．
     * 
     */
    public Set<String> getTargetDirectories() {
        return Collections.unmodifiableSet(this.targetDirectories);
    }

    public void addTargetDirectory(final String targetDirectory) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == targetDirectory) {
            throw new IllegalArgumentException();
        }
        this.targetDirectories.add(targetDirectory);
    }

    public void setTargetDirectory(final String targetDirectory) {
        if (null == targetDirectory) {
            throw new IllegalArgumentException();
        }
        this.targetDirectories.clear();
        this.addTargetDirectory(targetDirectory);
    }

    /**
     * 解析対象ファイルの記述言語を返す
     * 
     * @return 解析対象ファイルの記述言語
     * @throws UnavailableLanguageException 利用不可能な言語が指定されている場合にスローされる
     */
    public LANGUAGE getLanguage() throws UnavailableLanguageException {
        assert null != this.language : "\"language\" is not set";
        return this.language;
    }

    public void setLanguage(final String language) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == language) {
            throw new IllegalArgumentException();
        }

        if (language.equalsIgnoreCase("java") || language.equalsIgnoreCase("java15")) {
            this.language = LANGUAGE.JAVA15;
            final File file = new File("./resource/jdk160java.lang.jar");
            if (file.exists()) {
                this.libraries.add(file.getAbsolutePath());
            }
        } else if (language.equalsIgnoreCase("java14")) {
            this.language = LANGUAGE.JAVA14;
            final File file = new File("./resource/jdk142java.lang.jar");
            if (file.exists()) {
                this.libraries.add(file.getAbsolutePath());
            }
        } else if (language.equalsIgnoreCase("java13")) {
            this.language = LANGUAGE.JAVA13;
            final File file = new File("./resource/jdk142java.lang.jar");
            if (file.exists()) {
                this.libraries.add(file.getAbsolutePath());
            }
            // }else if (language.equalsIgnoreCase("cpp")) {
            // return LANGUAGE.C_PLUS_PLUS;
            // }else if (language.equalsIgnoreCase("csharp")) {
            // return LANGUAGE.C_SHARP
        } else if (language.equalsIgnoreCase("csharp")) {
            this.language = LANGUAGE.CSHARP;
        } else {
            throw new UnavailableLanguageException("\"" + language
                    + "\" is not an available programming language!");
        }
    }

    /**
     * 
     * @return 解析対象ファイルのパスを記述しているファイル
     * 
     * 解析対象ファイルのパスを記述しているファイルのパスを返す
     * 
     */
    public Set<String> getListFiles() {
        return Collections.unmodifiableSet(this.listFiles);
    }

    public void addListFile(final String listFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == listFile) {
            throw new IllegalArgumentException();
        }
        this.listFiles.add(listFile);
    }

    /**
     * 
     * @return 計測するメトリクス
     * 
     * 計測するメトリクス一覧を返す
     * 
     */
    public String[] getMetrics() {
        return this.metrics;
    }

    public void setMetrics(final String metrics) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == metrics) {
            throw new IllegalArgumentException();
        }

        final StringTokenizer tokenizer = new StringTokenizer(metrics, ",", false);
        this.metrics = new String[tokenizer.countTokens()];
        for (int i = 0; i < this.metrics.length; i++) {
            this.metrics[i] = tokenizer.nextToken();
        }
    }

    /**
     * 
     * @return ファイルタイプのメトリクスを出力するファイル
     * 
     * ファイルタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public String getFileMetricsFile() {
        return this.fileMetricsFile;
    }

    public void setFileMetricsFile(final String fileMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fileMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.fileMetricsFile = fileMetricsFile;
    }

    /**
     * 
     * @return クラスタイプのメトリクスを出力するファイル
     * 
     * クラスタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public String getClassMetricsFile() {
        return this.classMetricsFile;
    }

    public void setClassMetricsFile(final String classMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.classMetricsFile = classMetricsFile;
    }

    /**
     * 
     * @return メソッドタイプのメトリクスを出力するファイル
     * 
     * メソッドタイプのメトリクスを出力するファイルのパスを返す
     * 
     */
    public String getMethodMetricsFile() {
        return methodMetricsFile;
    }

    public void setMethodMetricsFile(final String methodMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.methodMetricsFile = methodMetricsFile;
    }

    /**
     * 
     * @return フィールドタイプのメトリクスを出力するファイル
     */
    public String getFieldMetricsFile() {
        return this.fieldMetricsFile;
    }

    public void setFieldMetricsFile(final String fieldMetricsFile) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldMetricsFile) {
            throw new IllegalArgumentException();
        }
        this.fieldMetricsFile = fieldMetricsFile;
    }

    /**
     * 文情報を解析するかどうかを返す
     * 
     * @return　解析する場合はtrue,しない場合はfalse
     */
    public boolean isStatement() {
        return this.statement;
    }

    /**
     * 文情報を解析するかどうかをセットする
     * 
     * @param statement 解析する場合はtrue, しない場合はfalse
     */
    public void setStatement(final boolean statement) {
        this.statement = statement;
    }

    /**
     * ライブラリの位置を追加する．
     * ライブラリとは，対象クラスの解析精度を上げるために与える解析対象外クラスのjarファイルや
     * classファイルを置いているディレクトリ
     * 
     * @param library
     */
    public void addLibrary(final String library) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == library) {
            throw new IllegalArgumentException();
        }
        this.libraries.add(library);
    }

    /**
     * ライブラリのListを返す
     * 
     * @return ライブラリのList
     */
    public List<String> getLibraries() {
        return Collections.unmodifiableList(this.libraries);
    }

    /**
     * マルチスレッドの数を設定する．
     * 
     * @param threadNumber
     */
    public void setThreadNumber(final int threadNumber) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.threadNumber = threadNumber;
    }

    /**
     * マルチスレッド数を返す
     * 
     * @return マルチスレッド数
     */
    public int getThreadNumber() {
        return this.threadNumber;
    }

    /**
     * 冗長出力モードかどうかを記録するための変数
     */
    private boolean verbose;

    /**
     * 解析対象ディレクトリを記録するための変数
     */
    private final Set<String> targetDirectories;

    /**
     * 解析対象ファイルのパスを記述したファイルのパスを記録するための変数
     */
    private final Set<String> listFiles;

    /**
     * 解析対象ファイルの記述言語を記録するための変数
     */
    private LANGUAGE language;

    /**
     * 計測するメトリクスを記録するための変数
     */
    private String[] metrics;

    /**
     * ファイルタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String fileMetricsFile;

    /**
     * クラスタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String classMetricsFile;

    /**
     * メソッドタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String methodMetricsFile;

    /**
     * フィールドタイプのメトリクスを出力するファイルのパスを記録するための変数
     */
    private String fieldMetricsFile;

    /**
     * 文情報を取得するかどうかを記録するための変数
     */
    private boolean statement;

    /**
     * 外部クラスのパスを保存するための変数
     */
    private List<String> libraries;

    /**
     * マルチスレッド数
     */
    private int threadNumber;
}
