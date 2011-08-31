package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列型を表すためのクラス．以下の情報を持つ．
 * <ul>
 * <li>未解決型 (UnresolvedTypeInfo)</li>
 * <li>次元 (int)</li>
 * </ul>
 * 
 * @author higo
 * @see UnresolvedTypeInfo
 */
public class UnresolvedArrayTypeInfo implements UnresolvedReferenceTypeInfo<ArrayTypeInfo> {

    /**
     * この未解決配列使用が解決済みかどうか返す
     * 
     * @return 解決済みの場合は true, そうでない場合は false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済み配列型を返す
     * 
     * @return 解決済み配列型
     * @throws NotResolvedException 未解決の場合にスローされる
     */
    @Override
    public final ArrayTypeInfo getResolved() {
        return this.resolvedInfo;
    }

    /**
     * 未解決配列型を解決し，解決済み配列型を返す．
     * 
     * @param usingClass 未解決配列型が存在するクラス
     * @param usingMethod 未解決配列型が存在するメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み配列型
     */
    @Override
    public ArrayTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedTypeInfo<?> unresolvedElementType = this.getElementType();
        final int dimension = this.getDimension();

        final TypeInfo elementType = unresolvedElementType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert elementType != null : "resolveEntityUsage returned null!";

        // 要素の型が不明のときは UnnownTypeInfo を返す
        if (elementType instanceof UnknownTypeInfo) {
            this.resolvedInfo = ArrayTypeInfo.getType(UnknownTypeInfo.getInstance(), dimension);
            return this.resolvedInfo;

            // 要素の型が解決できた場合はその配列型を作成し返す
        } else {
            this.resolvedInfo = ArrayTypeInfo.getType(elementType, dimension);
            return this.resolvedInfo;
        }
    }

    /**
     * 配列の要素の未解決型を返す
     * 
     * @return 配列の要素の未解決型
     */
    public final UnresolvedTypeInfo<? extends TypeInfo> getElementType() {
        return this.type;
    }

    /**
     * 配列の次元を返す
     * 
     * @return 配列の次元
     */
    public final int getDimension() {
        return this.dimension;
    }

    public final String getTypeName() {
        final StringBuilder text = new StringBuilder();
        text.append(this.getElementType().getTypeName());
        for (int i = 0; i < this.getDimension(); i++) {
            text.append("[]");
        }
        return text.toString();
    }

    /**
     * このインスタンスが表す配列の次元を1大きくした配列を表すインスタンスを返す．
     * 
     * @return このインスタンスが表す配列の次元を1大きくした配列
     */
    public final UnresolvedArrayTypeInfo getDimensionInclementedArrayType() {
        return getType(getElementType(), getDimension() + 1);
    }

    /**
     * UnresolvedArrayTypeInfo のインスタンスを返すためのファクトリメソッド．
     * 
     * @param type 未解決型を表す変数
     * @param dimension 次元を表す変数
     * @return 生成した UnresolvedArrayTypeInfo オブジェクト
     */
    public static UnresolvedArrayTypeInfo getType(
            final UnresolvedTypeInfo<? extends TypeInfo> type, final int dimension) {

        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        final Key key = new Key(type, dimension);
        UnresolvedArrayTypeInfo arrayUsage = ARRAY_TYPE_MAP.get(key);
        if (arrayUsage == null) {
            arrayUsage = new UnresolvedArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayUsage);
        }

        return arrayUsage;
    }

    /**
     * 未解決配列型オブジェクトの初期化を行う．配列の要素の未解決型と配列の次元が与えられなければならない
     * 
     * @param type 配列の要素の未解決型
     * @param dimension 配列の次元
     */
    UnresolvedArrayTypeInfo(final UnresolvedTypeInfo<?> type, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.type = type;
        this.dimension = dimension;
        this.resolvedInfo = null;
    }

    /**
     * 配列の要素の型を保存する変数
     */
    private final UnresolvedTypeInfo<?> type;

    /**
     * 配列の次元を保存する変数
     */
    private final int dimension;

    /**
     * 解決済み配列使用を保存するための変数
     */
    ArrayTypeInfo resolvedInfo;

    /**
     * UnresolvedArrayTypeInfo オブジェクトを一元管理するための Map．オブジェクトはファクトリメソッドで生成される．
     */
    private static final ConcurrentMap<Key, UnresolvedArrayTypeInfo> ARRAY_TYPE_MAP = new ConcurrentHashMap<Key, UnresolvedArrayTypeInfo>();

    /**
     * 変数の型と次元を用いてキーとなるクラス．
     * 
     * @author higo
     */
    static class Key {

        /**
         * 第一キー
         */
        private final UnresolvedTypeInfo<?> type;

        /**
         * 第二キー
         */
        private final int dimension;

        /**
         * 第一，第二キーから，キーオブジェクトを生成する
         * 
         * @param type 第一キー
         * @param dimension 第二キー
         */
        Key(final UnresolvedTypeInfo<?> type, final int dimension) {

            if (null == type) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.type = type;
            this.dimension = dimension;
        }

        /**
         * このオブジェクトのハッシュコードを返す．
         */
        @Override
        public int hashCode() {
            return this.type.hashCode() + this.dimension;
        }

        /**
         * このキーオブジェクトの第一キーを返す．
         * 
         * @return 第一キー
         */
        public UnresolvedTypeInfo<?> getFirstKey() {
            return this.type;
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

            final UnresolvedTypeInfo<?> firstKey = this.getFirstKey();
            final UnresolvedTypeInfo<?> correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            }

            final int secondKey = this.getSecondKey();
            final int correspondSecondKey = ((Key) o).getSecondKey();
            return secondKey == correspondSecondKey;
        }
    }
}
