package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.IdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedJumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLabelInfo;


public abstract class JumpStatementBuilder<T extends UnresolvedJumpStatementInfo<? extends JumpStatementInfo>>
        extends SingleStatementBuilder<T> {

    public JumpStatementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);

        this.identifierBuilder = new IdentifierBuilder();
        this.addInnerBuilder(this.identifierBuilder);
    }

    @Override
    public void entered(AstVisitEvent e) {
        super.entered(e);
        if (this.isTriggerToken(e.getToken())) {
            this.identifierBuilder.clearBuiltData();
            this.identifierBuilder.activate();
        }
    }

    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        super.exited(e);
        if (this.isTriggerToken(e.getToken())) {
            this.identifierBuilder.deactivate();

            final T builtStatement = this.getLastBuildData();
            final String[] builtIdentifiers = this.identifierBuilder.getLastBuildData();
            final String labelName = null != builtIdentifiers && builtIdentifiers.length != 0 ? builtIdentifiers[0]
                    : null;

            if (null != labelName) {
                final UnresolvedLabelInfo label = this.buildDataManager
                        .getAvailableLabel(labelName);
                //assert null != label : "Illegal state: label was not found";
                builtStatement.setDestinationLabel(label);
            }
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isJump();
    }

    private final IdentifierBuilder identifierBuilder;

}
