package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 配列型を表すためのクラス．
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ArrayTypeInfo implements ReferenceTypeInfo {

    /**
     * 型名を返す
     * 
     * @return 型名
     */
    @Override
    public String getTypeName() {
        final TypeInfo elementType = this.getElementType();
        final int dimension = this.getDimension();

        final StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getTypeName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    /**
     * 等しいかどうかのチェックを行う
     * 
     * @param typeInfo 比較対象型
     * @return 等しい場合はtrue, そうでない場合はfalse
     */
    @Override
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof ArrayTypeInfo)) {
            return false;
        }

        final TypeInfo element = this.getElementType();
        final TypeInfo correspondElement = ((ArrayTypeInfo) typeInfo).getElementType();
        if (!element.equals(correspondElement)) {
            return false;
        }

        final int dimension = this.getDimension();
        final int correspondDimension = ((ArrayTypeInfo) typeInfo).getDimension();
        return dimension == correspondDimension;
    }

    /**
     * 配列の要素を返す
     * 
     * @return 配列の要素の型
     */
    public final TypeInfo getElementType() {
        return this.element;
    }

    /**
     * 配列の次元を返す
     * 
     * @return 配列の次元
     */
    public final int getDimension() {
        return this.dimension;
    }

    /**
     * ArrayTypeInfo のインスタンスを返すためのファクトリメソッド．
     * 
     * @param element 型を表す変数
     * @param dimension 次元を表す変数
     * @return 生成した ArrayTypeInfo オブジェクト
     */
    public static ArrayTypeInfo getType(final TypeInfo element, final int dimension) {

        if (null == element) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        Key key = new Key(element, dimension);
        ArrayTypeInfo arrayType = ARRAY_TYPE_MAP.get(key);
        if (arrayType == null) {
            arrayType = new ArrayTypeInfo(element, dimension);
            ARRAY_TYPE_MAP.put(key, arrayType);
        }

        return arrayType;
    }

    /**
     * オブジェクトの初期化を行う．配列の要素の型と配列の次元が与えられなければならない
     * 
     * @param element 配列の要素
     * @param dimension 配列の事件
     */
    ArrayTypeInfo(final TypeInfo element, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == element) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.element = element;
        this.dimension = dimension;
    }

    /**
     * 配列の要素を保存する変数
     */
    private final TypeInfo element;

    /**
     * 配列の次元を保存する変数
     */
    private final int dimension;

    /**
     * ArrayTypeInfo オブジェクトを一元管理するための Map．オブジェクトはファクトリメソッドで生成される．
     */
    private static final Map<Key, ArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, ArrayTypeInfo>();

    /**
     * 変数の型と次元を用いてキーとなるクラス．
     * 
     * @author higo
     */
    static class Key {

        /**
         * 第一キー
         */
        private final TypeInfo element;

        /**
         * 第二キー
         */
        private final int dimension;

        /**
         * 第一，第二キーから，キーオブジェクトを生成する
         * 
         * @param element 第一キー
         * @param dimension 第二キー
         */
        Key(final TypeInfo element, final int dimension) {

            if (null == element) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.element = element;
            this.dimension = dimension;
        }

        /**
         * このオブジェクトのハッシュコードを返す．
         */
        @Override
        public int hashCode() {
            return this.element.hashCode() + this.dimension;
        }

        /**
         * このキーオブジェクトの第一キーを返す．
         * 
         * @return 第一キー
         */
        public String getFirstKey() {
            return this.element.getTypeName();
        }

        /**
         * このキーオブジェクトの第二キーを返す．
         * 
         * @return 第二キー
         */
        public int getSecondKey() {
            return this.dimension;
        }

        /**
         * このオブジェクトと引数で指定されたオブジェクトが等しいかを返す．
         */
        @Override
        public boolean equals(Object o) {

            if (null == o) {
                throw new NullPointerException();
            }

            if (!(o instanceof Key)) {
                return false;
            }

            final String firstKey = this.getFirstKey();
            final String correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            }

            final int secondKey = this.getSecondKey();
            final int correspondSecondKey = ((Key) o).getSecondKey();
            return secondKey == correspondSecondKey;
        }
    }
}
