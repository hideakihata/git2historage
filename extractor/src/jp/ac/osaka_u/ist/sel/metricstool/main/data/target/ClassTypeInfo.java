package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 参照型を表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ClassTypeInfo implements ReferenceTypeInfo {

    /**
     * 参照型のListをクラスのListに変換する
     * 
     * @param references 参照型のList
     * @return クラスのList
     */
    public static List<ClassInfo> convert(final List<ClassTypeInfo> references) {

        final List<ClassInfo> classInfos = new LinkedList<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }

        return Collections.unmodifiableList(classInfos);
    }

    /**
     * 参照型のSortedSetをクラスのSortedSetに変換する
     * 
     * @param references 参照型のSortedSet
     * @return クラスのSortedSet
     */
    public static SortedSet<ClassInfo> convert(final SortedSet<ClassTypeInfo> references) {

        final SortedSet<ClassInfo> classInfos = new TreeSet<ClassInfo>();
        for (final ClassTypeInfo reference : references) {
            classInfos.add(reference.getReferencedClass());
        }

        return Collections.unmodifiableSortedSet(classInfos);
    }

    /**
     * 参照されるクラスを与えて初期化
     * 
     * @param referencedClass 参照されるクラス
     */
    public ClassTypeInfo(final ClassInfo referencedClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencedClass) {
            throw new NullPointerException();
        }

        this.referencedClass = referencedClass;
        this.typeArguments = new ArrayList<TypeInfo>();
    }

    /**
     * 引数で与えられた型を等しいかどうかを比較．
     * 
     * @return 等しい場合はtrue，等しくない場合はfalse
     */
    public boolean equals(TypeInfo typeInfo) {

        // 引数が null ならば，等しくない
        if (null == typeInfo) {
            return false;
        }

        // 引数が参照型でなければ，等しくない
        if (!(typeInfo instanceof ClassTypeInfo)) {
            return false;
        }

        // 引数が参照型の場合，
        // 参照されているクラスが等しくない場合は，参照型は等しくない
        final ClassTypeInfo targetReferenceType = (ClassTypeInfo) typeInfo;
        final ClassInfo targetReferencedClass = targetReferenceType.getReferencedClass();
        if (!this.referencedClass.equals(targetReferencedClass)) {
            return false;
        }

        // 型パラメータの数が異なる場合は，等しくない
        final List<TypeInfo> thisTypeParameters = this.typeArguments;
        final List<TypeInfo> targetTypeParameters = targetReferenceType.getTypeArguments();
        if (thisTypeParameters.size() != targetTypeParameters.size()) {
            return false;
        }

        // 全ての型パラメータが等しくなければ，等しくない
        final Iterator<TypeInfo> thisTypeParameterIterator = thisTypeParameters.iterator();
        final Iterator<TypeInfo> targetTypeParameterIterator = targetTypeParameters.iterator();
        while (thisTypeParameterIterator.hasNext()) {
            final TypeInfo thisTypeParameter = thisTypeParameterIterator.next();
            final TypeInfo targetTypeParameter = targetTypeParameterIterator.next();
            if (!thisTypeParameter.equals(targetTypeParameter)) {
                return false;
            }
        }

        return true;
    }

    /**
     * この参照型を表す文字列を返す
     * 
     * @return この参照型を表す文字列
     */
    public String getTypeName() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.referencedClass.getFullQualifiedName("."));

        if (0 < this.typeArguments.size()) {
            sb.append("<");
            for (final TypeInfo typeParameter : this.typeArguments) {
                sb.append(typeParameter.getTypeName());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(">");
        }

        return sb.toString();
    }

    /**
     * 参照されているクラスを返す
     * 
     * @return 参照されているクラス
     */
    public ClassInfo getReferencedClass() {
        return this.referencedClass;
    }

    /**
     * この参照型に用いられている型引数のリストを返す
     * 
     * @return この参照型に用いられている型引数のリストを返す
     */
    public List<TypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * この参照型のインデックスで指定された型引数を返す.
     * 
     * @param index 型引数のインデックス
     * @return　この参照型のインデックスで指定された型引数
     */
    public TypeInfo getTypeArgument(final int index) {

        final ClassInfo referencedClass = this.getReferencedClass();
        final List<TypeParameterInfo> typeParameters = referencedClass.getTypeParameters();

        // index が型パラメータが定義されている範囲外であれば例外をスロー
        if ((index < 0) || (typeParameters.size() <= index)) {
            throw new IllegalArgumentException();
        }

        // indexが型引数が定義されている範囲内であれば，それを返す
        if (index < this.typeArguments.size()) {
            return this.typeArguments.get(index);
        }

        //indexが型引数が定義されていない範囲であれば，Objectを返す
        else {
            final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "Object" });
            return new ClassTypeInfo(objectClass);
        }
    }

    /**
     * 引数で与えられた型パラメータに対応する型引数を返す
     * @param typeParameter
     * @return
     */
    public TypeInfo getTypeArgument(final TypeParameterInfo typeParameter) {

        final ClassInfo referencedClass = this.getReferencedClass();
        final List<TypeParameterInfo> typeParameters = referencedClass.getTypeParameters();
        if (typeParameters.contains(typeParameter)) {
            final int index = typeParameter.getIndex();
            return this.getTypeArgument(index);
        }

        for (final ClassTypeInfo superClassType : referencedClass.getSuperClasses()) {
            final TypeInfo superTypeArgumentType = superClassType.getTypeArgument(typeParameter);
            if (null == superTypeArgumentType) {
                continue;
            }

            // 親クラスの型引数の型が型パラメータだった場合
            if (superTypeArgumentType instanceof TypeParameterTypeInfo) {
                final TypeParameterInfo superTypeTypeParameter = ((TypeParameterTypeInfo) superTypeArgumentType)
                        .getReferncedTypeParameter();

                if (typeParameters.contains(superTypeTypeParameter)) {
                    final int index = typeParameter.getIndex();
                    return this.getTypeArgument(index);
                }

                // 本来はエラーを返すべき?
                else {
                    final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                            .getClassInfo(new String[] { "java", "lang", "Object" });
                    return new ClassTypeInfo(objectClass);
                }
            }

            // 親クラスの型引数の型が型パラメータでなかった場合
            else {
                return superTypeArgumentType;
            }
        }

        return null;
    }

    /**
     * この参照型に型引数を追加
     * 
     * @param argument 追加する型引数
     */
    public void addTypeArgument(final TypeInfo argument) {
        this.typeArguments.add(argument);
    }

    /**
     * この参照型が表すクラスを保存するための変数
     */
    private final ClassInfo referencedClass;

    /**
     * この参照型の型パラメータを保存するための変数
     */
    private final List<TypeInfo> typeArguments;

}
