package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * 外部メソッドの引数情報を保存するためのクラス
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class ExternalParameterInfo extends ParameterInfo {

    /**
     * 引数で与えられたExpressionInfoの List から，引数の型の List を作成し，返す
     * 
     * @param expressions エンティティのList
     * @param ownerMethod 引数を宣言しているメソッド
     * @return 引数の型の List
     */
    public static List<ParameterInfo> createParameters(final List<ExpressionInfo> expressions,
            final ExternalMethodInfo ownerMethod) {

        if (null == expressions || null == ownerMethod) {
            throw new NullPointerException();
        }

        final List<ParameterInfo> parameters = new LinkedList<ParameterInfo>();
        for (final ExpressionInfo expression : expressions) {
            final TypeInfo type = expression.getType();
            final ExternalParameterInfo parameter = new ExternalParameterInfo(type, ownerMethod);
            parameters.add(parameter);
        }

        return Collections.unmodifiableList(parameters);
    }

    /**
     * 引数で与えられたExpressionInfoの List から，引数の型の List を作成し，返す
     * 
     * @param expressions エンティティのList
     * @param ownerConstructor 引数を宣言しているコンストラクタ
     * @return 引数の型の List
     */
    public static List<ParameterInfo> createParameters(final List<ExpressionInfo> expressions,
            final ExternalConstructorInfo ownerConstructor) {

        if (null == expressions || null == ownerConstructor) {
            throw new NullPointerException();
        }

        final List<ParameterInfo> parameters = new LinkedList<ParameterInfo>();
        for (final ExpressionInfo expression : expressions) {
            final TypeInfo type = expression.getType();
            final ExternalParameterInfo parameter = new ExternalParameterInfo(type,
                    ownerConstructor);
            parameters.add(parameter);
        }

        return Collections.unmodifiableList(parameters);
    }

    /**
     * 引数の型を指定してオブジェクトを初期化．外部定義のメソッド名なので引数名は不明．
     * 
     * @param type 引数の型
     * @param definitionMethod 宣言しているメソッド
     */
    public ExternalParameterInfo(final TypeInfo type, final CallableUnitInfo definitionMethod) {
        super(new HashSet<ModifierInfo>(), UNKNOWN_NAME, type, definitionMethod, 0, 0, 0, 0);
    }

    /**
     * 不明な引数名を表す定数
     */
    public final static String UNKNOWN_NAME = "unknown";
}
