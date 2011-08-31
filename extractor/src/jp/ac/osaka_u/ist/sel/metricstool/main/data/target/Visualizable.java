package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * クラスや，フィールド，メソッドの可視性を定義するインターフェース． 以下の可視性を定義する．
 * 
 * <ul>
 * <li>子クラスから参照可能</li>
 * <li>同じ名前空間内から参照可能</li>
 * <li>どこからでも参照可能</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public interface Visualizable {

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    boolean isNamespaceVisible();

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    boolean isInheritanceVisible();

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    boolean isPublicVisible();
}
