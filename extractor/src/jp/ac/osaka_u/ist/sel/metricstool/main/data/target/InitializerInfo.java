package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;


/**
 *　イニシャライザの共通の親クラス
 *<br>
 *イニシャライザとは，スタティック・イニシャライザやインスタンス・イニシャライザ　などである 
 * @author g-yamada
 *
 */
@SuppressWarnings("serial")
public abstract class InitializerInfo extends CallableUnitInfo {

    public InitializerInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(Collections.<ModifierInfo> emptySet(), fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public final String getSignatureText() {
        return "";
    }

}
