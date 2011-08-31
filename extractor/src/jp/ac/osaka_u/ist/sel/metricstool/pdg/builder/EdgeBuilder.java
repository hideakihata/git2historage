package jp.ac.osaka_u.ist.sel.metricstool.pdg.builder;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public abstract class EdgeBuilder<T extends ExecutableElementInfo> {

    public static final EdgeBuilder<?> getBuilder(final ExecutableElementInfo element) {

        // ここからブロック文
        if (element instanceof ForBlockInfo) {
            return new ForBlockEdgeBuilder((ForBlockInfo) element);
        } else if (element instanceof ConditionalBlockInfo) {
            return new ConditionalBlockEdgeBuilder<ConditionalBlockInfo>(
                    (ConditionalBlockInfo) element);
        } else if (element instanceof BlockInfo) {
            return new BlockEdgeBuilder<BlockInfo>((BlockInfo) element);
        }

        // ここからSingleStatement
        else if (element instanceof VariableDeclarationStatementInfo) {
            final ExpressionInfo initializer = ((VariableDeclarationStatementInfo) element)
                    .getInitializationExpression();
            if (null != initializer) {
                return getBuilder(initializer);
            } else {
                return new EmptyEdgeBuilder(element);
            }

        } else if (element instanceof LabelInfo) {
            final StatementInfo statement = ((LabelInfo) element).getLabeledStatement();
            return getBuilder(statement);
        } else if (element instanceof AssertStatementInfo) {
            final ExpressionInfo expression = ((AssertStatementInfo) element)
                    .getAssertedExpression();
            return getBuilder(expression);
        } else if (element instanceof ExpressionStatementInfo) {
            final ExpressionInfo expression = ((ExpressionStatementInfo) element).getExpression();
            return getBuilder(expression);
        } else if (element instanceof JumpStatementInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof ReturnStatementInfo) {
            final ExpressionInfo expression = ((ReturnStatementInfo) element)
                    .getReturnedExpression();
            return getBuilder(expression);
        } else if (element instanceof ThrowStatementInfo) {
            final ExpressionInfo expression = ((ThrowStatementInfo) element).getThrownExpression();
            return getBuilder(expression);
        }

        // ここからExpression
        else if (element instanceof ArrayElementUsageInfo) {
            return new ArrayElementUsageEdgeBuilder((ArrayElementUsageInfo) element);
        } else if (element instanceof ArrayInitializerInfo) {
            return new ArrayInitializerEdgeBuilder((ArrayInitializerInfo) element);
        } else if (element instanceof ArrayTypeReferenceInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof BinominalOperationInfo) {
            return new BinominalOperationEdgeBuilder((BinominalOperationInfo) element);
        } else if (element instanceof CallInfo<?>) {
            // TODO interproceduralなエッジを作成
        } else if (element instanceof CastUsageInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof ClassReferenceInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof EmptyExpressionInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof ForeachConditionInfo) {
            final ExpressionInfo expression = ((ForeachConditionInfo) element)
                    .getIteratorExpression();
            return getBuilder(expression);
        } else if (element instanceof LiteralUsageInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof MonominalOperationInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof NullUsageInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof ParenthesesExpressionInfo) {
            final ExpressionInfo expression = ((ParenthesesExpressionInfo) element)
                    .getParnentheticExpression();
            return getBuilder(expression);
        } else if (element instanceof TernaryOperationInfo) {
            return new TernaryOperationEdgeBuilder((TernaryOperationInfo) element);
        } else if (element instanceof UnknownEntityUsageInfo) {
            return new EmptyEdgeBuilder(element);
        } else if (element instanceof VariableUsageInfo<?>) {
            return new EmptyEdgeBuilder(element);
        }

        throw new IllegalStateException("Here shouldn't be reached!");
    }

    EdgeBuilder(final T statement) {
        if (null == statement) {
            throw new IllegalArgumentException();
        }
        this.statement = statement;
    }

    public void addInterproceduralEdge() {
    }

    final T statement;
}
