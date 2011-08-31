package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class ConstructorStateManager extends CallableUnitStateManager {

    /**
     * 引数のイベントがコンストラクタ定義部を表すかどうかを返す．
     * token.isConstructorDefinition()メソッドを用いて判定する．
     * 
     * @param event　コンストラクタ定義部を表すかどうかを調べたいASTイベント
     * @return コンストラクタ定義部を表すトークンであればtrue
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isConstructorDefinition();
    }

}
