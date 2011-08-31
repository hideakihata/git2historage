package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricNotRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JavaPredefinedModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFileManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedInstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVClassMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVFileMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.CSVMethodMetricsWriter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeNameResolver;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeParser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm.JavaByteCodeUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.DefaultPluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginLauncher;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.DefaultPluginLoader;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader.PluginLoadException;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.objectweb.asm.ClassReader;


/**
 * 
 * @author higo
 * 
 * MetricsToolのメインクラス． 現在は仮実装．
 * 
 * since 2006.11.12
 * 
 */
public class MetricsTool {

    /**
     * 
     * @param args 対象ファイルのファイルパス
     * 
     * 現在仮実装． 対象ファイルのデータを格納した後，構文解析を行う．
     */
    public static void main(String[] args) {

        initSecurityManager();

        // 情報表示用のリスナを作成
        final MessageListener outListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        final MessageListener errListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.err.print(event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(errListener);

        final Options options = new Options();

        {
            final Option h = new Option("h", "help", false, "display usage");
            h.setRequired(false);
            options.addOption(h);
        }

        {
            final Option v = new Option("v", "verbose", false, "output progress verbosely");
            v.setRequired(false);
            options.addOption(v);
        }

        {
            final Option d = new Option("d", "directores", true,
                    "specify target directories (separate with comma \',\' if you specify multiple directories");
            d.setArgName("directories");
            d.setArgs(1);
            d.setRequired(false);
            options.addOption(d);
        }

        {
            final Option i = new Option(
                    "i",
                    "input",
                    true,
                    "specify the input that contains the list of target files (separate with comma \',\' if you specify multiple inputs)");
            i.setArgName("input");
            i.setArgs(1);
            i.setRequired(false);
            options.addOption(i);
        }

        {
            final Option l = new Option("l", "language", true, "specify programming language");
            l.setArgName("input");
            l.setArgs(1);
            l.setRequired(false);
            options.addOption(l);
        }

        {
            final Option m = new Option("m", "metrics", true,
                    "specify measured metrics with comma separeted format (e.g., -m rfc,dit,lcom)");
            m.setArgName("metrics");
            m.setArgs(1);
            m.setRequired(false);
            options.addOption(m);
        }

        {
            final Option F = new Option("F", "FileMetricsFile", true,
                    "specify file that measured FILE metrics were stored into");
            F.setArgName("file metrics file");
            F.setArgs(1);
            F.setRequired(false);
            options.addOption(F);
        }

        {
            final Option C = new Option("C", "ClassMetricsFile", true,
                    "specify file that measured CLASS metrics were stored into");
            C.setArgName("class metrics file");
            C.setArgs(1);
            C.setRequired(false);
            options.addOption(C);
        }

        {
            final Option M = new Option("M", "MethodMetricsFile", true,
                    "specify file that measured METHOD metrics were stored into");
            M.setArgName("method metrics file");
            M.setArgs(1);
            M.setRequired(false);
            options.addOption(M);
        }

        {
            final Option A = new Option("A", "AttributeMetricsFile", true,
                    "specify file that measured ATTRIBUTE metrics were stored into");
            A.setArgName("attribute metrics file");
            A.setArgs(1);
            A.setRequired(false);
            options.addOption(A);
        }

        {
            final Option s = new Option("s", "AnalyzeStatement", false,
                    "specify this option if you don't need statement information");
            s.setRequired(false);
            options.addOption(s);
        }

        {
            final Option b = new Option("b", "libraries", true,
                    "specify libraries (.jar file or .class file or directory that contains .jar and .class files)");
            b.setArgName("libraries");
            b.setArgs(1);
            b.setRequired(false);
            options.addOption(b);
        }

        {
            final Option t = new Option("t", "threads", true,
                    "specify thread number used for multi-thread processings");
            t.setArgName("number");
            t.setArgs(1);
            t.setRequired(false);
            options.addOption(t);
        }

        final MetricsTool metricsTool = new MetricsTool();

        try {

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            // "-h"が指定されている場合はヘルプを表示して終了
            // このとき，他のオプションは全て無視される
            if (cmd.hasOption("h") || (0 == args.length)) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("MetricsTool", options, true);

                // -l で言語が指定されていない場合は，解析可能言語一覧を表示
                if (!cmd.hasOption("l")) {
                    err.println("Available languages;");
                    for (final LANGUAGE language : LANGUAGE.values()) {
                        err.println("\t" + language.getName() + ": can be specified with term \""
                                + language.getIdentifierName() + "\"");
                    }

                    // -l で言語が指定されている場合は，そのプログラミング言語で使用可能なメトリクス一覧を表示
                } else {
                    Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
                    err.println("Available metrics for "
                            + Settings.getInstance().getLanguage().getName());
                    metricsTool.loadPlugins(Settings.getInstance().getMetrics());
                    for (final AbstractPlugin plugin : DataManager.getInstance().getPluginManager()
                            .getPlugins()) {
                        final PluginInfo pluginInfo = plugin.getPluginInfo();
                        if (pluginInfo.isMeasurable(Settings.getInstance().getLanguage())) {
                            err.println("\t" + pluginInfo.getMetricName());
                        }
                    }
                }

                System.exit(0);
            }

            Settings.getInstance().setVerbose(cmd.hasOption("v"));
            if (cmd.hasOption("d")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("d"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String directory = tokenizer.nextToken();
                    Settings.getInstance().addTargetDirectory(directory);
                }
            }
            if (cmd.hasOption("i")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("i"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String listFile = tokenizer.nextToken();
                    Settings.getInstance().addListFile(listFile);
                }
            }
            Settings.getInstance().setLanguage(cmd.getOptionValue("l"));
            if (cmd.hasOption("m")) {
                Settings.getInstance().setMetrics(cmd.getOptionValue("m"));
            }
            if (cmd.hasOption("F")) {
                Settings.getInstance().setFileMetricsFile(cmd.getOptionValue("F"));
            }
            if (cmd.hasOption("C")) {
                Settings.getInstance().setClassMetricsFile(cmd.getOptionValue("C"));
            }
            if (cmd.hasOption("M")) {
                Settings.getInstance().setMethodMetricsFile(cmd.getOptionValue("M"));
            }
            if (cmd.hasOption("A")) {
                Settings.getInstance().setFieldMetricsFile(cmd.getOptionValue("A"));
            }
            Settings.getInstance().setStatement(!cmd.hasOption("s"));
            if (cmd.hasOption("b")) {
                final StringTokenizer tokenizer = new StringTokenizer(cmd.getOptionValue("b"), ",");
                while (tokenizer.hasMoreElements()) {
                    final String library = tokenizer.nextToken();
                    Settings.getInstance().addLibrary(library);
                }
            }
            if (cmd.hasOption("t")) {
                Settings.getInstance().setThreadNumber(Integer.parseInt(cmd.getOptionValue("t")));
            }

            metricsTool.loadPlugins(Settings.getInstance().getMetrics());

            // コマンドライン引数が正しいかどうかチェックする
            {
                // -d と -i のどちらも指定されているのは不正
                if (!cmd.hasOption("d") && !cmd.hasOption("l")) {
                    err.println("-d and/or -i must be specified in the analysis mode!");
                    System.exit(0);
                }

                // 言語が指定されなかったのは不正
                if (!cmd.hasOption("l")) {
                    err.println("-l must be specified for analysis");
                    System.exit(0);
                }

                {
                    // ファイルメトリクスを計測する場合は -F オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && !cmd.hasOption("F")) {
                        err.println("-F must be specified for file metrics!");
                        System.exit(0);
                    }

                    // クラスメトリクスを計測する場合は -C オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && !cmd.hasOption("C")) {
                        err.println("-C must be specified for class metrics!");
                        System.exit(0);
                    }
                    // メソッドメトリクスを計測する場合は -M オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && !cmd.hasOption("M")) {
                        err.println("-M must be specified for method metrics!");
                        System.exit(0);
                    }

                    // フィールドメトリクスを計測する場合は -A オプションが指定されていなければならない
                    if ((0 < DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && !cmd.hasOption("A")) {
                        err.println("-A must be specified for field metrics!");
                        System.exit(0);
                    }
                }

                {
                    // ファイルメトリクスを計測しないのに -F　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getFileMetricPlugins()
                            .size())
                            && cmd.hasOption("F")) {
                        err.println("No file metric is specified. -F is ignored.");
                    }

