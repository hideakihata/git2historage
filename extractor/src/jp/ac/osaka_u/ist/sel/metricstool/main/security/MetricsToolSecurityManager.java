package jp.ac.osaka_u.ist.sel.metricstool.main.security;


import java.security.AccessControlException;
import java.security.Permission;
import java.security.Permissions;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.WeakHashSet;


/**
 * メトリクスツールのアクセス制御をスレッド単位で動的に行うセキュリティマネージャ
 * <p>
 * 最初に {@link #getInstance()} を呼んだスレッドに全てのパーミッションを許可する特別権限を与える．
 * その後，特別権限を持っているスレッドが {@link #addPrivilegeThread(Thread)} メソッドを通じて登録したスレッドにも同様の特別権限を与える．
 * 特別権限を与えられたスレッドの特別権限は削除されない．
 * <p>
 * このクラスの利用者は特別権限を持たないスレッドからのアクセスを排除したい場合は {@link #checkAccess()} メソッドを呼ぶ．
 * 呼び出したスレッドが特別権限を持たない場合は， {@link AccessControlException}　例外がスローされる．
 * 特別権限を持つスレッドであった場合は，何もせずに処理を返す．
 * <p>
 * プラグインからのアクセスを排除したい場合は， {@link #checkPlugin()} メソッドを呼ぶ.
 * 呼び出したプラグインが登録されたプラグインスレッドと同じグループに属していれば， {@link AccessControlException}がスローされる.
 * プラグインスレッドと同じグループでなければ，何もせずに処理を返す.
 * <p>
 * また，別のアクセスコントロールとしてグローバルパーミッションという概念を扱う．
 * グローバルパーミッションとはプラグインやGUIを含むVM上の全てのクラスに許されるパーミッションのことで，
 * {@link #addGlobalPermission(Permission)}によって登録されたパーミッションは，
 * 全てのスレッド，全てのコンテキスト，全てのコードソースに許可される．
 * ただし，グローバルパーミッションの追加は特別権限スレッドのみから可能である．
 * <p>
 * 各スレッド単位の特定のパーミッションを許可することもできる.
 * {@link #addThreadPermission(Thread, Permission)}メソッドによって，任意のスレッドに任意のパーミッションを許可することができる.
 * ただし，このメソッドは特別権限を持つスレッドのみから呼び出すことができる.
 * <p>
 * 特別権限を持たないスレッドで，パーミッション登録されていないものについては，
 * 通常の {@link SecurityManager} と同等の機構を適用する．
 * <p>
 * シングルトンクラスであるため，コンストラクタは private であり，このクラスを継承することはできないが，
 * それを明示的に宣言するために final 修飾子をつけている．
 * 
 * @author kou-tngt
 *
 */
public final class MetricsToolSecurityManager extends SecurityManager {

    /**
     * シングルトンインスタンスを取得する
     * @return シングルトンインスタンス
     */
    public static MetricsToolSecurityManager getInstance() {
        if (null == SINGLETON) {
            synchronized (MetricsToolSecurityManager.class) {
                if (null == SINGLETON) {
                    SINGLETON = new MetricsToolSecurityManager();
                }
            }
        }
        return SINGLETON;
    }

    /**
     * メトリクスツールを実行しているVM上の全体で許可するパーミッションを追加する．
     * ロギングとかやりたい場合は，これを使って登録する．
     * 登録するには呼び出しスレッドに特別権限が必要
     * @param permission 許可したいパーミッションインスタンス
     * @throws AccessControlException スレッドに特別権限がない場合
     * @throws NullPointerException permissionがnullの場合
     */
    public final void addGlobalPermission(final Permission permission) {
        this.checkAccess();

        if (null == permission) {
            throw new NullPointerException("permission is null.");
        }

        this.globalPermissions.add(permission);
    }

    /**
     * プラグインスレッドを登録する.
     * 登録されたスレッドと同じスレッドグループに属するスレッドが，プラグインスレッドと判定される
     * @param thread 登録するプラグインスレッド
     * @throws NullPointerException　threadがnullの場合
     */
    public final void addPluginThread(final Thread thread) {
        if (null == thread) {
            throw new NullPointerException("group is null.");
        }

        this.pluginThreadGroups.add(thread.getThreadGroup());
    }

    /**
     * 引数 thread で与えられたスレッドに特別権限を付与する
     * @param thread 特別権限を付与したいスレッド
     * @throws AccessControlException 呼び出し側のスレッドが特別権限を持っていなかった場合
     * @throws NullPointerException threadがnullだった場合
     */
    public final void addPrivilegeThread(final Thread thread) {
        this.checkAccess();
        if (null == thread) {
            throw new NullPointerException("Added thread is null.");
        }

        this.privilegeThreads.add(thread);
    }

