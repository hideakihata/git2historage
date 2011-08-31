package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import java.security.AccessControlException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.ConcurrentHashSet;


/**
 * 進捗状況を報告する {@link ProgressReporter} とそれを受け取る {@link ProgressListener}の橋渡しをするクラス.
 * １つの {@link ProgressSource} 対してこのクラスのインスタンスが１つ作られる.
 * このクラスの１つのインスタンスに対して，複数のリスナーが登録できる.
 * <p>
 * {@link ProgressReporter}を実装するクラスは， {@link #getConnector(AbstractPlugin)}メソッドに
 * 報告する {@link ProgressSource} を渡すことで，このクラスのインスタンスを取得する.
 * 次に， {@link #connect(ProgressReporter)}メソッドに自分自身を渡すことで，
 * コネクションを確立する.
 * <p>
 *  {@link ProgressListener}を実装するクラスは，{@link #getConnector(AbstractPlugin)}メソッドに
 *  報告を受け取りたい {@link ProgressSource} を渡すことで，このクラスのインスタンスを取得し，
 *  次に， {@link #addProgressListener(ProgressListener)}メソッドに自身を渡すことで，
 *  コネクションを確立する.
 * <p>
 * 特別権限を持つスレッドは，このクラスのインスタンスに対して， {@link #disconnect()}メソッドを呼び出すことで，
 * コネクションを強制的に解除させることができる.
 * コネクションが解除されたことはリスナー側には即座に通知され， {@link ProgressSource} 側には次回以降の進捗報告時に 
 * {@link ProgressConnectionException}がスローされる
 * 
 * @author kou-tngt
 *
 */
public final class ProgressConnector {

    /**
     * ファクトリメソッド.
     * 引数に {@link ProgressSource} を与えることで，そのソースからの進捗報告を橋渡しするコネクタを作成
     * @param source 進捗情報を送るソース
     * @return pluginインスタンスからの進捗報告を橋渡しするコネクタ
     */
    public static synchronized ProgressConnector getConnector(final ProgressSource source) {
        if (connectionsMap.containsKey(source)) {
            //マップにインスタンスが登録されていたので，そのまま返す
            return connectionsMap.get(source);
        } else {
            //なかったので新しく作って登録して返す.
            final ProgressConnector connection = new ProgressConnector(source);
            connectionsMap.put(source, connection);
            return connection;
        }
    }

