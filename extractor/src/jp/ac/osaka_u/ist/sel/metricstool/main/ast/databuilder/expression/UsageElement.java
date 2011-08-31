package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;

/**
 * @author t-miyake
 *
 */
public class UsageElement extends ExpressionElement {

    public UsageElement(final UnresolvedExpressionInfo<? extends ExpressionInfo> usage) {
        super(usage);
    }
    
    public boolean isMemberCall() {
        return this.usage instanceof UnresolvedCallInfo;
    }
    
    public boolean isVariableUsage() {
        return this.usage instanceof UnresolvedVariableUsageInfo;
    }

}
