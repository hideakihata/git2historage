package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;

public class JavaArrayInstantiationElement extends ExpressionElement{

    
    public JavaArrayInstantiationElement(final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression) {
        if(null == indexExpression) {
            throw new IllegalArgumentException("indexExpression is null");
        }
        this.indexExpression = indexExpression;
    }
    
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getIndexExpression() {
        return indexExpression;
    }
    
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression;
}
