package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGParameterInNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;


/**
 * 引数を表すPDGノード
 * 
 * @author higo
 * 
 */
public class PDGParameterInNode extends PDGDataNode<CFGParameterInNode> implements PDGDataInNode {

    /**
     * 引数で与えられたparameterからPDGParameterNodeを作成して返す
     * 
     * @param parameter
     * @return
     */
    public static PDGParameterInNode getInstance(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        final CFGParameterInNode cfgNode = CFGParameterInNode.getInstance(parameter);
        return new PDGParameterInNode(cfgNode);
    }

    /**
     * cfgノードを与えて初期化． このクラス内の getInstanceメソッドからのみ呼び出される
     * 
     * @param parameter
     */
    private PDGParameterInNode(final CFGParameterInNode node) {
        super(node);
    }
}
