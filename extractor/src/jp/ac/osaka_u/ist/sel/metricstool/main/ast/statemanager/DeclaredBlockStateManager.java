package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ビジターがクラスやメソッド定義部などのブロック付き定義部に到達した時のビジターの状態を管理する抽象クラス.
 * <p>
 * クラス定義部、メソッド定義部、名前空間定義部などの状態管理に利用される.
 * ブロック無しの場合やブロック内でさらに入れ子に宣言部が現れるような構造も扱える.
 * <p>
 * 状態遷移パターン
 * パターン1: 定義のみの場合
 * INIT --（定義ノードの中に入る）--> DEFINITION --（定義ノードから出る）--> INIT
 * パターン2: 定義の後に関連するブロックが続く場合
 * INIT --（定義ノードの中に入る）--> DEFINITION --（ブロックに入る）--> BLOCK --（ブロックから出る）--> DEFINITION --（定義ノードから出る）--> INIT
 * パターン3: 入れ子になる場合
 * INIT --（定義ノードの中に入る）--> DEFINITION --（ブロックに入る）--> BLOCK --（定義ノードの中に入る）--> DEFINITION --（ブロックに入る）-->
 * BLOCK --> ... --（ブロックから出る）--> DEFINITION --（定義ノードから出る）--> BLOCK --（ブロックから出る）--> DEFINITION --（定義ノードから出る）--> INIT
 * <p>
 * このクラスのサブクラスは5つの抽象メソッド {@link #getBlockEnterEventType()}, {@link #getBlockExitEventType()}
 * {@link #getDefinitionEnterEventType()}, {@link #getDefinitionExitEventType()}, {@link #isDefinitionEvent(AstToken)}
 * を実装しなければならない．
 * また必要に応じて， {@link #isBlockToken(AstToken)}を任意にオーバーライドする必要がある．
 * 
 * @author kou-tngt
 */
public abstract class DeclaredBlockStateManager extends
        StackedAstVisitStateManager<DeclaredBlockStateManager.DeclaredBlockState> {

    public DeclaredBlockStateManager() {
        this.setState(STATE.OUT);
    }

    /**
     * ビジターがASTノードの中に入った時のイベント通知を受け取り，
     * そのノードが定義部やその後に続くブロックを表すものならば状態を遷移して状態変化イベントを発行する．
     * 
     * どのノードが定義部やブロックを表すかは， {@link #isDefinitionEvent(AstToken)}と {@link #isBlockToken(AstToken)}
     * をオーバーライドして指定する．
     * 
     * また，定義部やブロックに入った時に発行する状態変化イベントの種類は， {@link #getDefinitionEnterEventType()}と
     * {@link #getBlockEnterEventType()}をオーバーライドして指定する．
     * 
     * @param event ASTビジットイベント
     */
    @Override
    public void entered(final AstVisitEvent event) {

        if (this.isStateChangeTriggerEvent(event)) {
            //状態変化トリガなら

            //状態をスタックへ記録
            super.entered(event);

            fireStateChangeEnterEvent(event);
        }
    }

    /**
     * 状態変化トリガであるASTノードの中に入ったときの処理を行う．
     * イベントトリガとブロックの解析状態に応じた状態遷移イベントを発行する
     * @param event ASTビジットイベント
     * @return 何らかのイベントが発行された場合true，何もイベントが発行されなかった場合false;
     */
    protected boolean fireStateChangeEnterEvent(final AstVisitEvent event) {
        if (this.isDefinitionEvent(event)) {
            //定義ノードなら状態遷移してイベントを発行
            this.setState(STATE.DECLARE);
            this.fireStateChangeEvent(this.getDefinitionEnterEventType(), event);
        } else if (this.isBlockToken(event.getToken()) && STATE.DECLARE == this.getState()) {
            //定義部にいる状態でブロックを表すノードが来れば状態遷移してイベントを発行
            this.setState(STATE.BLOCK);
            this.fireStateChangeEvent(this.getBlockEnterEventType(), event);
        } else {
            return false;
        }
        return true;
    }

    /**
     * ビジターがASTノードから出た時のイベント通知を受け取り，
     * そのノードが定義部やその後に続くブロックを表すものならば状態を戻して状態変化イベントを発行する．
     * 
     *　どのノードが定義部やブロックを表すかは， {@link #isDefinitionEvent(AstToken)}と {@link #isBlockToken(AstToken)}
     * をオーバーライドして指定する．
     * 
     * また，定義部やブロックから出た時に発行する状態変化イベントの種類は， {@link #getDefinitionExitEventType()}と
     * {@link #getBlockExitEventType()}をオーバーライドして指定する．
     * 
     * @param event ASTビジットイベント
     */
    @Override
    public void exited(final AstVisitEvent event) {

        if (this.isStateChangeTriggerEvent(event)) {
            //状態変化トリガなら

            //スタックの一番上の状態に戻す
            super.exited(event);

        }
        fireStateChangeExitEvent(event);
    }

    /**
     * 状態変化トリガであるASTノードの中から出たの処理を行う．
     * イベントトリガとブロックの解析状態に応じた状態遷移イベントを発行する
     * @param event ASTビジットイベント
     * @return 何らかのイベントが発行された場合true，何もイベントが発行されなかった場合false;
     */
    protected boolean fireStateChangeExitEvent(final AstVisitEvent event) {
        if (this.isDefinitionEvent(event)) {
            //定義ノードならイベントを発行
            this.fireStateChangeEvent(this.getDefinitionExitEventType(), event);
        } else if (this.isBlockToken(event.getToken()) && STATE.DECLARE == this.getState()) {
            //定義部にいる状態でブロックを表すノードが来ればイベントを発行
            this.fireStateChangeEvent(this.getBlockExitEventType(), event);
        } else {
            return false;
        }
        return true;
    }

    /**
     * ブロックに入った時に発行する状態変化イベントのイベントタイプを返す抽象メソッド．
     * このメソッドをオーバーライドすることで，イベントタイプを任意に設定することができる．
     * @return ブロックに入った時に発行する状態変化イベントのイベントタイプ
     */
    protected abstract StateChangeEventType getBlockEnterEventType();

    /**
     * ブロックにから出た時に発行する状態変化イベントのイベントタイプを返す抽象メソッド．
     * このメソッドをオーバーライドすることで，イベントタイプを任意に設定することができる．
     * @return ブロックから出た時に発行する状態変化イベントのイベントタイプ
     */
    protected abstract StateChangeEventType getBlockExitEventType();

    /**
     * 定義部入った時に発行する状態変化イベントのイベントタイプを返す抽象メソッド．
     * このメソッドをオーバーライドすることで，イベントタイプを任意に設定することができる．
     * @return 定義部入った時に発行する状態変化イベントのイベントタイプ
     */
    protected abstract StateChangeEventType getDefinitionEnterEventType();

    /**
     * 定義部から出た時に発行する状態変化イベントのイベントタイプを返す抽象メソッド．
     * このメソッドをオーバーライドすることで，イベントタイプを任意に設定することができる．
     * @return 定義部から出た時に発行する状態変化イベントのイベントタイプ
     */
    protected abstract StateChangeEventType getDefinitionExitEventType();

    /**
     * 引数のイベントが対応する定義部を表すかどうかを返す抽象メソッド．
     * このメソッドをオーバーライドすることで，任意の定義部に対応するサブクラスを作成することができる．
     * 
     * @param event 定義部を表すかどうかを調べたいASTイベント
     * @return 定義部を表すトークンであればtrue
     */
    protected abstract boolean isDefinitionEvent(AstVisitEvent event);

    /**
     * 引数のトークンが対応するブロックを表すかどうかを返す．
     * デフォルトではtoken.isBlock()を用いて判定する．
     * このメソッドをオーバーライドすることで，任意のブロック対応するサブクラスを作成することができる．
     * 
     * @param token ブロックを表すかどうかを調べたいASTトークン
     * @return ブロックを表すトークンであればtrue
     */
    protected boolean isBlockToken(final AstToken token) {
        return token.isBlock();
    }

    /**
     * ビジターが現在定義ブロック内にいるかどうかを返す．
     * @return ビジターが現在定義ブロック内にいる場合はtrue
     */
    public boolean isInBlock() {
        return STATE.BLOCK == this.getState();
    }

    /**
     * ビジターが現在定義部か定義ブロックにいるかどうかを返す．
     * @return ビジターが現在定義部または定義ブロックにいる場合はtrue
     */
    public boolean isInDefinition() {
        return STATE.DECLARE == this.getState() || this.isInBlock();
    }

    /**
     * ビジターが現在定義部にいるかどうかを返す．
     * @return　ビジターが現在定義部にいる場合はtrue
     */
    public boolean isInPreDeclaration() {
        return STATE.DECLARE == this.getState();
    }

    /**
     * 引数で与えられたイベントが状態変化のトリガになり得るかどうかを返す.
     * 引数eventが{@link #isBlockToken(AstToken)}または {@link #isDefinitionEvent(AstVisitEvent)}のどちらかを満たせばtrueを返す．
     * 
     * @param event 状態変化のトリガとなり得るかどうかを調べるトークン
     * @return 状態変化のトリガになり得る場合はtrue
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return this.isBlockToken(event.getToken()) || this.isDefinitionEvent(event);
    }

    public interface DeclaredBlockState {
    }

    /**
     * 状態を表すEnum
     * 
     * @author kou-tngt
     *
     */
    public static enum STATE implements DeclaredBlockState {
        OUT, DECLARE, BLOCK
    }

}
