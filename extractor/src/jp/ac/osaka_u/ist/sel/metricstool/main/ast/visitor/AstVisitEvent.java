package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import java.util.EventObject;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * {@link AstVisitor} がASTの任意のノードに到達した時に発行されるイベント.
 * 
 * @author kou-tngt
 *
 */
public class AstVisitEvent extends EventObject {

    /**
     * このイベントが表すノードに関する情報を引数に取るコンストラクタ.
     * @param source　このイベントの発行元となるビジター
     * @param token 到達したノードの種類を表すトークン
     * @param text 到達したノードが表す文字列
     * @param enterLine 到達したノードが表す要素のソースコード上での開始行
     * @param enterColumn 到達したノードが表す要素のソースコード上の出開始列
     * @param exitLine 到達したノードが表す要素のソースコード上での終了行
     * @param exitColumn 到達したノードが表す要素のソースコード上での終了行
     */
    public AstVisitEvent(final AstVisitor source, final AstToken token, final String text, final AstToken parentToken,
            final int enterLine, final int enterColumn, final int exitLine, final int exitColumn) {
        super(source);
        this.source = source;
        this.token = token;
        this.text = text;
        this.parentToken = parentToken;
        this.startLine = enterLine;
        this.endLine = exitLine;
        this.startColumn = enterColumn;
        this.endColumn = exitColumn;
    }

    /**
     * このイベントの発行者であるビジターを返す.
     */
    @Override
    public AstVisitor getSource() {
        return this.source;
    }

    /**
     * このイベントが関連するASTノードの種類を表すトークンを返す.
     * 
     * @return このイベントが関連するASTノードの種類を表すトークン
     */
    public AstToken getToken() {
        return this.token;
    }
    
    /**
     * このイベントが関連するASTノードの文字列を返す．
     * 
     * @return このイベントが関連するASTノードの文字列
     */
    public final String getText() {
        return this.text;
    }
    
    /**
     * このイベントが関連するASTノードの親ノードの種類を表すトークンを返す．
     * @return このイベントが関連するASTノードの親ノードの種類を表すトークン
     */
    public AstToken getParentToken() {
        return this.parentToken;
    }

    /**
     * このイベントが関連するASTノードのソースコード上での開始列を返す.
     * 
     * @return　このイベントが関連するASTノードのソースコード上での開始列
     */
    public int getStartColumn() {
        return this.startColumn;
    }

    /**
     * このイベントが関連するASTノードのソースコード上での開始行を返す.
     * 
     * @return　このイベントが関連するASTノードのソースコード上での開始行
     */
    public int getStartLine() {
        return this.startLine;
    }

    /**
     * このイベントが関連するASTノードのソースコード上での終了列を返す.
     * 
     * @return　このイベントが関連するASTノードのソースコード上での終了列.
     */
    public int getEndColumn() {
        return this.endColumn;
    }

    /**
     * このイベントが関連するASTノードのソースコード上での終了行を返す.
     * 
     * @return　このイベントが関連するASTノードのソースコード上での終了行.
     */
    public int getEndLine() {
        return this.endLine;
    }

    /**
     * このイベントの発行者であるビジターを返す.
     */
    private final AstVisitor source;

    /**
     * このイベントが関連するASTノードの種類を表すトークン
     */
    private final AstToken token;
    
    /**
     * このイベントが関連するASTノードの文字列
     */
    private final String text;

    /**
     * このイベントが関連するASTノードの親ノードの種類を表すトークン
     */
    private final AstToken parentToken;

    /**
     * このイベントが関連するASTノードのソースコード上での開始行.
     */
    private final int startLine;

    /**
     * このイベントが関連するASTノードのソースコード上での開始列.
     */
    private final int startColumn;

    /**
     * このイベントが関連するASTノードのソースコード上での終了行.
     */
    private final int endLine;

    /**
     * このイベントが関連するASTノードのソースコード上での終了列.
     */
    private final int endColumn;

}