                    // クラスメトリクスを計測しないのに -C　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getClassMetricPlugins()
                            .size())
                            && cmd.hasOption("C")) {
                        err.println("No class metric is specified. -C is ignored.");
                    }

                    // メソッドメトリクスを計測しないのに -M　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getMethodMetricPlugins()
                            .size())
                            && cmd.hasOption("M")) {
                        err.println("No method metric is specified. -M is ignored.");
                    }

                    // フィールドメトリクスを計測しないのに -A　オプションが指定されている場合は無視する旨を通知
                    if ((0 == DataManager.getInstance().getPluginManager().getFieldMetricPlugins()
                            .size())
                            && cmd.hasOption("A")) {
                        err.println("No field metric is specified. -A is ignored.");
                    }
                }
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        final long start = System.nanoTime();

        metricsTool.analyzeLibraries();
        metricsTool.readTargetFiles();
        metricsTool.analyzeTargetFiles();
        metricsTool.launchPlugins();
        metricsTool.writeMetrics();

        out.println("successfully finished.");

        final long end = System.nanoTime();

        if (Settings.getInstance().isVerbose()) {
            out.println("elapsed time: " + (end - start) / 1000000000 + " seconds");
            out.println("number of analyzed files: "
                    + DataManager.getInstance().getFileInfoManager().getFileInfos().size());

            int loc = 0;
            for (final FileInfo file : DataManager.getInstance().getFileInfoManager()
                    .getFileInfos()) {
                loc += file.getLOC();
            }
            out.println("analyzed lines of code: " + loc);

        }
        MessagePool.getInstance(MESSAGE_TYPE.OUT).removeMessageListener(outListener);
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).removeMessageListener(errListener);
    }

    /**
     * 引数無しコンストラクタ． セキュリティマネージャの初期化を行う．
     */
    public MetricsTool() {

    }

    /**
     * ライブラリを解析し，その情報をExternalClassInfoとして登録する．
     * readTargetFiles()の前に呼び出されなければならない
     */
    public void analyzeLibraries() {

        final Settings settings = Settings.getInstance();

        // java言語の場合
        if (settings.getLanguage().equals(LANGUAGE.JAVA15)
                || settings.getLanguage().equals(LANGUAGE.JAVA14)
                || settings.getLanguage().equals(LANGUAGE.JAVA13)) {

            this.analyzeJavaLibraries();
        }

        else if (settings.getLanguage().equals(LANGUAGE.CSHARP)) {

        }
    }

    private void analyzeJavaLibraries() {

        final Set<JavaUnresolvedExternalClassInfo> unresolvedExternalClasses = new HashSet<JavaUnresolvedExternalClassInfo>();

        // バイトコードから読み込み
        for (final String path : Settings.getInstance().getLibraries()) {
            readJavaLibraries(new File(path), unresolvedExternalClasses);
        }

        // クラスそのもののみ名前解決（型は解決しない）
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // 無名クラスは名前解決しない
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            final String unresolvedName = unresolvedClassInfo.getName();
            final String[] name = JavaByteCodeNameResolver.resolveName(unresolvedName);
            final Set<String> unresolvedModifiers = unresolvedClassInfo.getModifiers();
            final boolean isInterface = unresolvedClassInfo.isInterface();
            final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
            for (final String unresolvedModifier : unresolvedModifiers) {
                modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
            }
            final ExternalClassInfo classInfo = unresolvedClassInfo.isInner() ? new ExternalInnerClassInfo(
                    modifiers, name, isInterface) : new ExternalClassInfo(modifiers, name,
                    isInterface);
            classInfoManager.add(classInfo);
        }

        // 外側のクラス情報を追加
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // 無名クラスは無視
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            // インナークラスでない場合は無視
            if (!unresolvedClassInfo.isInner()) {
                continue;
            }

            final String[] fqName = JavaByteCodeUtility.separateName(unresolvedClassInfo.getName());
            final String[] outerFQName = Arrays.copyOf(fqName, fqName.length - 1);
            final ClassInfo outerClass = classInfoManager.getClassInfo(outerFQName);
            if (null != outerClass) { // outerClassが登録されていないかもしれないので
                final ClassInfo classInfo = classInfoManager.getClassInfo(fqName);
                ((ExternalInnerClassInfo) classInfo).setOuterUnit(outerClass);
            }
        }

        //　型パラメータを解決
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // 無名クラスは無視
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            // まずは，解決済みオブジェクトを取得            
            final String unresolvedClassName = unresolvedClassInfo.getName();
            final String[] className = JavaByteCodeNameResolver.resolveName(unresolvedClassName);
            final ExternalClassInfo classInfo = (ExternalClassInfo) classInfoManager
                    .getClassInfo(className);

            // 型パラメータを解決
            final List<String> unresolvedTypeParameters = unresolvedClassInfo.getTypeParameters();
            for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                TypeParameterInfo typeParameter = JavaByteCodeNameResolver.resolveTypeParameter(
                        unresolvedTypeParameter, index, classInfo);
                classInfo.addTypeParameter(typeParameter);
            }
        }

        //　各クラスで表われている型を解決していく
        for (final JavaUnresolvedExternalClassInfo unresolvedClassInfo : unresolvedExternalClasses) {

            // 無名クラスは名前解決しない
            if (unresolvedClassInfo.isAnonymous()) {
                continue;
            }

            // まずは，解決済みオブジェクトを取得            
            final String unresolvedClassName = unresolvedClassInfo.getName();
            final String[] className = JavaByteCodeNameResolver.resolveName(unresolvedClassName);
            final ExternalClassInfo classInfo = (ExternalClassInfo) classInfoManager
                    .getClassInfo(className);

            // 親クラス,インターフェースを解決
            for (final String unresolvedSuperType : unresolvedClassInfo.getSuperTypes()) {
                final ClassTypeInfo superType = (ClassTypeInfo) JavaByteCodeNameResolver
                        .resolveType(unresolvedSuperType, null, classInfo);
                classInfo.addSuperClass(superType);
                final ClassInfo superClass = superType.getReferencedClass();
                superClass.addSubClass(classInfo);
            }

            // フィールドの解決            
            for (final JavaUnresolvedExternalFieldInfo unresolvedField : unresolvedClassInfo
                    .getFields()) {

                final String fieldName = unresolvedField.getName();
                final String unresolvedType = unresolvedField.getType();
                final TypeInfo fieldType = JavaByteCodeNameResolver.resolveType(unresolvedType,
                        null, null);
                final Set<String> unresolvedModifiers = unresolvedField.getModifiers();
                final boolean isInstance = !unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.STATIC_STRING);
                final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
                for (final String unresolvedModifier : unresolvedModifiers) {
                    modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
                }
                final ExternalFieldInfo field = new ExternalFieldInfo(modifiers, fieldName,
                        classInfo, isInstance);
                field.setType(fieldType);
                classInfo.addDefinedField(field);
            }

            // メソッドの解決
            for (final JavaUnresolvedExternalMethodInfo unresolvedMethod : unresolvedClassInfo
                    .getMethods()) {

                final String name = unresolvedMethod.getName();
                final Set<String> unresolvedModifiers = unresolvedMethod.getModifiers();
                final boolean isStatic = unresolvedModifiers
                        .contains(JavaPredefinedModifierInfo.STATIC_STRING);
                final Set<ModifierInfo> modifiers = new HashSet<ModifierInfo>();
                for (final String unresolvedModifier : unresolvedModifiers) {
                    modifiers.add(JavaPredefinedModifierInfo.getModifierInfo(unresolvedModifier));
                }

                // コンストラクタのとき
                if (name.equals("<init>")) {

                    final ExternalConstructorInfo constructor = new ExternalConstructorInfo(
                            modifiers);
                    constructor.setOuterUnit(classInfo);

                    // 型パラメータの解決
                    final List<String> unresolvedTypeParameters = unresolvedMethod
                            .getTypeParameters();
                    for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                        final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                        TypeParameterInfo typeParameter = (TypeParameterInfo) JavaByteCodeNameResolver
                                .resolveTypeParameter(unresolvedTypeParameter, index, constructor);
                        constructor.addTypeParameter(typeParameter);
                    }

                    // 引数の解決
                    final List<String> unresolvedParameters = unresolvedMethod.getArgumentTypes();
                    for (final String unresolvedParameter : unresolvedParameters) {
                        final TypeInfo parameterType = JavaByteCodeNameResolver.resolveType(
                                unresolvedParameter, null, constructor);
                        final ExternalParameterInfo parameter = new ExternalParameterInfo(
                                parameterType, constructor);
                        constructor.addParameter(parameter);
                    }

                    // スローされる例外の解決
                    final List<String> unresolvedThrownExceptions = unresolvedMethod
                            .getThrownExceptions();
                    for (final String unresolvedThrownException : unresolvedThrownExceptions) {
                        final TypeInfo exceptionType = JavaByteCodeNameResolver.resolveType(
                                unresolvedThrownException, null, constructor);
                        constructor.addThrownException((ReferenceTypeInfo) exceptionType);
                    }

                    classInfo.addDefinedConstructor(constructor);
                }

                // メソッドのとき
                else {

                    final ExternalMethodInfo method = new ExternalMethodInfo(modifiers, name,
                            !isStatic);
                    method.setOuterUnit(classInfo);

                    // 型パラメータの解決
                    final List<String> unresolvedTypeParameters = unresolvedMethod
                            .getTypeParameters();
                    for (int index = 0; index < unresolvedTypeParameters.size(); index++) {
                        final String unresolvedTypeParameter = unresolvedTypeParameters.get(index);
                        TypeParameterInfo typeParameter = JavaByteCodeNameResolver
                                .resolveTypeParameter(unresolvedTypeParameter, index, method);
                        method.addTypeParameter(typeParameter);
                    }

                    // 返り値の解決
                    final String unresolvedReturnType = unresolvedMethod.getReturnType();
                    final TypeInfo returnType = JavaByteCodeNameResolver.resolveType(
                            unresolvedReturnType, null, method);
                    method.setReturnType(returnType);

                    // 引数の解決
                    final List<String> unresolvedParameters = unresolvedMethod.getArgumentTypes();
                    for (final String unresolvedParameter : unresolvedParameters) {
                        final TypeInfo parameterType = JavaByteCodeNameResolver.resolveType(
                                unresolvedParameter, null, method);
                        final ExternalParameterInfo parameter = new ExternalParameterInfo(
                                parameterType, method);
                        method.addParameter(parameter);
                    }

                    // スローされる例外の解決
                    final List<String> unresolvedThrownExceptions = unresolvedMethod
                            .getThrownExceptions();
                    for (final String unresolvedThrownException : unresolvedThrownExceptions) {
                        final TypeInfo exceptionType = JavaByteCodeNameResolver.resolveType(
                                unresolvedThrownException, null, method);
                        method.addThrownException((ReferenceTypeInfo) exceptionType);
                    }

                    classInfo.addDefinedMethod(method);
                }
            }
        }
    }

    private void readJavaLibraries(final File file,
            final Set<JavaUnresolvedExternalClassInfo> unresolvedExternalClasses) {

        try {

            // jarファイルの場合
            if (file.isFile() && file.getName().endsWith(".jar")) {

                final JarFile jar = new JarFile(file);
                for (final Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
                    final JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {

                        final ClassReader reader = new ClassReader(jar.getInputStream(entry));
                        final JavaByteCodeParser parser = new JavaByteCodeParser();
                        reader.accept(parser, ClassReader.SKIP_CODE);
                        unresolvedExternalClasses.add(parser.getClassInfo());
                    }
                }
            }

            // classファイルの場合
            else if (file.isFile() && file.getName().endsWith(".class")) {

                final ClassReader reader = new ClassReader(new FileInputStream(file));
                final JavaByteCodeParser parser = new JavaByteCodeParser();
                reader.accept(parser, ClassReader.SKIP_CODE);
                unresolvedExternalClasses.add(parser.getClassInfo());
            }

            // ディレクトリの場合
            else if (file.isDirectory()) {

                for (final File subfile : file.listFiles()) {

                    if (subfile.isFile()) {
                        final String name = subfile.getName();
                        if (name.endsWith(".jar") || name.endsWith(".class")) {
                            readJavaLibraries(subfile, unresolvedExternalClasses);
                        }
                    }

                    else if (subfile.isDirectory()) {
                        readJavaLibraries(subfile, unresolvedExternalClasses);
                    }
                }
            }

            //上記以外の場合は正しくないファイルがJavaのライブラリとして指定されていることになり，終了            
            else {
                err.println("file <" + file.getAbsolutePath()
                        + "> is inappropriate as a Java library.");
                System.exit(0);
            }
        }

        // ライブラリの読み込みで例外が発生した場合はプログラムを終了
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * {@link #readTargetFiles()} で読み込んだ対象ファイル群を解析する.
     * 
     */
    public void analyzeTargetFiles() {

        // 対象ファイルのASTから未解決クラス，フィールド，メソッド情報を取得
        out.println("parsing all target files.");
        parseTargetFiles();

        out.println("resolving definitions and usages.");
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP1 : resolve definitions.");
        }
        resolveDefinitions();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP2 : resolve types in definitions.");
        }
        resolveTypes();
        if (Settings.getInstance().isVerbose()) {
            out.println("STEP3 : resolve method overrides.");
        }
        addOverrideRelation();
        if (Settings.getInstance().isStatement()) {
            if (Settings.getInstance().isVerbose()) {
                out.println("STEP4 : resolve field and method usages.");
            }
            addMethodInsideInfomation();
        }

        // 文法誤りのあるファイル一覧を表示
        // err.println("The following files includes incorrect syntax.");
        // err.println("Any metrics of them were not measured");
        for (final TargetFile targetFile : DataManager.getInstance().getTargetFileManager()) {
            if (!targetFile.isCorrectSyntax()) {
                err.println("Incorrect syntax file: " + targetFile.getName());
            }
        }
    }

    /**
     * プラグインをロードする. 指定された言語，指定されたメトリクスに関連するプラグインのみを {@link PluginManager}に登録する.
     * null が指定された場合は対象言語において計測可能な全てのメトリクスを登録する
     * 
     * @param metrics 指定するメトリクスの配列，指定しない場合はnull
     */
    public void loadPlugins(final String[] metrics) {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();
        try {
            for (final AbstractPlugin plugin : (new DefaultPluginLoader()).loadPlugins()) {// プラグインを全ロード
                final PluginInfo info = plugin.getPluginInfo();

                // 対象言語で計測可能でなければ登録しない
                if (!info.isMeasurable(settings.getLanguage())) {
                    continue;
                }

                if (null != metrics) {
                    // メトリクスが指定されているのでこのプラグインと一致するかチェック
                    final String pluginMetricName = info.getMetricName();
                    for (final String metric : metrics) {
                        if (metric.equalsIgnoreCase(pluginMetricName)) {
                            pluginManager.addPlugin(plugin);
                            break;
                        }
                    }

                    // メトリクスが指定されていないのでとりあえず全部登録
                } else {
                    pluginManager.addPlugin(plugin);
                }
            }
        } catch (PluginLoadException e) {
            err.println(e.getMessage());
            System.exit(0);
        }
    }

    /**
     * ロード済みのプラグインを実行する.
     */
    public void launchPlugins() {

        out.println("calculating metrics.");

        PluginLauncher launcher = new DefaultPluginLauncher();
        launcher.setMaximumLaunchingNum(1);
        launcher.launchAll(DataManager.getInstance().getPluginManager().getPlugins());

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 気にしない
            }
        } while (0 < launcher.getCurrentLaunchingNum() + launcher.getLaunchWaitingTaskNum());

        launcher.stopLaunching();
    }

    /**
     * {@link Settings}に指定された場所から解析対象ファイルを読み込んで登録する
     */
    public void readTargetFiles() {

        out.println("building target file list.");

        final Settings settings = Settings.getInstance();

        // ディレクトリから読み込み
        for (final String directory : settings.getTargetDirectories()) {
            registerFilesFromDirectory(new File(directory));
        }

        // リストファイルから読み込み
        for (final String file : settings.getListFiles()) {

            try {

                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) {
                    final String line = reader.readLine();
                    final TargetFile targetFile = new TargetFile(line);
                    targetFiles.add(targetFile);
                }
                reader.close();
            } catch (FileNotFoundException e) {
                err.println("\"" + file + "\" is not a valid file!");
                System.exit(0);
            } catch (IOException e) {
                err.println("\"" + file + "\" can\'t read!");
                System.exit(0);
            }
        }
    }

    /**
     * メトリクス情報を {@link Settings} に指定されたファイルに出力する.
     */
    public void writeMetrics() {

        final PluginManager pluginManager = DataManager.getInstance().getPluginManager();
        final Settings settings = Settings.getInstance();

        // ファイルメトリクスを計測する場合
        if (0 < pluginManager.getFileMetricPlugins().size()) {

            try {
                final FileMetricsInfoManager manager = DataManager.getInstance()
                        .getFileMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getFileMetricsFile();
                final CSVFileMetricsWriter writer = new CSVFileMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("File metrics can't be output!");
            }
        }

        // クラスメトリクスを計測する場合
        if (0 < pluginManager.getClassMetricPlugins().size()) {

            try {
                final ClassMetricsInfoManager manager = DataManager.getInstance()
                        .getClassMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getClassMetricsFile();
                final CSVClassMetricsWriter writer = new CSVClassMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Class metrics can't be output!");
            }
        }

        // メソッドメトリクスを計測する場合
        if (0 < pluginManager.getMethodMetricPlugins().size()) {

            try {
                final MethodMetricsInfoManager manager = DataManager.getInstance()
                        .getMethodMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Method metrics can't be output!");
            }

        }

        // フィールドメトリクスを計測する場合
        if (0 < pluginManager.getFieldMetricPlugins().size()) {

            try {
                final FieldMetricsInfoManager manager = DataManager.getInstance()
                        .getFieldMetricsInfoManager();
                manager.checkMetrics();

                final String fileName = settings.getMethodMetricsFile();
                final CSVMethodMetricsWriter writer = new CSVMethodMetricsWriter(fileName);
                writer.write();

            } catch (MetricNotRegisteredException e) {
                err.println(e.getMessage());
                err.println("Field metrics can't be output!");
            }
        }
    }

    /**
     * {@link MetricsToolSecurityManager} の初期化を行う. システムに登録できれば，システムのセキュリティマネージャにも登録する.
     */
    private static final void initSecurityManager() {
        try {
            // MetricsToolSecurityManagerのシングルトンインスタンスを構築し，初期特別権限スレッドになる
            System.setSecurityManager(MetricsToolSecurityManager.getInstance());
        } catch (final SecurityException e) {
            // 既にセットされているセキュリティマネージャによって，新たなセキュリティマネージャの登録が許可されなかった．
            // システムのセキュリティマネージャとして使わなくても，特別権限スレッドのアクセス制御は問題なく動作するのでとりあえず無視する
            err.println("Failed to set system security manager. MetricsToolsecurityManager works only to manage privilege threads.");
        }
    }

    /**
     * 
     * @param file 対象ファイルまたはディレクトリ
     * 
     * 対象がディレクトリの場合は，その子に対して再帰的に処理をする． 対象がファイルの場合は，対象言語のソースファイルであれば，登録処理を行う．
     */
    private void registerFilesFromDirectory(final File file) {

        // ディレクトリならば，再帰的に処理
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                registerFilesFromDirectory(subfiles[i]);
            }

            // ファイルならば，拡張子が対象言語と一致すれば登録
        } else if (file.isFile()) {

            final LANGUAGE language = Settings.getInstance().getLanguage();
            final String extension = language.getExtension();
            final String path = file.getAbsolutePath();
            if (path.endsWith(extension)) {
                final TargetFileManager targetFiles = DataManager.getInstance()
                        .getTargetFileManager();
                final TargetFile targetFile = new TargetFile(path);
                targetFiles.add(targetFile);
            }

            // ディレクトリでもファイルでもない場合は不正
        } else {
            err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }
    }

    /**
     * 出力メッセージ出力用のプリンタ
     */
    protected static MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * エラーメッセージ出力用のプリンタ
     */
    protected static MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "main";
        }
    }, MESSAGE_TYPE.ERROR);

    /**
     * 対象ファイルのASTから未解決クラス，フィールド，メソッド情報を取得
     */
    public void parseTargetFiles() {

        final Thread[] threads = new Thread[Settings.getInstance().getThreadNumber()];
        final TargetFile[] files = DataManager.getInstance().getTargetFileManager().getFiles()
                .toArray(new TargetFile[0]);
        final AtomicInteger index = new AtomicInteger();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new TargetFileParser(files, index, out, err));
            MetricsToolSecurityManager.getInstance().addPrivilegeThread(threads[i]);
            threads[i].start();
        }

        //全てのスレッドが終わるのを待つ
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * クラス，メソッド，フィールドなどの定義を名前解決する．AST パースの後に呼び出さなければならない．
     */
    private void resolveDefinitions() {

        // 未解決クラス情報マネージャ， クラス情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodManager = DataManager.getInstance().getMethodInfoManager();

        // 各未解決クラスに対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassManager.getClassInfos()) {

            final FileInfo fileInfo = unresolvedClassInfo.getFileInfo();

            //　クラス情報を解決
            final TargetClassInfo classInfo = unresolvedClassInfo.resolve(null, null, classManager,
                    fieldManager, methodManager);

            fileInfo.addDefinedClass(classInfo);

            // 解決されたクラス情報を登録
            classManager.add(classInfo);
        }

        // 各未解決クラスの外側のユニットを解決
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassManager.getClassInfos()) {
            unresolvedClassInfo.resolveOuterUnit(classManager);
        }
    }

    /**
     * クラスなどの定義の中で利用されている型を名前解決する．
     * resolveDefinitionsの後に呼び出されなければならない
     */
    private void resolveTypes() {

        // 未解決クラス情報マネージャ， クラス情報マネージャを取得
        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

        // 先に superTypだけ解決
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {
            unresolvedClassInfo.resolveSuperClass(classInfoManager);
        }

        // 残りのTypeを解決
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            unresolvedClassInfo.resolveTypeParameter(classInfoManager);

            for (final UnresolvedMethodInfo unresolvedMethod : unresolvedClassInfo
                    .getDefinedMethods()) {
                unresolvedMethod.resolveParameter(classInfoManager);
                unresolvedMethod.resolveReturnType(classInfoManager);
                unresolvedMethod.resolveThrownException(classInfoManager);
                unresolvedMethod.resolveTypeParameter(classInfoManager);
            }

            for (final UnresolvedConstructorInfo unresolvedConstructor : unresolvedClassInfo
                    .getDefinedConstructors()) {
                unresolvedConstructor.resolveParameter(classInfoManager);
                unresolvedConstructor.resolveThrownException(classInfoManager);
                unresolvedConstructor.resolveTypeParameter(classInfoManager);
            }

            for (final UnresolvedFieldInfo unresolvedField : unresolvedClassInfo.getDefinedFields()) {
                unresolvedField.resolveType(classInfoManager);
            }
        }
    }

    /**
     * メソッドオーバーライド情報を各MethodInfoに追加する．addInheritanceInfomationToClassInfos の後 かつ registMethodInfos
     * の後に呼び出さなければならない
     */
    private void addOverrideRelation() {

        // 全ての外部クラスに対して
        for (final ExternalClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getExternalClassInfos()) {
            addOverrideRelation(classInfo);

        }

        // 全ての対象クラスに対して
        for (final TargetClassInfo classInfo : DataManager.getInstance().getClassInfoManager()
                .getTargetClassInfos()) {
            addOverrideRelation(classInfo);
        }
    }

    /**
     * メソッドオーバーライド情報を各MethodInfoに追加する．引数で指定したクラスのメソッドについて処理を行う
     * 
     * @param classInfo 対象クラス
     */
    private void addOverrideRelation(final ClassInfo classInfo) {

        // 各親クラスに対して
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            // 各対象クラスの各メソッドについて，親クラスのメソッドをオーバーライドしているかを調査
            for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {
                addOverrideRelation(superClassInfo, methodInfo);
            }
        }

        // 各インナークラスに対して
        for (InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            addOverrideRelation((ClassInfo) innerClassInfo);
        }
    }

    /**
     * メソッドオーバーライド情報を追加する．引数で指定されたクラスで定義されているメソッドに対して操作を行う.
     * AddOverrideInformationToMethodInfos()の中からのみ呼び出される．
     * 
     * @param classInfo クラス情報
     * @param overrider オーバーライド対象のメソッド
     */
    private void addOverrideRelation(final ClassInfo classInfo, final MethodInfo overrider) {

        if ((null == classInfo) || (null == overrider)) {
            throw new IllegalArgumentException();
        }

        for (final MethodInfo methodInfo : classInfo.getDefinedMethods()) {

            // メソッド名が違う場合はオーバーライドされない
            if (!methodInfo.getMethodName().equals(overrider.getMethodName())) {
                continue;
            }

            if (0 != methodInfo.compareArgumentsTo(overrider)) {
                continue;
            }

            // オーバーライド関係を登録する
            overrider.addOverridee(methodInfo);
            methodInfo.addOverrider(overrider);

            // 直接のオーバーライド関係しか抽出しないので，このクラスの親クラスは調査しない
            return;
        }

        // 親クラス群に対して再帰的に処理
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            addOverrideRelation(superClassInfo, overrider);
        }
    }

    /**
     * エンティティ（フィールドやクラス）の代入・参照，メソッドの呼び出し関係を追加する．
     */
    private void addMethodInsideInfomation() {

        final UnresolvedClassInfoManager unresolvedClassInfoManager = DataManager.getInstance()
                .getUnresolvedClassInfoManager();
        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();
        final FieldInfoManager fieldInfoManager = DataManager.getInstance().getFieldInfoManager();
        final MethodInfoManager methodInfoManager = DataManager.getInstance()
                .getMethodInfoManager();

        // 各未解決クラス情報 に対して
        for (final UnresolvedClassInfo unresolvedClassInfo : unresolvedClassInfoManager
                .getClassInfos()) {

            final TargetClassInfo classInfo = unresolvedClassInfo.getResolved();

            // 未解決フィールド情報に対して
            for (final UnresolvedFieldInfo unresolvedFieldInfo : unresolvedClassInfo
                    .getDefinedFields()) {
                final TargetFieldInfo fieldInfo = unresolvedFieldInfo.getResolved();
                if (null != unresolvedFieldInfo.getInitilizer()) {
                    final CallableUnitInfo initializerUnit = fieldInfo.isInstanceMember() ? classInfo
                            .getImplicitInstanceInitializer() : classInfo
                            .getImplicitStaticInitializer();
                    final ExpressionInfo initializerExpression = unresolvedFieldInfo
                            .getInitilizer().resolve(classInfo, initializerUnit, classInfoManager,
                                    fieldInfoManager, methodInfoManager);
                    fieldInfo.setInitializer(initializerExpression);

                    // regist as an initializer
                    // TODO need more SMART way
                    final LocalVariableInfo fieldInfoAsLocalVariable = new LocalVariableInfo(
                            fieldInfo.getModifiers(), fieldInfo.getName(), fieldInfo.getType(),
                            initializerUnit, fieldInfo.getFromLine(), fieldInfo.getFromColumn(),
                            fieldInfo.getToLine(), fieldInfo.getToColumn());
                    final LocalVariableUsageInfo fieldUsage = LocalVariableUsageInfo.getInstance(
                            fieldInfoAsLocalVariable, false, true, initializerUnit,
                            fieldInfo.getFromLine(), fieldInfo.getFromColumn(),
                            fieldInfo.getToLine(), fieldInfo.getToColumn());
                    final VariableDeclarationStatementInfo implicitInitializerStatement = new VariableDeclarationStatementInfo(
                            initializerUnit, fieldUsage, initializerExpression,
                            fieldInfo.getFromLine(), fieldInfo.getFromColumn(),
                            initializerExpression.getToLine(), initializerExpression.getToColumn());
                    initializerUnit.addStatement(implicitInitializerStatement);
                }
            }

            // 各未解決メソッド情報に対して
            for (final UnresolvedMethodInfo unresolvedMethod : unresolvedClassInfo
                    .getDefinedMethods()) {

                final TargetMethodInfo method = unresolvedMethod.getResolved();
                unresolvedMethod.resolveInnerBlock(classInfo, method, classInfoManager,
                        fieldInfoManager, methodInfoManager);
            }

            // 各未解決コンストラクタ情報に対して
            for (final UnresolvedConstructorInfo unresolvedConstructor : unresolvedClassInfo
                    .getDefinedConstructors()) {

                final TargetConstructorInfo constructor = unresolvedConstructor.getResolved();
                unresolvedConstructor.resolveInnerBlock(classInfo, constructor, classInfoManager,
                        fieldInfoManager, methodInfoManager);
            }

            // resolve UnresolvedInstanceInitializers and register them
            for (final UnresolvedInstanceInitializerInfo unresolvedInstanceInitializer : unresolvedClassInfo
                    .getInstanceInitializers()) {

                final InstanceInitializerInfo initializer = unresolvedInstanceInitializer
                        .getResolved();
                unresolvedInstanceInitializer.resolveInnerBlock(classInfo, initializer,
                        classInfoManager, fieldInfoManager, methodInfoManager);
            }

            // resolve UnresolvedStaticInitializers and register them
            for (final UnresolvedStaticInitializerInfo unresolvedStaticInitializer : unresolvedClassInfo
                    .getStaticInitializers()) {

                final StaticInitializerInfo initializer = unresolvedStaticInitializer.getResolved();
                unresolvedStaticInitializer.resolveInnerBlock(classInfo, initializer,
                        classInfoManager, fieldInfoManager, methodInfoManager);
            }
        }
    }
}
