package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.*;


/**
 * アノテーションの情報を表すクラス
 * アノテーションは修飾子の一種として扱う
 * 現在はアノテーションに与えられた引数はすべて文字列(String)としてしか解析していない
 * @author a-saitoh
 *
 */

@SuppressWarnings("serial")
public class AnnotationInfo extends JavaModifierInfo {

    public AnnotationInfo(final String name, final String arguments) {
        this.name = name;
        this.annotationArgument = arguments;
    }

    public String getAnnotationArgument() {
        return annotationArgument;
    }

    private String annotationArgument;

    
}
