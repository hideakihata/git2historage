package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * AstVisitStateManager の基本となる抽象クラス.
 * <p>
 * ビジターのASTノードへの到達状況に応じて状態の保存，復元を行う仕組みを提供する.
 * ASTは木構造であるため，そのビジターの到達状態は，あるノードの中に入る，あるノードの中から出る時に変化すると考えられる.
 * 特に，ノードの中に出入りする際には，中に入った時に変化した状態を，外に出る時に復元しなければならない.
 * そこで，このクラスは状態変化のトリガとなるようなASTノードに入る時にスタックに現在の状態を保存しておき，
 * トリガとなるようなASTノードから出た時にスタックから過去の状態を取り出して状態を復元する仕組みを提供する.
 * 状態を復元するには，その時の状態を復元できるような情報を記録しなければならないが，どのような情報を記録しなければならないかは，
 * サブクラスによって異なると考えられる.
 * そこで，型パラメータTを用いて任意に情報を保持する型を指定することで,状態の記録，復元時に参照する情報をサブクラス毎に指定することができるようにしている.
 * 
 * <p>
 * このクラスを継承するクラスは {@link #isStateChangeTriggerEvent(AstToken)},{@link #getState()},
 * {@link #setState(T)}の3つの抽象メソッドを実装する必要がある.
 * <p>
 * 
 * @author kou-tngt
 *
 * @param <T> 状態を復元するための情報を保持する型.
 */
public abstract class StackedAstVisitStateManager<T> implements AstVisitStateManager {

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#addStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void addStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.add(listener);
    }

    /**
     * ビジターがASTノードの中に入った時のイベント通知を受け取って，現在の状態をスタックに記録する.
     * 
     * @param event イベント
     */
    public void entered(final AstVisitEvent event) {
        if (this.isStateChangeTriggerEvent(event)) {
            this.pushState();
        }
    }

    /**
     * ビジターがASTノードの中から出た時のイベント通知を受け取って，スタックから過去の状態を取り出し復元する.
     * 
     * @param event イベント
     */
    public void exited(final AstVisitEvent event) {
        if (this.isStateChangeTriggerEvent(event)) {
            this.popState();
        }
    }

    /**
     * 登録済みの状態変化リスナーのセットを取得する
     * @return 登録済みの状態変化リスナーのセット
     */
    public Set<StateChangeListener<AstVisitEvent>> getListeners() {
        return Collections.unmodifiableSet(this.listeners);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#removeStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void removeStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.remove(listener);
    }

    /**
     * ASTのノードに到達したイベントを受け取る.
     * 到達しただけではビジターの状態変化は発生しないため特に何もしない.
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#visited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public final void visited(final AstVisitEvent event) {
        //何もしない
    }

    /**
     * 登録済みの状態変化リスナーに対して状態変化イベントを通知する.
     * 
     * @param type 状態変化の種類
     * @param triggerEvent 状態変化のトリガとなったAST到達イベント
     */
    protected final void fireStateChangeEvent(final StateChangeEventType type, final AstVisitEvent triggerEvent) {
        final StateChangeEvent<AstVisitEvent> event = new StateChangeEvent<AstVisitEvent>(this, type,
                triggerEvent);
        for (final StateChangeListener<AstVisitEvent> listener : this.getListeners()) {
            listener.stateChanged(event);
        }
    }

    /**
     * 状態をスタックから取り出して復元する.
     */
    private void popState() {
        this.setState(this.stateStack.pop());
    }

    /**
     * 現在の状態をスタックに入れる.
     */
    private void pushState() {
        this.stateStack.push(this.getState());
    }

    /**
     * 現在の状態の情報を返す.
     * @return 現在の状態の情報
     */
    protected T getState() {
        return this.state;
    }

    /**
     * 引数で与えられた情報を基に状態を復元する.
     * @param state 状態を復元するための情報
     */
    protected void setState(T state) {
        this.state = state;
    }

    /**
     * 引数で与えられたトークンが状態変化のトリガになり得るかどうかを返す.
     * 
     * @param token 状態変化のトリガとなり得るかどうかを調べるトークン
     * @return 状態変化のトリガになり得る場合はtrue
     */
    protected abstract boolean isStateChangeTriggerEvent(AstVisitEvent event);

    /**
     * 状態変化リスナのセット
     */
    private final Set<StateChangeListener<AstVisitEvent>> listeners = new LinkedHashSet<StateChangeListener<AstVisitEvent>>();

    /**
     * 状態を記録しておくスタック
     */
    private final Stack<T> stateStack = new Stack<T>();
    
    private T state;
}