    /**
     * スレッド個別に許可するパーミッションを設定する.
     * このメソッドで設定されたパーミッションは，引数で与えられたスレッドのみで有効であり，
     * そのスレッドから作成された別のスレッドには譲渡されない.
     * @param thread パーミッションを許可するスレッド
     * @param permission 許可するパーミッション
     * @throws AccessControlException 呼び出し元のスレッドが特別権限を持たない場合
     * @throws NullPointerException permissionがnullの場合
     */
    public final void addThreadPermission(final Thread thread, final Permission permission) {
        this.checkAccess();
        if (null == thread) {
            throw new NullPointerException("thread is null.");
        }
        if (null == permission) {
            throw new NullPointerException("permission is null.");
        }

        Permissions permissions;
        if (this.threadPermissions.containsKey(thread)) {
            permissions = this.threadPermissions.get(thread);
        } else {
            permissions = new Permissions();
            this.threadPermissions.put(thread, permissions);
        }
        permissions.add(permission);
    }

    /**
     * 特別権限スレッドからの呼び出しかどうかを判定するメソッド．
     * 特別権限スレッド以外から呼び出されると， {@link AccessControlException}　をスローする．
     * @throws AccessControlException 特別権限スレッド以外から呼び出された場合
     */
    public final void checkAccess() {
        //カレントスレッドを取得
        final Thread currentThread = Thread.currentThread();
        if (!this.isPrivilegeThread(currentThread)) {
            //登録されていなかった

            //エラー表示用にスタックとレースの取得
            final StackTraceElement[] traces = currentThread.getStackTrace();

            //このメソッドの呼び出し元のメソッドを取得
            assert (null != traces && 3 < traces.length) : "Illegal state: empty stack trace.";
            final StackTraceElement callerMethod = traces[3];

            throw new AccessControlException(
                    "Permission denide: current thread can not invoke the method "
                            + callerMethod.getClassName() + "#" + callerMethod.getMethodName()
                            + ".");
        }
    }

