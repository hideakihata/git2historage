package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class JavaExpressionElementBuilder extends ExpressionBuilder {

    /**
     * @param expressionManager
     */
    public JavaExpressionElementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);

        if (null == buildDataManager) {
            throw new NullPointerException("buildManager is null.");
        }
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        if (token.equals(JavaAstToken.SUPER)) {
            pushElement(new JavaExpressionElement(false, true, event.getStartLine(), event
                    .getStartColumn(), event.getEndLine(), event.getEndColumn()));
        } else if (token.equals(JavaAstToken.CLASS)) {
            pushElement(new JavaExpressionElement(true, false, event.getStartLine(), event
                    .getStartColumn(), event.getEndLine(), event.getEndColumn()));
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.equals(JavaAstToken.SUPER) || token.equals(JavaAstToken.CLASS);
    }

}
