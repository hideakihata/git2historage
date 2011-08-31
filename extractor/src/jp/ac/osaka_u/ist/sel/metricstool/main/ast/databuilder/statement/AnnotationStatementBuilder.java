package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ConstantToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * アノテーションに与えられる引数を解析するビルダー
 * 現在アノテーションの引数は真面目に解析していない
 * 与えられた引数はすべてStringとして取得することにしている
 * @author a-saitoh
 *
 */
public class AnnotationStatementBuilder extends DataBuilderAdapter<String> {

    @Override
    public void entered(AstVisitEvent e) {
        if (isActive()) {

            //ANNOTATION_STRINGの後にくる文字列を取得
            if (!e.getToken().isAnnotationString()) {
                this.annotationArguments.append(e.getText());
            }
        }
    }

    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        //  do nothing
    }

    /**
     * 構築したStringデータを得る．
     * 真面目に解析するならばStackに格納する必要があるかも(アノテーションは入れ子にできるので)
     * @return
     */
    public String getArguments() {
        return this.annotationArguments.toString();
    }

    public void clearAnnotationArguments() {
        this.annotationArguments.delete(0, this.annotationArguments.length());
    }

    private final StringBuffer annotationArguments = new StringBuffer();

}
