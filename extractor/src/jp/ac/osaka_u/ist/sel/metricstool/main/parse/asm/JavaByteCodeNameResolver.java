package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArbitraryTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExtendsTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.NameResolver;


/**
 * バイトコードから得た未解決情報を名前解決するためのクラス
 * 
 * @author higo
 *
 */
public class JavaByteCodeNameResolver {

    /**
     * 未解決名前情報を名前解決するメソッド
     * 解決した名前のFull Qualified Nameを返す
     * 
     * @param unresolvedName
     * @return
     */
    public static String[] resolveName(final String unresolvedName) {
        return JavaByteCodeUtility.separateName(unresolvedName);
    }

    /**
     * 未解決型情報を名前解決するメソッド．
     * 第二，第三引数は，TypeParameterを解決する場合のみ指定すればよい.
     * しかし，解決する型が内部にジェネリクスを含んでいる場合があるので，
     * 第三引数はきちんと指定することが重要
     * ## 第二引数は削除されました.
     * 
     * @param unresolvedType
     * @param thisTypeParameter 型パラメータのextendsTypeを解決するときのみ必須
     * @param ownerUnit
     * 
     * @return
     */
    public static TypeInfo resolveType(final String unresolvedType,
            final TypeParameterInfo thisTypeParameter, final TypeParameterizable ownerUnit) {

        if (null == unresolvedType) {
            throw new IllegalArgumentException();
        }

        // 一文字ならば，primitiveTypeかVoidでなければならない
        if (1 == unresolvedType.length()) {
            return translateSingleCharacterType(unresolvedType.charAt(0));
        }

        // '['で始まっているときは配列
        else if ('[' == unresolvedType.charAt(0)) {
            final TypeInfo subType = resolveType(unresolvedType.substring(1), thisTypeParameter,
                    ownerUnit);

            // もともと配列ならば次元を1つ増やす
            if (subType instanceof ArrayTypeInfo) {
                final ArrayTypeInfo subArrayType = (ArrayTypeInfo) subType;
                final TypeInfo ElementType = subArrayType.getElementType();
                final int dimension = subArrayType.getDimension();
                return ArrayTypeInfo.getType(ElementType, dimension + 1);
            }

            //　配列でないなら配列にする
            else {
                return ArrayTypeInfo.getType(subType, 1);
            }
        }

        // 配列でない参照型の場合
        else if ('L' == unresolvedType.charAt(0)) {

            final ClassInfoManager classInfoManager = DataManager.getInstance()
                    .getClassInfoManager();
            final String[] unresolvedSeparatedType = JavaByteCodeUtility
                    .separateName(unresolvedType.substring(1, unresolvedType.length() - 1));
            final String[] unresolvedSeparatedTypeWithoutTypeArguments = new String[unresolvedSeparatedType.length];
            for (int index = 0; index < unresolvedSeparatedType.length; index++) {
                unresolvedSeparatedTypeWithoutTypeArguments[index] = JavaByteCodeUtility
                        .removeTypeArguments(unresolvedSeparatedType[index]);
            }

            ExternalClassInfo referencedClass = (ExternalClassInfo) classInfoManager
                    .getClassInfo(unresolvedSeparatedTypeWithoutTypeArguments);
            if (null == referencedClass) {
                referencedClass = new ExternalClassInfo(unresolvedSeparatedTypeWithoutTypeArguments);
                classInfoManager.add(referencedClass);
            }
            final ClassTypeInfo type = new ClassTypeInfo(referencedClass);

            final String unresolvedTypeArgumentsString = JavaByteCodeUtility
                    .extractTypeArguments(unresolvedSeparatedType[unresolvedSeparatedType.length - 1]);
            if (null != unresolvedTypeArgumentsString) {
                final String[] unresolvedTypeArguments = JavaByteCodeUtility
                        .separateTypes(unresolvedTypeArgumentsString);
                for (final String unresolvedTypeArgument : unresolvedTypeArguments) {
                    final TypeInfo typeArgument = resolveType(unresolvedTypeArgument,
                            thisTypeParameter, ownerUnit);
                    type.addTypeArgument(typeArgument);
                }
            }

            return type;
        }

        // ジェネリクス(TE(別にEじゃなくてもいいけど);)の場合
        else if ('T' == unresolvedType.charAt(0)) {

            final String identifier = unresolvedType.substring(1, unresolvedType.length() - 1);
            if ((null != thisTypeParameter) && identifier.equals(thisTypeParameter.getName())) {
                return new TypeParameterTypeInfo(thisTypeParameter);
            }
            final List<TypeParameterInfo> availableTypeParameters = NameResolver
                    .getAvailableTypeParameters(ownerUnit);
            for (final TypeParameterInfo typeParameter : availableTypeParameters) {
                if (identifier.equals(typeParameter.getName())) {
                    return new TypeParameterTypeInfo(typeParameter);
                }
            }
        }

        // ジェネリクス(-)の場合
        else if ('-' == unresolvedType.charAt(0)) {

            final String unresolvedSuperType = unresolvedType.substring(1);
            final TypeInfo superType = resolveType(unresolvedSuperType, thisTypeParameter,
                    ownerUnit);
            assert superType instanceof ReferenceTypeInfo : "superType must be instanceof ReferenceTypeInfo";
            return new SuperTypeInfo((ReferenceTypeInfo) superType);
        }

        // ジェネリクス(+)の場合
        else if ('+' == unresolvedType.charAt(0)) {

            final String unresolvedExtendsType = unresolvedType.substring(1);
            final TypeInfo extendsType = resolveType(unresolvedExtendsType, thisTypeParameter,
                    ownerUnit);
            assert extendsType instanceof ReferenceTypeInfo : "extendsType must be instanceof ReferenceTypeInfo";
            return new ExtendsTypeInfo((ReferenceTypeInfo) extendsType);
        }

        throw new IllegalArgumentException();
    }

