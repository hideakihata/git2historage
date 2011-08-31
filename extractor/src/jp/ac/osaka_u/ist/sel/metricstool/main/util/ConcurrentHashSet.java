package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * {@link ConcurrentHashMap}を用いた同期Set
 * 
 * @author kou-tngt
 *
 * @param <T> セットの要素の型
 * @see Set
 */
public class ConcurrentHashSet<T> implements Set<T> {
    /* (non-Javadoc)
     * @see java.util.Set#add(java.lang.Object)
     */
    public boolean add(final T o) {
        return null == this.INNER_MAP.put(o, DUMMY_VALUE);
    }

    /* (non-Javadoc)
     * @see java.util.Set#addAll(java.util.Collection)
     */
    public boolean addAll(final Collection<? extends T> c) {
        boolean result = false;
        for (final T element : c) {
            if (this.add(element)) {
                result = true;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.util.Set#clear()
     */
    public void clear() {
        this.INNER_MAP.clear();
    }

    /* (non-Javadoc)
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(final Object o) {
        return this.INNER_MAP.containsKey(o);
    }

    /* (non-Javadoc)
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty() {
        return this.INNER_MAP.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Set#iterator()
     */
    public Iterator<T> iterator() {
        return this.INNER_MAP.keySet().iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Set#remove(java.lang.Object)
     */
    public boolean remove(final Object o) {
        return null != this.INNER_MAP.remove(o);
    }

    /* (non-Javadoc)
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    public boolean removeAll(final Collection<?> c) {
        boolean result = false;
        for (final Object o : c) {
            if (this.remove(o)) {
                result = true;
            }
        }

        return result;
    }

    /* (non-Javadoc)
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    public boolean retainAll(final Collection<?> c) {
        boolean result = false;
        for (final Iterator<T> it = this.INNER_MAP.keySet().iterator(); it.hasNext();) {
            final T key = it.next();
            if (!c.contains(key)) {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.util.Set#size()
     */
    public int size() {
        return this.INNER_MAP.size();
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray()
     */
    public Object[] toArray() {
        return this.INNER_MAP.keySet().toArray();
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray(T[])
     */
    public <K> K[] toArray(final K[] a) {
        return this.INNER_MAP.keySet().toArray(a);
    }

    /**
     * マップの値に入れるダミーオブジェクト.
     */
    private static final Object DUMMY_VALUE = new Object();

    /**
     * 内部で利用するハッシュマップ
     */
    private final Map<T, Object> INNER_MAP = new ConcurrentHashMap<T, Object>();

}
