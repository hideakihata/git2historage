package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * 任意のタイミングで閉じることが出来るブロッキングキュー
 * @author kou-tngt
 *
 * @param <E>　キューの要素の型
 */
public class ClosableLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {
    /**
     * 
     */
    private static final long serialVersionUID = -4159234755316262135L;

    /**
     * キューに入れることができる要素数を返す.
     * {@link #close()}が呼ばれた後は常に0を返す.
     * @return キューに入れることができる要素数.
     * @see java.util.concurrent.LinkedBlockingQueue#remainingCapacity()
     */
    @Override
    public int remainingCapacity() {
        if (this.closed) {
            return 0;
        } else {
            return super.remainingCapacity();
        }
    }

    /**
     * キューに要素を追加する
     * {@link #close()}が呼ばれた後は常に失敗する.
     * @param element キューに入れる要素
     * @return キューに要素が追加できればture, 失敗すればfalse
     * @see java.util.AbstractQueue#add(java.lang.Object)
     */
    @Override
    public boolean add(final E element) {
        if (this.closed) {
            return false;
        } else {
            return super.add(element);
        }
    }

    /**
     * キューに要素を追加する
     * {@link #close()}が呼ばれた後は常に失敗する.
     * @param element キューに入れる要素
     * @return キューに要素が追加できればture, 失敗すればfalse
     * @see java.util.concurrent.LinkedBlockingQueue#offer(java.lang.Object)
     */
    @Override
    public boolean offer(final E element) {
        if (this.closed) {
            return false;
        } else {
            return super.offer(element);
        }
    }

    /**
     * キューに要素が追加できるまで，一定時間待つメソッド.
     * {@link #close()}が呼ばれた後は常に即座に失敗する.
     * @param element キューに入れる要素
     * @param timeout タイムアウトする時間
     * @param unit タイムアウトする時間の単位
     * @return キューに要素が追加できればture, 失敗すればfalse
     * @see java.util.concurrent.LinkedBlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean offer(final E element, final long timeout, final TimeUnit unit)
            throws InterruptedException {
        if (this.closed) {
            return false;
        } else {
            return super.offer(element, timeout, unit);
        }
    }

    /**
     * このキューを閉じる
     */
    public void close() {
        this.closed = true;
    }

    /**
     * キューが閉じられているかどうかを返す.
     * @return 閉じられていればture, そうでなければfalse
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * 閉じられたことを表すフラグ
     */
    private boolean closed = false;
}
