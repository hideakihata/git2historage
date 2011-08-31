package jp.ac.osaka_u.ist.sel.metricstool.main.util;


/**
 * 言語のフィルタ.
 * 
 * このインターフェースのインスタンスは
 * {@link LanguageUtil#filterLanguages(LanguageFilter)} に渡すことができる.
 * 
 * @author rniitani
 */
public interface LanguageFilter {
    /**
     * 指定された言語が言語リストに含まれる必要があるかどうかを判定する.
     * 
     * @param language テスト対象の言語
     * @return language が含まれる必要がある場合 true
     */
    boolean accept(LANGUAGE language);
}
