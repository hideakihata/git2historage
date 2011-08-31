package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * @author higo
 *
 */
public interface StatementInfo extends ExecutableElementInfo {

    /**
     * 文のテキスト表現(String型)を返す
     * 
     * @return　文のテキスト表現(String型)を返す
     */
    String getText();

    /**
     * 文を直接所有する空間を返す
     * @return 文を直接所有する空間
     */
    LocalSpaceInfo getOwnerSpace();

    /**
     * 文を所有するメソッドまたはコンストラクタを返す
     * 
     * @return 文を所有するメソッドまたはコンストラクタ
     */
    CallableUnitInfo getOwnerMethod();
}
