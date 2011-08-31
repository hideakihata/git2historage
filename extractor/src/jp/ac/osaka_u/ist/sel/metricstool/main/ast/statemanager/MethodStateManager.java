package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class MethodStateManager extends CallableUnitStateManager {

    /**
     * 引数のイベントがメソッド定義部を表すかどうかを返す．
     * token.isMethodDefinition()メソッドを用いて判定する．
     * 
     * @param event メソッド定義部を表すかどうかを調べたいASTイベント
     * @return メソッド定義部を表すトークンであればtrue
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isMethodDefinition();
    }

}
