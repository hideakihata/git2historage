package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ASTビジターの何らかの記述部への出入りを管理する抽象ステートマネージャ．
 * <p>
 * 記述部が再帰的な構造になっている場合にも対応しており，
 * ビジターがその記述部の何階層目にいるかという情報も管理する．
 * <p>
 * このクラスのサブクラスは， {@link #getEnterEventType()}, {@link #getExitEventType()}, {@link #isStateChangeTriggerEvent(AstToken)}
 * の3つの抽象メソッドを実装する必要がある
 * 
 * @author kou-tngt
 *
 */
public abstract class EnterExitStateManager extends StackedAstVisitStateManager {

    /**
     * ビジターがASTノードの中に入った時のイベント通知を受け取り，
     * それが状態変化のきっかけになるノードであれば，そのノード内に入るイベントを発行する．
     * 
     * どのようなノードが状態変化のトリガとなるかは {@link #isStateChangeTriggerEvent(AstToken)}メソッドによって決定される．
     * また，発行するイベントのタイプは {@link #getEnterEventType()}メソッドによって返されるものを用いる．
     * 
     * @param e ASTビジターのビジットイベント
     */
    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);

        if (this.isStateChangeTriggerEvent(e)) {
            this.enterDepthCount++;
            this.fireStateChangeEvent(this.getEnterEventType(), e);
        }
    }

    /**
     * ビジターがASTノードの中から出た時のイベント通知を受け取り，
     * それが状態変化のきっかけになるノードであれば，そのノードから出るイベントを発行する．
     * 
     * どのようなノードが状態変化のトリガとなるかは {@link #isStateChangeTriggerEvent(AstToken)}メソッドによって決定される．
     * また，発行するイベントのタイプは {@link #getEnterEventType()}メソッドによって返されるものを用いる．
     * 
     * @param e ASTビジターのビジットイベント
     */
    @Override
    public void exited(final AstVisitEvent e) {
        super.exited(e);

        if (this.isStateChangeTriggerEvent(e)) {
            this.enterDepthCount--;
            this.fireStateChangeEvent(this.getExitEventType(), e);
        }
    }

    /**
     * ビジターが対応する記述部の内部にいるかどうかを返す．
     * 
     * @return　ビジターが対応する記述部の中にいればtrue
     */
    public boolean isEntered() {
        return 0 < this.enterDepthCount;
    }

    /**
     * 対応する記述部の中に入った時に，それによる状態変化を通知するための状態変化イベントの種類を返す抽象メソッド．
     * サブクラスはこのメソッドをオーバーライドすることで，任意の種類の状態変化イベントを送信させることができる．
     * @return　対応する記述部の中に入った時に通知される状態変化イベントの種類
     */
    public abstract StateChangeEventType getEnterEventType();

    /**
     * 対応する記述部から出た時に，それによる状態変化を通知するための状態変化イベントの種類を返す抽象メソッド．
     * サブクラスはこのメソッドをオーバーライドすることで，任意の種類の状態変化イベントを送信させることができる．
     * @return　対応する記述部から出た時に通知される状態変化イベントの種類
     */
    public abstract StateChangeEventType getExitEventType();

    /**
     * 現在の状態の情報を返す.
     * この抽象クラスは状態を持たないためnullを返す．
     * @return null
     */
    @Override
    protected Object getState() {
        return null;
    }

    /**
     * ビジターが対応する記述部の何階層目にいるかを返す．
     * 
     * @return　ビジターが対応する記述部内にいなければ0，記述部内に居る場合はその階層を返す．
     */
    protected int getEnterDepthCount() {
        return this.enterDepthCount;
    }

    /**
     * 引数で与えられたイベントが対応する記述部へ出入りのトリガかどうかを返す抽象メソッド.
     * このメソッドをオーバーライドすることで，サブクラスがどの記述部への出入りに対応するかを指定することができる．
     * 
     * @param event 記述部へ出入りのトリガかどうかを調べるイベント
     * @return 記述部へ出入りのトリガの場合はtrue
     */
    @Override
    protected abstract boolean isStateChangeTriggerEvent(AstVisitEvent event);

    /**
     * 引数で与えられた情報を基に状態を復元する.
     * この抽象クラスは状態を持たないため何もしない．
     * @param state 状態を復元するための情報
     */
    @Override
    protected void setState(final Object state) {
        //何もしない
    }

    /**
     * 対応する記述部の現在の階層数
     */
    private int enterDepthCount;
}
