package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * {@link LANGUAGE} のユーティリティクラス.
 * 
 * @author rniitani
 */
public class LanguageUtil {

    /**
     * 全ての言語から filter によってマッチした言語のみを返す.
     * 
     * {@link AbstractPlugin#getMeasurableLanguages()} などで使用する.
     * 
     * @param filter 言語のフィルタ
     * @return フィルタされた言語の配列
     */
    public static LANGUAGE[] filterLanguages(LanguageFilter filter) {
        final LANGUAGE[] allLanguages = LANGUAGE.values();
        final Set<LANGUAGE> resultSet = new LinkedHashSet<LANGUAGE>();

        for (final LANGUAGE language : allLanguages) {
            if (filter.accept(language)) {
                resultSet.add(language);
            }
        }

        final LANGUAGE[] resultArray = new LANGUAGE[resultSet.size()];
        return resultSet.toArray(resultArray);
    }


    /**
     * オブジェクト指向な言語のみを取得.
     * 
     * @return オブジェクト指向な言語の配列
     */
    public static LANGUAGE[] getObjectOrientedLanguages() {
        return filterLanguages(new LanguageFilter() {
            public boolean accept(LANGUAGE language) {
                return language.isObjectOrientedLanguage();
            }
        });
    }
}
