package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetAnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;


/**
 * 未解決型情報を解決するためのユーティリティクラス
 * 
 * @author higo
 * 
 */
public final class NameResolver {

    /**
     * 引数で与えられたクラスの親クラスであり，かつ外部クラス(ExternalClassInfo)であるものを返す． クラス階層的に最も下位に位置する外部クラスを返す．
     * 該当するクラスが存在しない場合は， null を返す．
     * 
     * @param classInfo 対象クラス
     * @return 引数で与えられたクラスの親クラスであり，かつクラス階層的に最も下位に位置する外部クラス
     */
    public static ExternalClassInfo getExternalSuperClass(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            if (superClassInfo instanceof ExternalClassInfo) {
                return (ExternalClassInfo) superClassInfo;
            }

            final ExternalClassInfo superSuperClassInfo = NameResolver
                    .getExternalSuperClass(superClassInfo);
            if (null != superSuperClassInfo) {
                return superSuperClassInfo;
            }
        }

        return null;
    }

    /**
     * 引数で与えられたクラスを内部クラスとして持つ，最も外側の（インナークラスでない）クラスを返す
     * 
     * @param innerClass インナークラス
     * @return 最も外側のクラス
     */
    public static ClassInfo getOuterstClass(final InnerClassInfo innerClass) {

        if (null == innerClass) {
            throw new IllegalArgumentException();
        }

        final ClassInfo outerClass = innerClass.getOuterClass();
        return outerClass instanceof InnerClassInfo ? NameResolver
                .getOuterstClass((InnerClassInfo) outerClass) : outerClass;
    }

    /**
     * 引数で与えられたクラス内の利用可能な内部クラスの SortedSet を返す
     * 
     * @param classInfo クラス
     * @return 引数で与えられたクラス内の利用可能な内部クラスの SortedSet
     */
    public static SortedSet<InnerClassInfo> getAvailableInnerClasses(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final SortedSet<InnerClassInfo> innerClasses = new TreeSet<InnerClassInfo>();
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {

            innerClasses.add(innerClass);
            final SortedSet<InnerClassInfo> innerClassesInInnerClass = NameResolver
                    .getAvailableInnerClasses((ClassInfo) innerClass);
            innerClasses.addAll(innerClassesInInnerClass);
        }

        return Collections.unmodifiableSortedSet(innerClasses);
    }

    public static void getAvailableSuperClasses(final ClassInfo subClass,
            final ClassInfo superClass, final List<ClassInfo> availableClasses) {

        if ((null == subClass) || (null == superClass) || (null == availableClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (availableClasses.contains(superClass)) {
            return;
        }

        // 自クラスを追加
        // 子クラスと親クラスの名前空間が同じ場合は，名前空間可視もしくは継承可視があればよい
        if (subClass.getNamespace().equals(superClass.getNamespace())) {

            if (superClass.isInheritanceVisible() || superClass.isNamespaceVisible()) {
                availableClasses.add(superClass);
                for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
                    NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
                }
            }

            //子クラスと親クラスの名前空間が違う場合は，継承可視があればよい
        } else {

            if (superClass.isInheritanceVisible()) {
                availableClasses.add(superClass);
                for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
                    NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
                }
            }
        }

        // 親クラスを追加
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            NameResolver.getAvailableSuperClasses(subClass, superSuperClass, availableClasses);
        }
    }

    public static void getAvailableInnerClasses(final ClassInfo classInfo,
            final List<ClassInfo> availableClasses) {

        if ((null == classInfo) || (null == availableClasses)) {
            throw new NullPointerException();
        }

        // 既にチェックしたクラスである場合は何もせずに終了する
        if (availableClasses.contains(classInfo)) {
            return;
        }

        // 無名インナークラスの場合は追加せずに終了する
        if (classInfo instanceof AnonymousClassInfo) {
            return;
        }

        availableClasses.add(classInfo);

        // 内部クラスを追加
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
        }

        return;
    }

    /**
     * 引数で与えられたクラス型で呼び出し可能なコンストラクタのListを返す
     * 
     * @param classType
     * @return
     */
    public static final List<ConstructorInfo> getAvailableConstructors(final ClassTypeInfo classType) {

        final List<ConstructorInfo> constructors = new LinkedList<ConstructorInfo>();
        final ClassInfo classInfo = classType.getReferencedClass();

        constructors.addAll(classInfo.getDefinedConstructors());

        for (final ClassTypeInfo superClassType : classInfo.getSuperClasses()) {
            final List<ConstructorInfo> superConstructors = NameResolver
                    .getAvailableConstructors(superClassType);
            constructors.addAll(superConstructors);
        }

        return constructors;
    }

    /**
     * 引数で与えられたクラスの直接のインナークラスを返す．親クラスで定義されたインナークラスも含まれる．
     * 
     * @param classInfo クラス
     * @return 引数で与えられたクラスの直接のインナークラス，親クラスで定義されたインナークラスも含まれる．
     */
    public static final SortedSet<InnerClassInfo> getAvailableDirectInnerClasses(
            final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final SortedSet<InnerClassInfo> availableDirectInnerClasses = new TreeSet<InnerClassInfo>();

        // 引数で与えられたクラスの直接のインナークラスを追加
        availableDirectInnerClasses.addAll(classInfo.getInnerClasses());

        // 親クラスに対して再帰的に処理
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            final SortedSet<InnerClassInfo> availableDirectInnerClassesInSuperClass = NameResolver
                    .getAvailableDirectInnerClasses((ClassInfo) superClassInfo);
            availableDirectInnerClasses.addAll(availableDirectInnerClassesInSuperClass);
        }

        return Collections.unmodifiableSortedSet(availableDirectInnerClasses);
    }

    public static final List<TypeParameterInfo> getAvailableTypeParameters(
            final TypeParameterizable unit) {

        if (null == unit) {
            throw new IllegalArgumentException();
        }

        final List<TypeParameterInfo> typeParameters = new LinkedList<TypeParameterInfo>();

        typeParameters.addAll(unit.getTypeParameters());
        final TypeParameterizable outerUnit = unit.getOuterTypeParameterizableUnit();
        if (null != outerUnit) {
            typeParameters.addAll(getAvailableTypeParameters(outerUnit));
        }

        return Collections.unmodifiableList(typeParameters);
    }

    /**
     * 引数で指定されたクラスで利用可能な，クラスのListを返す
     * 
     * @param usingClass
     * @return
     */
    public static synchronized List<ClassInfo> getAvailableClasses(final ClassInfo usingClass) {

        if (CLASS_CACHE.containsKey(usingClass)) {
            return CLASS_CACHE.get(usingClass);
        } else {
            final List<ClassInfo> _SAME_CLASS = new NonDuplicationLinkedList<ClassInfo>();
            final List<ClassInfo> _INHERITANCE = new NonDuplicationLinkedList<ClassInfo>();
            final List<ClassInfo> _SAME_NAMESPACE = new NonDuplicationLinkedList<ClassInfo>();

            getAvailableClasses(usingClass, usingClass, new HashSet<ClassInfo>(), _SAME_CLASS,
                    _INHERITANCE, _SAME_NAMESPACE);

            final List<ClassInfo> availableClasses = new NonDuplicationLinkedList<ClassInfo>();
            availableClasses.addAll(_SAME_CLASS);
            availableClasses.addAll(_INHERITANCE);
            availableClasses.addAll(_SAME_NAMESPACE);

            final ClassInfo outestClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            availableClasses.addAll(DataManager.getInstance().getClassInfoManager().getClassInfos(
                    outestClass.getNamespace()));
            CLASS_CACHE.put(usingClass, availableClasses);
            return Collections.unmodifiableList(availableClasses);
        }
    }

    private static void getAvailableClasses(final ClassInfo usedClass, final ClassInfo usingClass,
            Set<ClassInfo> checkedClasses, final List<ClassInfo> _SAME_CLASS,
            final List<ClassInfo> _INHERITANCE, final List<ClassInfo> _SAME_NAMESPACE) {

        if (checkedClasses.contains(usedClass)) {
            return;
        }

        if (usedClass instanceof TargetAnonymousClassInfo) {
            return;
        }

        checkedClasses.add(usedClass);

        // usedが利用可能かどうかを調査し，可能であればリストに追加
        if (!addAvailableClass(usedClass, usingClass, _SAME_CLASS, _INHERITANCE, _SAME_NAMESPACE)) {
            return;
        }

        for (final InnerClassInfo innerClass : usedClass.getInnerClasses()) {
            getAvailableClasses((ClassInfo) innerClass, usingClass, checkedClasses, _SAME_CLASS,
                    _INHERITANCE, _SAME_NAMESPACE);
        }

        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerUsedClass = ((InnerClassInfo) usedClass).getOuterClass();
            getAvailableClasses(outerUsedClass, usingClass, checkedClasses, _SAME_CLASS,
                    _INHERITANCE, _SAME_NAMESPACE);
        }

        for (final ClassTypeInfo superUsedType : usedClass.getSuperClasses()) {
            final ClassInfo superUsedClass = superUsedType.getReferencedClass();
            getAvailableClasses(superUsedClass, usingClass, checkedClasses, _SAME_CLASS,
                    _INHERITANCE, _SAME_NAMESPACE);
        }
    }

    /**
     * usedClassがusingClassにおいてアクセス可能かを返す．
     * なお，usedClassがpublicである場合は考慮していない．
     * publicでアクセス可能かどうかは，インポート文も調べなければわからない
     * 
     * @param usedClass
     * @param usingClass
     * @return
     */
    private static boolean addAvailableClass(final ClassInfo usedClass, final ClassInfo usingClass,
            final List<ClassInfo> _SAME_CLASS, final List<ClassInfo> _INHERITANCE,
            final List<ClassInfo> _SAME_NAMESPACE) {

        // usingとusedが同じであれば，利用可能
        if (usingClass.equals(usedClass)) {
            _SAME_CLASS.add(usedClass);
            return true;
        }

        // usedがインナークラスのとき
        if (usedClass instanceof InnerClassInfo) {

            final ClassInfo outerUsedClass = ((InnerClassInfo) usedClass).getOuterClass();

            //直のouterクラスからはアクセス可
            if (outerUsedClass.equals(usingClass)) {
                _SAME_CLASS.add(usedClass);
                return true;
            }

            // usingもインナークラスの場合は，usedと同じクラスのインナークラスかどうかを調べる
            if (usingClass instanceof InnerClassInfo) {
                final ClassInfo outerUsingClass = ((InnerClassInfo) usingClass).getOuterClass();
                if (outerUsedClass.equals(outerUsingClass)) {
                    _SAME_CLASS.add(usedClass);
                    return true;
                }
            }

            // 直のouterクラスとusingの名前空間が同じ場合
            if (outerUsedClass.getNamespace().equals(usingClass.getNamespace())) {
                if (outerUsedClass instanceof InnerClassInfo) {
                    _SAME_CLASS.add(usedClass);
                    return true;
                } else {
                    _SAME_NAMESPACE.add(usedClass);
                    return true;
                }
            }

            // 直のouterクラスがインナークラスでない場合
            if (!(outerUsedClass instanceof InnerClassInfo)) {
                final ClassInfo outestUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                        .getOutestClass((InnerClassInfo) usingClass)
                        : usingClass;

                // 名前空間が同じ時
                if (outerUsedClass.getNamespace().equals(outestUsingClass.getNamespace())) {

                    ClassInfo outerUsingClass = usingClass;
                    while (true) {
                        if (outerUsingClass.isSubClass(outerUsedClass)) {
                            _INHERITANCE.add(usedClass);
                            return true;
                        }

                        if (!(outerUsingClass instanceof InnerClassInfo)) {
                            break;
                        }

                        outerUsingClass = ((InnerClassInfo) outerUsingClass).getOuterClass();
                    }
                }

                // 名前空間が違う時
                else {
                    if (usedClass.isInheritanceVisible()) {

                        ClassInfo outerUsingClass = usingClass;
                        while (true) {
                            if (outerUsingClass.isSubClass(outerUsedClass)) {
                                _INHERITANCE.add(usedClass);
                                return true;
                            }

                            if (!(outerUsingClass instanceof InnerClassInfo)) {
                                break;
                            }

                            outerUsingClass = ((InnerClassInfo) outerUsingClass).getOuterClass();
                        }
                    }
                }
            }
        }

        // usedがインナークラスでないとき
        else {

            // usingがインナークラスであれば，最外クラスを取得
            final ClassInfo outestUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;

            //usedとusingが同じクラス（内）のとき                    
            if (outestUsingClass.equals(usedClass)) {
                _SAME_CLASS.add(usedClass);
                return true;
            }

            if (outestUsingClass.getNamespace().equals(usedClass.getNamespace())) {
                _SAME_NAMESPACE.add(usedClass);
                return true;
            }

            if (outestUsingClass.isSubClass(usedClass)) {
                _INHERITANCE.add(usedClass);
                return true;
            }
        }

        return false;
    }

    /**
     * 使用するクラスと使用されるクラスを与えることにより，利用可能なメソッドのListを返す
     * 
     * @param usedClass 使用されるクラス
     * @param usingClass 使用するクラス
     * @return
     */
    public static synchronized List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final boolean hasCache = METHOD_CACHE.hasCash(usedClass, usingClass);
        if (hasCache) {
            return METHOD_CACHE.getCache(usedClass, usingClass);
        } else {
            final List<MethodInfo> methods = getAvailableMethods(usedClass, usingClass,
                    new HashSet<ClassInfo>());
            METHOD_CACHE.putCache(usedClass, usingClass, methods);
            return methods;
        }
    }

    /**
     * 使用するクラスと使用されるクラスを与えることにより，利用可能なフィールドのListを返す
     * 
     * @param usedClass 使用されるクラス
     * @param usingClass 使用するクラス
     * @return
     */
    public static synchronized List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final boolean hasCache = FIELD_CACHE.hasCash(usedClass, usingClass);
        if (hasCache) {
            return FIELD_CACHE.getCache(usedClass, usingClass);
        } else {
            final List<FieldInfo> fields = getAvailableFields(usedClass, usingClass,
                    new HashSet<ClassInfo>());
            FIELD_CACHE.putCache(usedClass, usingClass, fields);
            return fields;
        }
    }

    private static List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass, final Set<ClassInfo> checkedClasses) {

        // すでにチェックしているクラスであれば何もせずに抜ける
        if (checkedClasses.contains(usedClass)) {
            return Collections.<MethodInfo> emptyList();
        }

        // チェック済みクラスに追加
        checkedClasses.add(usedClass);

        // usedに定義されているメソッドのうち，利用可能なものを追加
        final List<MethodInfo> availableMethods = new NonDuplicationLinkedList<MethodInfo>();
        availableMethods.addAll(extractAvailableMethods(usedClass, usingClass));

        // usedの外クラスをチェック
        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerClass = ((InnerClassInfo) usedClass).getOuterClass();
            availableMethods.addAll(getAvailableMethods(outerClass, usingClass, checkedClasses));
        }

        // 親クラスをチェック
        for (final ClassTypeInfo superClassType : usedClass.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            availableMethods.addAll(getAvailableMethods(superClass, usingClass, checkedClasses));
        }

        // オーバーライドにより呼び出し不可となったメソッドは削除
        final List<MethodInfo> deletedMethods = new NonDuplicationLinkedList<MethodInfo>();
        for (final MethodInfo method : availableMethods) {
            deletedMethods.addAll(method.getOverridees());
        }
        availableMethods.removeAll(deletedMethods);

        return availableMethods;
    }

    private static List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass, final Set<ClassInfo> checkedClasses) {

        // すでにチェックしているクラスであれば何もせずに抜ける
        if (checkedClasses.contains(usedClass)) {
            return Collections.<FieldInfo> emptyList();
        }

        // チェック済みクラスに追加
        checkedClasses.add(usedClass);

        // usedに定義されているメソッドのうち，利用可能なものを追加
        final List<FieldInfo> availableFields = new NonDuplicationLinkedList<FieldInfo>();
        availableFields.addAll(extractAvailableFields(usedClass, usingClass));

        // usedの外クラスをチェック
        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerClass = ((InnerClassInfo) usedClass).getOuterClass();
            availableFields.addAll(getAvailableFields(outerClass, usingClass, checkedClasses));
        }

        // 親クラスをチェック
        for (final ClassTypeInfo superClassType : usedClass.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            availableFields.addAll(getAvailableFields(superClass, usingClass, checkedClasses));
        }

        return availableFields;
    }

    private static List<MethodInfo> extractAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final List<MethodInfo> availableMethods = new NonDuplicationLinkedList<MethodInfo>();

        // usingとusedが等しい場合は，すべてのメソッドを使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                availableMethods.addAll(usedClass.getDefinedMethods());
            }
        }

        // usingがusedと同じパッケージであれば，private 以外のメソッドが使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                for (final MethodInfo method : usedClass.getDefinedMethods()) {
                    if (method.isNamespaceVisible()) {
                        availableMethods.add(method);
                    }
                }
            }
        }

        // usingがusedのサブクラスであれば,public以外のメソッドが使用可能
        if (usingClass.isSubClass(usedClass)) {
            for (final MethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isInheritanceVisible()) {
                    availableMethods.add(method);
                }
            }
        }

        // usingの親クラスがusedのサブクラスであっても，public以外のメソッドを使用可能
        if (usingClass instanceof InnerClassInfo) {
            final ClassInfo outestUsingClass = TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass);
            if (outestUsingClass.isSubClass(usedClass)) {
                for (final MethodInfo method : usedClass.getDefinedMethods()) {
                    if (method.isInheritanceVisible()) {
                        availableMethods.add(method);
                    }
                }
            }
        }

        // usingがusedと関係のないクラスであれば，publicのメソッドが利用可能
        for (final MethodInfo method : usedClass.getDefinedMethods()) {
            if (method.isPublicVisible()) {
                availableMethods.add(method);
            }
        }

        return availableMethods;
    }

    private static List<FieldInfo> extractAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final List<FieldInfo> availableFields = new NonDuplicationLinkedList<FieldInfo>();

        // usingとusedが等しい場合は，すべてのフィールドを使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                availableFields.addAll(usedClass.getDefinedFields());
            }
        }

        // usingがusedと同じパッケージであれば，private 以外のフィールドが使用可能
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                for (final FieldInfo field : usedClass.getDefinedFields()) {
                    if (field.isNamespaceVisible()) {
                        availableFields.add(field);
                    }
                }
            }
        }

        // usingがusedのサブクラスであれば,protected以外のフィールドが使用可能
        if (usingClass.isSubClass(usedClass)) {
            for (final FieldInfo field : usedClass.getDefinedFields()) {
                if (field.isInheritanceVisible()) {
                    availableFields.add(field);
                }
            }
        }

        // usingがusedと関係のないクラスであれば，publicのフィールドが利用可能
        for (final FieldInfo field : usedClass.getDefinedFields()) {
            if (field.isPublicVisible()) {
                availableFields.add(field);
            }
        }

        return availableFields;
    }

    private static final Map<ClassInfo, List<ClassInfo>> CLASS_CACHE = new HashMap<ClassInfo, List<ClassInfo>>();

    private static final Cache<MethodInfo> METHOD_CACHE = new Cache<MethodInfo>();

    private static final Cache<FieldInfo> FIELD_CACHE = new Cache<FieldInfo>();

    /**
     * 使用するクラスと使用されるクラスの関係から利用可能なメンバーのキャッシュを蓄えておくためのクラス
     * 
     * @author higo
     *
     * @param <T>
     */
    static class Cache<T> {

        private final ConcurrentMap<ClassInfo, ConcurrentMap<ClassInfo, List<T>>> firstCache;

        Cache() {
            this.firstCache = new ConcurrentHashMap<ClassInfo, ConcurrentMap<ClassInfo, List<T>>>();
        }

        boolean hasCash(final ClassInfo usedClass, final ClassInfo usingClass) {

            final boolean hasSecondCache = this.firstCache.containsKey(usedClass);
            if (!hasSecondCache) {
                return false;
            }

            final ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            final boolean hasThirdCache = secondCache.containsKey(usingClass);
            return hasThirdCache;
        }

        List<T> getCache(final ClassInfo usedClass, final ClassInfo usingClass) {

            final ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            if (null == secondCache) {
                return null;
            }

            return secondCache.get(usingClass);
        }

        void putCache(final ClassInfo usedClass, final ClassInfo usingClass, final List<T> cache) {

            ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            if (null == secondCache) {
                secondCache = new ConcurrentHashMap<ClassInfo, List<T>>();
                this.firstCache.put(usedClass, secondCache);
            }

            secondCache.put(usingClass, cache);
        }
    }

    @SuppressWarnings("serial")
    private static class NonDuplicationLinkedList<T> extends LinkedList<T> {

        @Override
        public boolean add(final T element) {
            if (super.contains(element)) {
                return false;
            } else {
                return super.add(element);
            }
        }

        @Override
        public boolean addAll(Collection<? extends T> elements) {

            boolean added = false;
            for (final T element : elements) {
                if (this.add(element)) {
                    added = true;
                }
            }

            return added;
        }
    }
}
