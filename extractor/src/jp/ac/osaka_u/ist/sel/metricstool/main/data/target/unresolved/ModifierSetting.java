package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Modifier;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;


/**
 * 修飾子をセットするためのインターフェース
 * 
 * @author higo
 *
 */
public interface ModifierSetting extends Modifier {

    /**
     * 引数で与えられた修飾子を追加する
     * 
     * @param modifier 追加する修飾子
     */
    void addModifier(final ModifierInfo modifier);
}
