package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;




/**
 * 任意の構造のASTを訪問するビジターのインタフェース.
 * <p>
 * このインタフェースを実装するクラスは，ASTノードを順番に訪問し，
 * 各ノードに到達した時，そのノードの内部に入る時，そのノードの内部から出る時に，
 * 登録された {@link AstVisitListener} に対して適切なイベントを発行する.
 * 
 * @author kou-tngt
 *
 * @param <T>　訪問するASTノードの型
 */
public interface AstVisitor<T> {

    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを登録する.
     * 
     * @param listener 登録するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void addVisitListener(AstVisitListener listener);

    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを削除する.
     * 
     * @param listener　削除するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void removeVisitListener(AstVisitListener listener);

    /**
     * このビジターの状態を初期状態に戻す.
     * イベントリスナは削除されない.
     */
    public void reset();

    /**
     * ビジターの訪問を開始する
     * @param startNode　最初に訪問するASTノード
     */
    public void startVisiting(T startNode) throws ASTParseException;
}
