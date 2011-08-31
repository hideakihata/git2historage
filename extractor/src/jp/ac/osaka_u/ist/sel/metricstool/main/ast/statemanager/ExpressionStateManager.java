package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ビジターが式記述部に到達した時に状態遷移し，状態遷移イベントを通知する．
 * このクラスは主に式解析部のビルダーの有効無効を切り替えるために使用されることを想定している．
 * <p>
 * 式記述部中に式が継続しないツリー（Javaの匿名クラス宣言など）が存在する場合は，その内部は構成するものではないと判定し，
 * 式記述部から出た状態に遷移する．
 * そして，そのツリーの訪問が終了した後，もう一度式記述部に入った状態に遷移する．
 * 
 * @author kou-tngt
 *
 */
public class ExpressionStateManager extends
        StackedAstVisitStateManager<ExpressionStateManager.STATE> {

    public ExpressionStateManager() {
        this.setState(STATE.NOT);
    }
    
    /**
     * 通知する状態遷移イベントのイベントタイプを表すEnum
     * @author kou-tngt
     *
     */
    public static enum EXPR_STATE implements StateChangeEventType {
        ENTER_EXPR, EXIT_EXPR
    }

    /**
     * ビジターがASTノードの中に入った時のイベント通知を受け取り，
     * そのノードが式記述部や式が継続しないノードであれば，
     * 状態を遷移させた後イベントを発行する
     * 
     * @param event ASTビジットイベント
     */
    @Override
    public void entered(final AstVisitEvent event) {
        super.entered(event);

        final AstToken token = event.getToken();
        if (token.isExpression()) {
            this.setState(STATE.IN);
            this.fireStateChangeEvent(EXPR_STATE.ENTER_EXPR, event);
        } else if (this.isExpressionInsulator(token)) {
            this.setState(STATE.NOT);
            this.fireStateChangeEvent(EXPR_STATE.EXIT_EXPR, event);
        }
    }

    /**
     * ビジターがASTノードのから出た時のイベント通知を受け取り，
     * そのノードが式記述部や式が継続しないノードであれば，
     * 状態を戻した後イベントを発行する
     * 
     * @param event ASTビジットイベント
     */
    @Override
    public void exited(final AstVisitEvent event) {
        super.exited(event);

        final AstToken token = event.getToken();
        if (token.isExpression()) {
            this.fireStateChangeEvent(EXPR_STATE.EXIT_EXPR, event);
        } else if (this.isExpressionInsulator(token) && STATE.IN == this.getState()) {
            this.fireStateChangeEvent(EXPR_STATE.ENTER_EXPR, event);
        }
    }

    /**
     * 式の中にいるかどうかを返すメソッド
     * @return　式の中に居る場合はtrue
     */
    public boolean inExpression() {
        return STATE.IN == this.getState();
    }

    /**
     * 引数で与えられたトークンが式の継続しないノードかどうかを返す．
     * デフォルト実装では，token.isBlock()がtrueを返せばtrueを返す．
     * このメソッドをオーバーラードすることで，任意のノードで式を区切るような状態遷移をするクラスを作成することができる．
     * 
     * @param token 式の継続しないノードかどうかを返すトークン
     * @return 式の継続しないノードであればtrue
     */
    protected boolean isExpressionInsulator(final AstToken token) {
        return token.isBlock();
    }

    /**
     * 引数で与えられたトークンが状態変化のトリガになり得るかどうかを返す.
     * token.isExpression() または {@link #isExpressionInsulator(AstToken)}のどちらかを満たせば
     * trueを返す．
     * @param token 状態変化のトリガとなり得るかどうかを調べるトークン
     * @return token.isExpression() または {@link #isExpressionInsulator(AstToken)}のどちらかを満たす場合true
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        AstToken token = event.getToken();
        return token.isExpression() || this.isExpressionInsulator(token);
    }

    /**
     * 状態を表すEnum
     * @author kou-tngt
     *
     */
    protected static enum STATE {
        NOT, IN,
    }


}
