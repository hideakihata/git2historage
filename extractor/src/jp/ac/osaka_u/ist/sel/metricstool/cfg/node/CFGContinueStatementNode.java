package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;


/**
 * continue文を表すCFGノード
 * 
 * @author higo
 * 
 */
public class CFGContinueStatementNode extends CFGJumpStatementNode {

    /**
     * ノードを生成するcontinue文を与えて初期化
     * 
     * @param continueStatement
     */
    CFGContinueStatementNode(final ContinueStatementInfo continueStatement) {
        super(continueStatement);
    }
}
