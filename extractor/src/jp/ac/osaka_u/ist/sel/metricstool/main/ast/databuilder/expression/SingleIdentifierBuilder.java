package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassReferenceInfo;


public class SingleIdentifierBuilder extends ExpressionBuilder {

    public SingleIdentifierBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        if (token.isIdentifier()) {
            final int fromLine = event.getStartLine();
            final int fromColumn = event.getStartColumn();
            final int toLine = event.getEndLine();
            final int toColumn = event.getEndColumn();

            UnresolvedClassReferenceInfo currentClassReference = this.buildDataManager
                    .getCurrentClass().getClassReference(this.buildDataManager.getCurrentUnit(),
                            fromLine, fromColumn, toLine, toColumn);

            pushElement(new SingleIdentifierElement(token.toString(), currentClassReference,
                    fromLine, fromColumn, toLine, toColumn));
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isIdentifier();
    }
}
