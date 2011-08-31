package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;

/**
 * 抽象構文木のビジターがどのような状態にあるかを管理し，状態変化イベントを発行する.
 * このインタフェースを実装したクラス群からのイベントを受け取り，それらを組み合わせることでことで，
 * ビジターが現在ASTのどの部分に到達しているかを判断することができる.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitStateManager extends StateManager<AstVisitEvent>, AstVisitListener{

}