    /**
     * 進捗報告を受け取るリスナーを登録する
     * @param listener 進捗報告を受け取るリスナー
     * @throws NullPointerException　listnerがnullの場合
     */
    public final void addProgressListener(final ProgressListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }
        this.listeners.add(listener);
    }

    /**
     * コネクションを強制的に解除するメソッド
     * @throws AccessControlException このメソッドを呼び出したスレッドが特別権限を持たない場合
     */
    public final synchronized void disconnect() {
        //アクセス権チェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        //解除済みフラグを立てて，プラグインからのレポーターをnullにする
        this.connectionState = STATE.DISCONNECTED;
        this.reporter = null;

        //リスナーに通知してから全削除
        for (final ProgressListener listener : this.listeners) {
            listener.disconnected(new ProgressEvent(this.source, -1));
        }
        this.listeners.clear();
    }

    /**
     * このコネクタに現在登録されているリスナを返す
     * @return
     */
    public final Set<ProgressListener> getListeners() {
        return Collections.unmodifiableSet(this.listeners);
    }

    /**
     * このコネクタのソースを返す
     * @return ソース
     */
    public final ProgressSource getSource() {
        return this.source;
    }

    /**
     * リスナーを削除する
     * @param listener　削除するリスナー
     */
    public final void removeProgressListener(final ProgressListener listener) {
        if (null != listener) {
            this.listeners.remove(listener);
        }
    }

    /**
     * 引数に与えられた{@link ProgressSource}のレポーターとの接続を確立する
     * @param reporter 接続するレポーター
     * @throws AlreadyConnectedException 別のreporterとの接続が確立されている時に，同じソースからの別のリポーターが接続してきた場合
     * @throws NullPointerException　reporterがnullの場合
     */
    synchronized void connect(final ProgressReporter reporter) throws AlreadyConnectedException {
        if (null == reporter) {
            throw new NullPointerException("reporter is null.");
        }

        if (null != this.reporter) {
            //他のリポーターとの接続が確立中
            throw new AlreadyConnectedException("New progress connection was refused.");
        }

        this.reporter = reporter;
        this.connectionState = STATE.CONNECTED;
    }

    /**
     * 進捗情報の送信を終了するメソッド
     */
    final synchronized void progressEnd() {
        this.reporter = null;
        this.connectionState = STATE.DISCONNECTED;
        this.fireProgressEnd(new ProgressEvent(this.source, 100));
    }

    /**
     * 進捗情報を報告する
     * 
     * このパッケージ以外からは呼び出せない.
     * このメソッドを呼び出す時は，引数の正しさは事前にチェックしておかなければならない.
     * 
     * @param percentage 進捗情報（%）
     * @throws DisconnectedException コネクションが切断されている場合
     * @throws ConnectionException コネクションが確立されていない場合
     */
    void reportProgress(final int percentage) throws DisconnectedException, ConnectionException {
        if (STATE.INIT == this.connectionState) {
            throw new ConnectionException("No Connection was created.");
        } else if (STATE.DISCONNECTED == this.connectionState) {
            throw new DisconnectedException("Already disconnected.");
        }

        //コネクション管理の本質とは外れるので，例外ではなくアサーションで引数の正しさをチェック
        //呼び出し元で引数チェック＆例外投げをしておくべき
        assert (0 <= percentage && 100 >= percentage) : "Illegal parameter : percentage was "
                + percentage;

        if (STATE.CONNECTED == this.connectionState) {
            //接続中なのでイベントを作ってリスナに投げる
            this.fireProgress(new ProgressEvent(this.source, percentage));
        }
    }

    /**
     * リスナに進捗情報を通知するメソッド
     * @param event　通知するイベント
     */
    private void fireProgress(final ProgressEvent event) {
        if (null == event) {
            throw new NullPointerException("event is null.");
        }

        synchronized (this) {
            for (final ProgressListener listener : this.listeners) {
                listener.updataProgress(event);
            }
        }
    }

    /**
     * リスナに進捗情報の終了を通知するメソッド
     * @param event　通知するイベント
     */
    private void fireProgressEnd(final ProgressEvent event) {
        if (null == event) {
            throw new NullPointerException("event is null.");
        }

        synchronized (this) {
            for (final ProgressListener listener : this.listeners) {
                listener.progressEnd(event);
            }
        }
    }

    /**
     * private コンストラクタ.
     * 引数に{@link ProgressSource}を取る.
     * @param source 進捗情報のソース
     */
    private ProgressConnector(final ProgressSource source) {
        this.source = source;
    }

    /**
     * 接続状態を表す
     * 状態遷移は INIT -> CONNECTED -> DISCONNECTED -> CONNECTED -> ...
     * @author kou-tngt
     */
    private static enum STATE {
        INIT, CONNECTED, DISCONNECTED
    };

    /**
     * このクラスのインスタンスを管理するMap
     */
    private static final Map<ProgressSource, ProgressConnector> connectionsMap = new HashMap<ProgressSource, ProgressConnector>();

    /**
     * リスナーを管理するSet.
     * ここにだけ参照があっても意味が無いので，弱参照で持つ.
     */
    private final Set<ProgressListener> listeners = new ConcurrentHashSet<ProgressListener>();

    /**
     * このインスタンスの接続状態
     */
    private STATE connectionState = STATE.INIT;

    /**
     * このインスタンスが接続するソース
     */
    private final ProgressSource source;

    /**
     * このインスタンスに直接進捗報告をしてくるリポーター
     */
    private ProgressReporter reporter;

}