    /**
     * 未解決型パラメータ情報を名前解決するクラス.
     * 第二，第三引数は，TypeParameterを解決する場合のみ指定すればよい.
     * しかし，解決する型が内部にジェネリクスを含んでいる場合があるので，
     * 第三引数はきちんと指定することが重要
     * 
     * @param unresolvedTypeParameter 未解決型の文字列
     * @param index 型パラメータのインデックス（順番）
     * @param ownerUnit 型パラメータを所有するユニット（クラス or メソッド or コンストラクタ）
     * @return
     */
    public static TypeParameterInfo resolveTypeParameter(final String unresolvedTypeParameter,
            final int index, final TypeParameterizable ownerUnit) {

        if ((null == unresolvedTypeParameter) || (null == ownerUnit)) {
            throw new IllegalArgumentException();
        }

        final int firstColonIndex = unresolvedTypeParameter.indexOf(":");

        final String identifier = unresolvedTypeParameter.substring(0, firstColonIndex);
        final TypeParameterInfo typeParameter = new TypeParameterInfo(ownerUnit, identifier, index);

        final String unresolvedExtendsTypes = unresolvedTypeParameter.substring(firstColonIndex);
        for (int startIndex = 0, endIndex = 0, nestLevel = 0; endIndex < unresolvedExtendsTypes
                .length(); endIndex++) {

            if ((':' == unresolvedExtendsTypes.charAt(endIndex)) && (0 == nestLevel)) {
                startIndex = endIndex + 1;
            }

            else if ((';' == unresolvedExtendsTypes.charAt(endIndex)) && (0 == nestLevel)) {
                final String unresolvedExtendsType = unresolvedExtendsTypes.substring(startIndex,
                        endIndex + 1);
                final TypeInfo extendsType = resolveType(unresolvedExtendsType, typeParameter,
                        ownerUnit);
                typeParameter.addExtendsType(extendsType);
            }

            else if ('<' == unresolvedExtendsTypes.charAt(endIndex)) {
                nestLevel++;
            }

            else if ('>' == unresolvedExtendsTypes.charAt(endIndex)) {
                nestLevel--;
            }
        }

        return typeParameter;
    }

    /**
     * 与えられた一文字型を表す文字をもとに，型を表すオブジェクトを返す
     * 
     * @param c
     * @return
     */
    private static TypeInfo translateSingleCharacterType(final char c) {

        switch (c) {
        case 'Z':
            return PrimitiveTypeInfo.BOOLEAN;
        case 'C':
            return PrimitiveTypeInfo.CHAR;
        case 'B':
            return PrimitiveTypeInfo.BYTE;
        case 'S':
            return PrimitiveTypeInfo.SHORT;
        case 'I':
            return PrimitiveTypeInfo.INT;
        case 'F':
            return PrimitiveTypeInfo.FLOAT;
        case 'J':
            return PrimitiveTypeInfo.LONG;
        case 'D':
            return PrimitiveTypeInfo.DOUBLE;
        case 'V':
            return VoidTypeInfo.getInstance();
        case '*':
            return ArbitraryTypeInfo.getInstance();
        default:
            throw new IllegalArgumentException();
        }
    }

}
