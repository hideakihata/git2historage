package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;


/**
 * Javaの式中に登場する型要素を構築するクラス．
 * 
 * 親クラスの {@link TypeElementBuilder#getTypeUpperBounds()} メソッドをオーバーライドし，
 * nullが帰ってきた場合はjava.lang.Objectを返すように拡張する．
 * 
 * @author kou-tngt
 *
 */
public class JavaTypeElementBuilder extends TypeElementBuilder {

    public JavaTypeElementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildManager) {
        super(expressionManager, buildManager);
    }

    @Override
    protected UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getDefaultTypeUpperBound() {
        return JavaTypeBuilder.JAVA_LANG_OBJECT;
    }
}
