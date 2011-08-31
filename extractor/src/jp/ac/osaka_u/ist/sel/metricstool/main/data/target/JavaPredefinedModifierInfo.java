package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.HashMap;
import java.util.Map;

/**
 * publicやprivateなどJavaの言語で規定されている修飾子を扱うクラス
 * @author a-saitoh
 *
 */

@SuppressWarnings("serial")
public final class JavaPredefinedModifierInfo extends JavaModifierInfo {


    /**
     * abstract を表す定数
     */
    public static final String ABSTRACT_STRING = "abstract";

    /**
     * final を表す定数
     */
    public static final String FINAL_STRING = "final";

    /**
     * private を表す定数
     */
    public static final String PRIVATE_STRING = "private";

    /**
     * protected を表す定数
     */
    public static final String PROTECTED_STRING = "protected";

    /**
     * public を表す定数
     */
    public static final String PUBLIC_STRING = "public";

    /**
     * static を表す定数
     */
    public static final String STATIC_STRING = "static";

    /**
     * virtual を表す定数
     */
    public static final String VIRTUAL_STRING = "virtual";
    
    /**
     * synchronized を表す定数
     */
    public static final String SYNCHRONIZED_STRING = "synchronized";

    /**
     * abstract を表す定数
     */
    public static final JavaPredefinedModifierInfo ABSTRACT = new JavaPredefinedModifierInfo(ABSTRACT_STRING);

    /**
     * final を表す定数
     */
    public static final JavaPredefinedModifierInfo FINAL = new JavaPredefinedModifierInfo(FINAL_STRING);

    /**
     * private を表す定数
     */
    public static final JavaPredefinedModifierInfo PRIVATE = new JavaPredefinedModifierInfo(PRIVATE_STRING);

    /**
     * protected を表す定数
     */
    public static final JavaPredefinedModifierInfo PROTECTED = new JavaPredefinedModifierInfo(PROTECTED_STRING);

    /**
     * public を表す定数
     */
    public static final JavaPredefinedModifierInfo PUBLIC = new JavaPredefinedModifierInfo(PUBLIC_STRING);

    /**
     * static を表す定数
     */
    public static final JavaPredefinedModifierInfo STATIC = new JavaPredefinedModifierInfo(STATIC_STRING);

    /**
     * virtual を表す定数
     */
    public static final JavaPredefinedModifierInfo VIRTUAL = new JavaPredefinedModifierInfo(VIRTUAL_STRING);

    /**
     * synchronized を表す定数
     */
    public static final JavaPredefinedModifierInfo SYNCHRONIZED = new JavaPredefinedModifierInfo(SYNCHRONIZED_STRING);

    /**
     * 修飾子名から，修飾子オブジェクトを生成するファクトリメソッド
     * 
     * @param name 修飾子名
     * @return 修飾子オブジェクト
     */
    public static JavaPredefinedModifierInfo getModifierInfo(final String name) {

        JavaPredefinedModifierInfo requiredModifier = MODIFIERS.get(name);
        if (null == requiredModifier) {
            requiredModifier = new JavaPredefinedModifierInfo(name);
            MODIFIERS.put(name, requiredModifier);
        }

        return requiredModifier;
    }

    /**
     * 修飾子名を与えて，オブジェクトを初期化
     * 
     * @param name
     */
    private JavaPredefinedModifierInfo(final String name) {
        this.name = name;
    }


    /**
     * 生成した ModifierInfo オブジェクトを保存していくための定数
     */
    private static final Map<String, JavaPredefinedModifierInfo> MODIFIERS = new HashMap<String, JavaPredefinedModifierInfo>();

    static {
        MODIFIERS.put(ABSTRACT_STRING, ABSTRACT);
        MODIFIERS.put(FINAL_STRING, FINAL);
        MODIFIERS.put(PRIVATE_STRING, PRIVATE);
        MODIFIERS.put(PROTECTED_STRING, PROTECTED);
        MODIFIERS.put(PUBLIC_STRING, PUBLIC);
        MODIFIERS.put(STATIC_STRING, STATIC);
        MODIFIERS.put(VIRTUAL_STRING, VIRTUAL);
        MODIFIERS.put(SYNCHRONIZED_STRING, SYNCHRONIZED);
    }

}
