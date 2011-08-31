package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import java.util.EventListener;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;


/**
 * 抽象構文木のビジターから {@link AstVisitEvent} の通知を受け取るインタフェース.
 * 
 * 任意のノードaについて,{@link #visited(AstVisitEvent)},{@link #entered(AstVisitEvent)},{@link #exited(AstVisitEvent)}
 * の順番に通知される.
 * 木構造の葉であるノードについても， {@link #entered(AstVisitEvent)}と{@link #exited(AstVisitEvent)}メソッドが呼ばれる.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitListener extends EventListener {
    /**
     * ビジターがある頂点に到達した時にイベントを受け取る.
     * @param e 到達イベント
     */
    public void visited(AstVisitEvent e);

    /**
     * ビジターがある頂点の中に入る時にイベントを受け取る.
     * @param e
     */
    public void entered(AstVisitEvent e);

    /**
     * ビジターがある頂点の中から出た時にイベントを受け取る
     * @param e
     */
    public void exited(AstVisitEvent e) throws ASTParseException;
}
