package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;


import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CommonASTWithLineNumber;
import antlr.collections.AST;


/**
 * antlrのASTを訪問する {@link AstVisitor}.
 * 
 * 
 * @author kou-tngt
 *
 */
public class AntlrAstVisitor implements AstVisitor<AST> {

    /**
     * 引数translatorで指定された {@link AstTokenTranslator} とデフォルトの {@link AstVisitStrategy}を
     * 設定するコンストラクタ.
     * このコンストラクタから生成されたデフォルトのAstVisitStrategyはクラスやメソッド内部のノードも訪問するようにビジターを誘導する.
     * 
     * @param translator　このビジターが使用するASTノードの翻訳機
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator) {
        this(translator, true, true);
    }

    /**
     * 引数translatorで指定された {@link AstTokenTranslator} とデフォルトの {@link AstVisitStrategy}を
     * 設定するコンストラクタ.
     * 
     * クラスやメソッド内部のノードを訪問するかどうかを引数intoClassとintoMethodで指定する.
     * 
     * @param translator　このビジターが使用するASTノードの翻訳機
     * @param intoClass クラスを表すASTの内部を訪問するかどうかを指定する.訪問する場合はtrue.
     * @param intoMethod　メソッドを表すASTの内部を訪問するかどうかを指定する.訪問する場合はtrue.
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator, final boolean intoClass,
            final boolean intoMethod) {
        this(translator, new AntlrAstVisitStrategy(intoClass, intoMethod));
    }

    /**
     * 引数で指定された {@link AstTokenTranslator} と {@link AstVisitStrategy}を
     * 設定するコンストラクタ.
     * 
     * @param translator　このビジターが使用するASTノードの翻訳機
     * @param strategy　このビジターの訪問先を誘導するAstVisitStrategyインスタンス
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator,
            final AstVisitStrategy<AST> strategy) {
        if (null == translator) {
            throw new NullPointerException("translator is null.");
        }
        if (null == strategy) {
            throw new NullPointerException("starategy is null.");
        }

        this.visitStrategy = strategy;
        this.translator = translator;
    }

    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを登録する.
     * 
     * @param listener 登録するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void addVisitListener(final AstVisitListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }

        this.listeners.add(listener);
    }

    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを削除する.
     * 
     * @param listener　削除するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void removeVisitListener(final AstVisitListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * このビジターの状態を初期状態に戻す.
     * イベントリスナは削除されない.
     */
    public void reset() {
        this.eventStack.clear();
        this.nodeStack.clear();
    }

    private void printAST(AST node, int nest){
        CommonASTWithLineNumber nextNode = (CommonASTWithLineNumber) node;
        while(null != nextNode){
            CommonASTWithLineNumber currentNode = nextNode;
            nextNode = (CommonASTWithLineNumber) nextNode.getNextSibling();
            AstToken token = this.translator.translate(currentNode);
            for(int i = 0; i < nest; i++){
                System.out.print("  ");
            }
            System.out.println(token.toString() + " (" + currentNode.getText() + ")" + " : " + "[" + currentNode.getFromLine() + ", " + currentNode.getFromColumn() + "]" + "[" + currentNode.getToLine() + ", " + currentNode.getToColumn() + "]");
            printAST(currentNode.getFirstChild(), nest + 1);
        }
    }
    
    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor#startVisiting(java.lang.Object)
     */
    public void startVisiting(final AST startNode) throws ASTParseException {
        AST nextNode = startNode;
        //printAST(startNode, 0);

        AstToken parentToken = null;
        while (null != nextNode) {
            //このノードのトークンからAstTokenに変換する
            final AstToken token = this.translator.translate(nextNode);

            //位置情報が利用できるなら取得する.
            int startLine = 0;
            int startColumn = 0;
            int endLine = 0;
            int endColumn = 0;
            if (nextNode instanceof CommonASTWithLineNumber) {
                CommonASTWithLineNumber node = (CommonASTWithLineNumber) nextNode;
                startLine = node.getFromLine();
                startColumn = node.getFromColumn();
                endLine = node.getToLine();
                endColumn = node.getToColumn();
            }
            
            //訪問イベントを作成
            final AstVisitEvent event = new AstVisitEvent(this, token, nextNode.getText(), parentToken, startLine, startColumn,
                    endLine, endColumn);

            this.fireVisitEvent(event);

            if (this.visitStrategy.needToVisitChildren(nextNode, event.getToken())) {
                //子ノードを訪問する場合

                this.fireEnterEvent(event);
                this.eventStack.push(event);
                this.nodeStack.push(nextNode);
                nextNode = nextNode.getFirstChild();
                
                //子ノードを訪問するので，現在のノードが親ノードになる
                parentToken = token;

            } else {
                //次の兄弟に進む場合
                nextNode = nextNode.getNextSibling();
            }

            if (null == nextNode) {
                //次の行き先がない

                AstVisitEvent exitedEvent = null;
                
                //まだスタックを遡ってまだ辿ってない兄弟を探す
                while (!this.nodeStack.isEmpty()
                        && null == (nextNode = this.nodeStack.pop().getNextSibling())) {
                    exitedEvent = this.eventStack.pop();
                    this.fireExitEvent(exitedEvent);
                }

                if (!this.eventStack.isEmpty()) {
                    exitedEvent = this.eventStack.pop();
                    this.fireExitEvent(exitedEvent);
                }
                
                if(null != exitedEvent) {
                    parentToken = exitedEvent.getParentToken();
                }
            }
        }
    }

    /**
     * 現在のノードの内部に入るイベントを発行する
     * @param event　発行するイベント
     */
    private void fireEnterEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.entered(event);
        }
    }

    /**
     * 現在のノードの内部から出るイベントを発行する
     * @param event　発行するイベント
     */
    private void fireExitEvent(final AstVisitEvent event) throws ASTParseException {
        for (final AstVisitListener listener : this.listeners) {
            listener.exited(event);
        }
    }

    /**
     * ノードに訪問するイベントを発行する
     * @param event　発行するイベント
     */
    private void fireVisitEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.visited(event);
        }
    }

    /**
     * このビジターの訪問先を誘導する.
     */
    private final AstVisitStrategy<AST> visitStrategy;

    /**
     * 訪問したASTノードをAstTokenに変換する
     */
    private final AstTokenTranslator<AST> translator;

    /**
     * イベントを管理するスタック
     */
    private final Stack<AstVisitEvent> eventStack = new Stack<AstVisitEvent>();

    /**
     * ノードを管理するスタック
     */
    private final Stack<AST> nodeStack = new Stack<AST>();

    /**
     * イベント通知を受け取るリスナーのセット
     */
    private final Set<AstVisitListener> listeners = new LinkedHashSet<AstVisitListener>();

}
