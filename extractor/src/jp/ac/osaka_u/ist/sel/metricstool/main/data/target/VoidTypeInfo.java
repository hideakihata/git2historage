package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * void 型を表すクラス．
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class VoidTypeInfo implements TypeInfo, UnresolvedTypeInfo<VoidTypeInfo> {

    /**
     * このクラスの単一オブジェクトを返す
     * 
     * @return このクラスの単一オブジェクト
     */
    public static VoidTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * void 型の名前を返す．
     */
    public String getTypeName() {
        return this.name;
    }

    /**
     * 等しいかどうかのチェックを行う
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        return typeInfo instanceof VoidTypeInfo;
    }

    /**
     * 名前解決されているかどうかを返す
     * 
     * @return 常に true を返す
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * 名前解決された情報を返す
     * 
     * @return 自分自身を返す
     */
    public VoidTypeInfo getResolved() {
        return this;
    }

    /**
     * 未解決void情報を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決引数情報の定義が行われているクラス
     * @param usingMethod 未解決引数情報の定義が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済みvoid情報
     */
    public VoidTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * void 型の型名を表す定数
     */
    public static final String VOID_STRING = "void";

    /**
     * 引数なしコンストラクタ
     */
    private VoidTypeInfo() {
        this.name = VOID_STRING;
    }

    /**
     * このクラスの単一オブジェクトを保存するための定数
     */
    private static final VoidTypeInfo SINGLETON = new VoidTypeInfo();

    /**
     * この型の名前を保存するための変数
     */
    private final String name;
}