    /**
     * プラグインスレッドからの呼び出しかどうかを判定するメソッド.
     * プラグインスレッドからの呼び出しであれば，{@link AccessControlException}　をスローする．
     * @throws AccessControlException プラグインスレッドから呼び出された場合
     */
    public final void checkPlugin() {
        //カレントスレッドを取得
        final Thread currentThread = Thread.currentThread();
        if (this.isPluginThread(currentThread)) {
            //プラグインスレッドだった

            //エラー表示用にスタックとレースの取得
            final StackTraceElement[] traces = currentThread.getStackTrace();

            //このメソッドの呼び出し元のメソッドを取得
            assert (null != traces && 3 < traces.length) : "Illegal state: empty stack trace.";
            final StackTraceElement callerMethod = traces[3];

            throw new AccessControlException(
                    "Permission denide: current thread can not invoke the method "
                            + callerMethod.getClassName() + "#" + callerMethod.getMethodName()
                            + ".");
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission)} メソッドをオーバーライドし，
     * 特別権限スレッドからの呼び出し，グローバルパーミッションとして登録済み，スレッド個別のパーミッションとして登録済みであれば，
     * パーミッションチェックをせずに終了する．
     * そうでないなら，親クラスのメソッドを呼び，パーミッションのチェックを行う．
     * @param perm チェックするパーミッション
     * @throws NullPointerException 引数permがnullの場合
     * @throws SecurityException パーミッションが許可されていない場合
     * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
     */
    @Override
    public final void checkPermission(final Permission perm) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }

        if (this.isPrivilegeThread()) {
            return;
        } else if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            final Thread current = Thread.currentThread();

            //このメソッド内から呼び出されて，自分が所属しているスレッドグループの親グループがnullかどうかを調べに来たスレッドだけ許可する
            if (perm.getName().equals("modifyThreadGroup")
                    && this.groupParentCheckingThread.contains(current)) {
                return;
            }

            //自分が所属しているスレッドグループの親グループがnullかどうかを調べにいく
            //nullならシステムスレッドである．
            this.groupParentCheckingThread.add(current);
            boolean isSystemThread = null == current.getThreadGroup().getParent();
            this.groupParentCheckingThread.remove(current);

            if (isSystemThread) {
                //システムスレッドの場合は全てを許可してしまう．
                return;
            } else if (this.threadPermissions.containsKey(current)) {
                final Permissions permissions = this.threadPermissions.get(current);
                if (permissions.implies(perm)) {
                    return;
                }
            }

            super.checkPermission(perm);
        }
    }

    /**
     * {@link SecurityManager#checkPermission(Permission, Object)} メソッドをオーバーライドし，
     * グローバルパーミッションとして登録済みであればパーミッションチェックをせずに終了する．
     * そうでないなら，親クラスのメソッドを呼び，パーミッションのチェックを行う．
     * @param perm チェックするパーミッション
     * @throws NullPointerException 引数permがnullの場合
     * @throws SecurityException permがグローバルパーミッションでない場合に，パーミッションが許可されていない場合
     * @see java.lang.SecurityManager#checkPermission(Permission, Object)
     */
    @Override
    public void checkPermission(final Permission perm, final Object context) {
        if (null == perm) {
            throw new NullPointerException("Permission is null.");
        }
        if (this.globalPermissions.implies(perm)) {
            return;
        } else {
            super.checkPermission(perm, context);
        }
    }

    /**
     * カレントスレッドがプラグインスレッドかどうかを返す
     * @return カレントスレッドがプラグインスレッドならtrue，そうでないならfalse
     */
    public final boolean isPluginThread() {
        return this.isPluginThread(Thread.currentThread());
    }

    /**
     * 引数のスレッドがプラグインスレッドかどうかを返す
     * @param thread 調べたいスレッド
     * @return プラグインスレッドならtrue，そうでないならfalse
     */
    public final boolean isPluginThread(final Thread thread) {
        return this.pluginThreadGroups.contains(thread.getThreadGroup());
    }

    /**
     * カレントスレッドが特別権限を持っているかを返す
     * @return 特別権限を持っていればtrue
     */
    public final boolean isPrivilegeThread() {
        return this.isPrivilegeThread(Thread.currentThread());
    }

    /**
     * 引数 thread で与えられたスレッドが特別権限を持っているかを返す
     * @param thread 特別権限を持っているかを調べたいスレッド
     * @return 引数 thread で与えられたスレッドが特別権限を持っていればtrue
     */
    public final boolean isPrivilegeThread(final Thread thread) {
        return this.privilegeThreads.contains(thread);
    }

    /**
     * プラグインのアクセス権限を解除する
     * @param plugin
     */
    public final void removePluginPermission(final AbstractPlugin plugin) {
        Thread current = Thread.currentThread();

        //許可されていたパーミッションを取ってくる.
        Permissions permissions;
        if (this.threadPermissions.containsKey(current)) {
            permissions = this.threadPermissions.get(current);
        } else {
            permissions = new Permissions();
            this.threadPermissions.put(current, permissions);
        }

        //新規パーミッションセットを作る
        Permissions newPermissions = new Permissions();

        //許可されていたパーミッションがプラグインのパーミッションセットに含まれて居なければ新規パーミッションセットに入れる
        for (Enumeration<Permission> enumerator = permissions.elements(); enumerator
                .hasMoreElements();) {
            Permission permission = enumerator.nextElement();
            boolean include = false;
            for (Enumeration<Permission> pluginPermissions = plugin.getPermissions().elements(); pluginPermissions
                    .hasMoreElements();) {
                Permission pluginPermission = pluginPermissions.nextElement();
                if (pluginPermission == permission) {//インスタンスの比較をする
                    include = true;
                    break;
                }
            }
            if (!include) {
                newPermissions.add(permission);
            }
        }
        //新規パーミッションセットをこのスレッドのパーミッションとしてセットする
        this.threadPermissions.put(current, newPermissions);
    }

    /**
     * プラグインのアクセス権限を設定する
     * @param plugin　プラグインインスタンス
     */
    public final void requestPluginPermission(final AbstractPlugin plugin) {
        Thread current = Thread.currentThread();

        Permissions permissions;
        if (this.threadPermissions.containsKey(current)) {
            permissions = this.threadPermissions.get(current);
        } else {
            permissions = new Permissions();
            this.threadPermissions.put(current, permissions);
        }

        Permissions pluginPermissions = plugin.getPermissions();

        for (Enumeration<Permission> enumeration = pluginPermissions.elements(); enumeration
                .hasMoreElements();) {
            permissions.add(enumeration.nextElement());
        }
    }

    /**
     * シングルトン用privateコンストラクタ．
     * ここを呼び出したスレッドを初期特別権限クラスとして登録する．
     */
    private MetricsToolSecurityManager() {
        final Thread thread = Thread.currentThread();
        assert (null != thread) : "Illegal state : current thread is null.";
        this.privilegeThreads.add(thread);
    }

    /**
     * 特別権限スレッドのセット．
     * 他の全ての参照が切れたら特別権限スレッドを持っていても意味がないので，弱参照にするために {@link WeakHashSet} を用いる．
     * また，マルチスレッド環境で適切に動作させるために {@link Collections#synchronizedSet(Set)} を使って同期させる．
     */
    private final Set<Thread> privilegeThreads = Collections
            .synchronizedSet((new WeakHashSet<Thread>()));

    /**
     * プラグインのスレッドグループのセット.
     * 
     */
    private final Set<ThreadGroup> pluginThreadGroups = Collections
            .synchronizedSet(new WeakHashSet<ThreadGroup>());

    /**
     * {@link #checkPermission(Permission)}メソッドにおいて利用される {@link ThreadGroup#getParent()} メソッドの呼び出しを
     * 実行したスレッドを一時的に保存しておくためのセット．
     */
    private final Set<Thread> groupParentCheckingThread = new WeakHashSet<Thread>();

    /**
     * シングルトンインスタンス．
     */
    private static MetricsToolSecurityManager SINGLETON;

    /**
     * VM全体で許可するパーミッション
     */
    private final Permissions globalPermissions = new Permissions();

    /**
     * 各スレッド毎に付与されるパーミッションのマップ
     */
    private final Map<Thread, Permissions> threadPermissions = new WeakHashMap<Thread, Permissions>();
}
