package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ソフトウェアの単位がインスタンスなのかスタティックなのかを定義するインターフェース
 * 
 * @author higo
 */
public interface StaticOrInstance {

    /**
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    boolean isInstanceMember();

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    boolean isStaticMember();
}
