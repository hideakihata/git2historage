package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.InstanceToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class InstanceElementBuilder extends ExpressionBuilder {

    public InstanceElementBuilder(final BuildDataManager buildDataManager,
            final ExpressionElementManager expressionManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();

        int fromLine = event.getStartLine();
        int fromColumn = event.getStartColumn();
        int toLine = event.getEndLine();
        int toColumn = event.getEndColumn();

        if (token.equals(InstanceToken.THIS)) {
            final InstanceSpecificElement thisInstance = InstanceSpecificElement
                    .getThisInstanceType(this.buildDataManager, fromLine, fromColumn, toLine,
                            toColumn);

            pushElement(thisInstance);
        } else if (token.equals(InstanceToken.NULL)) {
            pushElement(InstanceSpecificElement.getNullElement(
                    this.buildDataManager.getCurrentUnit(), fromLine, fromColumn, toLine, toColumn));
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.equals(InstanceToken.THIS) || token.equals(InstanceToken.NULL);
    }

}
