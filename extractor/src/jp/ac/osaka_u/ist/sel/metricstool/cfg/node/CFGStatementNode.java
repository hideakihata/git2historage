package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;


/**
 * CFGの文ノードを表すクラス
 * 
 * @author t-miyake
 * 
 */
public abstract class CFGStatementNode<T extends SingleStatementInfo> extends CFGNormalNode<T> {

    /**
     * 生成するノードに対応する文を与えて初期化
     * 
     * @param statement
     *            生成するノードに対応する文
     */
    CFGStatementNode(final T statement) {
        super(statement);
    }
}
