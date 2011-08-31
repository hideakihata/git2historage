package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;


public class JavaArrayInstantiationBuilder extends ExpressionBuilder {

    public JavaArrayInstantiationBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        if (token.equals(JavaAstToken.ARRAY_INSTANTIATION)) {
            final ExpressionElement[] elements = this.getAvailableElements();
            final List<ExpressionElement> indexExpressions = new ArrayList<ExpressionElement>();
            for (final ExpressionElement element : elements) {
                if (element instanceof JavaArrayInstantiationElement) {
                    pushElement(element);
                } else {
                    indexExpressions.add(element);
                }
            }

            assert 1 >= indexExpressions.size();

            final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression = indexExpressions
                    .size() != 0 ? indexExpressions.get(0).getUsage()
                    : new UnresolvedEmptyExpressionInfo(this.buildDataManager.getCurrentUnit(),
                            event.getStartLine(), event.getStartColumn() + 1, event.getStartLine(),
                            event.getStartColumn() + 1);
            JavaArrayInstantiationElement array = new JavaArrayInstantiationElement(indexExpression);

            pushElement(array);
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.equals(JavaAstToken.ARRAY_INSTANTIATION);
    }
}
