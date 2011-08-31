package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラスの情報を保有するクラス．以下の情報を持つ．
 * <ul>
 * <li>クラス名</li>
 * <li>修飾子</li>
 * <li>名前空間（パッケージ名）</li>
 * <li>行数</li>
 * <li>継承しているクラス</li>
 * <li>継承されているクラス</li>
 * <li>参照しているクラス</li>
 * <li>参照されているクラス</li>
 * <li>内部クラス</li>
 * <li>このクラス内で定義されているメソッド</li>
 * <li>このクラス内で定義されているフィールド</li>
 * </ul>
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class TargetClassInfo extends ClassInfo {

    /**
     * 指定されたクラスに含まれる全てのインナークラスを返す
     * 
     * @param classInfo 指定するクラス
     * @return　指定されたクラスに含まれる全てのインナークラス
     */
    static public SortedSet<ClassInfo> getAllInnerClasses(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> innerClassInfos = new TreeSet<ClassInfo>();
        for (final InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {

            innerClassInfos.add((ClassInfo) innerClassInfo);
            innerClassInfos.addAll(getAllInnerClasses((ClassInfo) innerClassInfo));
        }

        return Collections.unmodifiableSortedSet(innerClassInfos);
    }

    /**
     * 指定したクラスにおいてアクセス可能なインナークラス一覧を返す．
     * アクセス可能なクラスとは，指定されたクラス，もしくはその親クラス内に定義されたクラスある．
     * 一度最外部クラスまでたどって，その内部クラス，親クラスをチェックする.
     * 
     * @param classInfo 指定されたクラス
     * @return 指定したクラスにおいてアクセス可能なインナークラス一覧を返す．
     */
    static public SortedSet<ClassInfo> getAccessibleInnerClasses(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final SortedSet<ClassInfo> classCache = new TreeSet<ClassInfo>();

        if (classInfo instanceof InnerClassInfo) {

            final ClassInfo outestClass = TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) classInfo);
            return Collections.unmodifiableSortedSet(getAccessibleInnerClasses(outestClass,
                    classCache));
        } else {
            return Collections.unmodifiableSortedSet(getAccessibleInnerClasses(classInfo,
                    classCache));
        }
    }

    static private SortedSet<ClassInfo> getAccessibleInnerClasses(final ClassInfo classInfo,
            final SortedSet<ClassInfo> classCache) {

        if ((null == classInfo) || (null == classCache)) {
            throw new IllegalArgumentException();
        }

        if (classCache.contains(classInfo)) {
            return Collections.unmodifiableSortedSet(new TreeSet<ClassInfo>());
        }

        classCache.add(classInfo);

        final SortedSet<ClassInfo> innerClassInfos = new TreeSet<ClassInfo>();

        for (final InnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            innerClassInfos.add((ClassInfo) innerClassInfo);
            innerClassInfos
                    .addAll(getAccessibleInnerClasses((ClassInfo) innerClassInfo, classCache));
        }

        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClassInfo instanceof InnerClassInfo) {
                innerClassInfos.add(superClassInfo);
            }
            innerClassInfos.addAll(getAccessibleInnerClasses(superClassInfo, classCache));
        }

        return Collections.unmodifiableSortedSet(innerClassInfos);
    }

    /**
     * 名前空間名，クラス名を与えてクラス情報オブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param namespace 名前空間名
     * @param className クラス名
     * @param isInterface インターフェースかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean isInterface, final FileInfo fileInfo,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, namespace, className, isInterface, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.implicitInstanceInitializer = new InstanceInitializerInfo(0, 0, 0, 0);
        this.implicitInstanceInitializer.setOuterUnit(this);
        this.implicitStaticInitializer = new StaticInitializerInfo(0, 0, 0, 0);
        this.implicitStaticInitializer.setOuterUnit(this);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.instanceInitializers.add(this.implicitInstanceInitializer);
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.importStatements = new TreeSet<ImportStatementInfo<?>>();

        this.ownerFile = fileInfo;
    }

    /**
     * 完全限定名を与えて，クラス情報オブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param fullQualifiedName 完全限定名
     * @param isInterface インタフェースであるかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fullQualifiedName, isInterface, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers || null == fileInfo) {
            throw new NullPointerException();
        }

        this.implicitInstanceInitializer = new InstanceInitializerInfo(0, 0, 0, 0);
        this.implicitInstanceInitializer.setOuterUnit(this);
        this.implicitStaticInitializer = new StaticInitializerInfo(0, 0, 0, 0);
        this.implicitStaticInitializer.setOuterUnit(this);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.importStatements = new TreeSet<ImportStatementInfo<?>>();

        this.ownerFile = fileInfo;
    }

    /**
     * インスタンスイニシャライザーを追加する
     * 
     * @param instanceInitializer 追加されるインスタンスイニシャライザー
     */
    public final void addInstanceInitializer(final InstanceInitializerInfo instanceInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == instanceInitializer) {
            throw new NullPointerException();
        }

        this.instanceInitializers.add(instanceInitializer);
    }

    /**
     * スタティックイニシャライザーを追加する
     * 
     * @param staticInitializer 追加されるスタティックイニシャライザー
     */
    public final void addStaticInitializer(final StaticInitializerInfo staticInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == staticInitializer) {
            throw new NullPointerException();
        }

        this.staticInitializers.add(staticInitializer);
    }
    
    public final void addImportStatement(final ImportStatementInfo<?> importStatement){
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == importStatement) {
            throw new NullPointerException();
        }
        
        this.importStatements.add(importStatement);
    }

    /**
     * このクラスの暗黙のインスタンスイニシャライザを返す
     * @return 暗黙のインスタンスイニシャライザ
     */
    public InstanceInitializerInfo getImplicitInstanceInitializer() {
        return this.implicitInstanceInitializer;
    }

    /**
     * このクラスのインスタンスイニシャライザ一覧を返す
     * @return このクラスのインスタンスイニシャライザ一覧
     */
    public SortedSet<InstanceInitializerInfo> getInstanceInitializers() {
        return this.instanceInitializers;
    }

    /**
     * このクラスの暗黙のスタティックイニシャライザを返す
     * @return 暗黙のスタティックイニシャライザ
     */
    public StaticInitializerInfo getImplicitStaticInitializer() {
        return this.implicitStaticInitializer;
    }

    /**
     * このクラスのスタティックイニシャライザ一覧を返す
     * @return スタティックイニシャライザ一覧
     */
    public SortedSet<StaticInitializerInfo> getStaticInitializers() {
        return this.staticInitializers;
    }

    /**
     * このクラス内における変数使用のSetを返す
     * 
     * @return このクラス内における変数使用のSet
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {

        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        // メソッド内で使用されている変数を追加
        for (final MethodInfo definedMethod : this.getDefinedMethods()) {
            variableUsages.addAll(definedMethod.getVariableUsages());
        }

        // コンストラクタ内で使用されている変数を追加
        for (final ConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            variableUsages.addAll(definedConstructor.getVariableUsages());
        }

        // 内部クラスで使用されている変数を追加
        for (final InnerClassInfo innerClass : this.getInnerClasses()) {
            variableUsages.addAll(((ClassInfo) innerClass).getVariableUsages());
        }

        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * このクラス内で定義されている変数のSetを返す
     * 
     * @return このクラス内で定義されている変数のSet
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {

        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();

        // 定義されているフィールドを追加
        definedVariables.addAll(this.getDefinedFields());

        // メソッド内で定義されている変数を追加
        for (final MethodInfo definedMethod : this.getDefinedMethods()) {
            definedVariables.addAll(definedMethod.getDefinedVariables());
        }

        // コンストラクタ内で定義されている変数を追加
        for (final ConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            definedVariables.addAll(definedConstructor.getDefinedVariables());
        }

        // 内部クラスで定義されている変数を追加
        for (final InnerClassInfo innerClass : this.getInnerClasses()) {
            definedVariables.addAll(((ClassInfo) innerClass).getDefinedVariables());
        }

        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * このクラス内における呼び出しのSetを返す
     * 
     * @return このクラス内における呼び出しのSet
     */
    @Override
    public final Set<CallInfo<? extends CallableUnitInfo>> getCalls() {

        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();

        // メソッド内での呼び出しを追加
        for (final MethodInfo definedMethod : this.getDefinedMethods()) {
            calls.addAll(definedMethod.getCalls());
        }

        // コンストラクタ内での呼び出しを追加
        for (final ConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            calls.addAll(definedConstructor.getCalls());
        }

        // 内部クラスでの呼び出しを追加
        for (final InnerClassInfo innerClass : this.getInnerClasses()) {
            calls.addAll(((ClassInfo) innerClass).getCalls());
        }

        return Collections.unmodifiableSet(calls);
    }

    /**
     * このクラスを宣言しているファイル情報を返す
     * 
     * @return このクラスを宣言しているファイル情報
     */
    public final FileInfo getOwnerFile() {
        return this.ownerFile;
    }

    /**
     * このクラスのスタティックイニシャライザ一覧を保存するための変数
     */
    private final SortedSet<StaticInitializerInfo> staticInitializers;

    /**
     * このクラスのインスタンスイニシャライザ一覧を保存するための変数
     */
    private final SortedSet<InstanceInitializerInfo> instanceInitializers;

    /**
     * このクラスの暗黙のインスタンスイニシャライザを保存するための変数
     */
    private final InstanceInitializerInfo implicitInstanceInitializer;

    /**
     * このクラスの暗黙のスタティックイニシャライザを保存するための変数
     */
    private final StaticInitializerInfo implicitStaticInitializer;

   
    private final SortedSet<ImportStatementInfo<?>> importStatements;
    
    /**
     * このクラスを宣言しているファイル情報を保存するための変数
     */
    private final FileInfo ownerFile;
}
