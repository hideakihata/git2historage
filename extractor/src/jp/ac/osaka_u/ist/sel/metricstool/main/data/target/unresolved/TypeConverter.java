package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * 型変換ユーティリティ．名前解決に用いる．
 * 
 * @author higo
 */
public abstract class TypeConverter {

    /**
     * 各言語向けの型変換器を返すファクトリメソッド（のようなもの）
     * 
     * @param language 言語
     * @return その言語用の型変換器
     */
    public static final TypeConverter getTypeConverter(final LANGUAGE language) {

        if (null == language) {
            throw new NullPointerException();
        }

        switch (language) {
        case JAVA15:
        case JAVA14:
        case JAVA13:
            return JavaTypeConverter.SINGLETON;
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * 引数で与えられたプリミティブ型のラッパークラスを返す
     * 
     * @param primitiveType プリミティブ型
     * @return 対応するラッパークラス
     */
    public abstract ClassInfo getWrapperClass(PrimitiveTypeInfo primitiveType);

}
