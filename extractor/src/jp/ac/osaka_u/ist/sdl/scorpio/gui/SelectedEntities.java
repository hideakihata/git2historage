package jp.ac.osaka_u.ist.sdl.scorpio.gui;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.Entity;


/**
 * 現在選択中の要素（クローンペアなどを保存するためのクラス． オブザーバーパターンを用いることができるように，java.util.Observable を継承している
 * 
 * @author higo
 */
public final class SelectedEntities<T extends Entity> extends Observable {

    /**
     * このクラスのオブジェクトを表す
     * 
     * @param label オブジェクトを示すラベル文字
     * @return ラベル文字によって示されているこのクラスのオブジェクト
     */
    public static final <S extends Entity> SelectedEntities<S> getInstance(final String label) {
        SelectedEntities<S> instance = (SelectedEntities<S>) INSTANCES.get(label);
        if (null == instance) {
            instance = new SelectedEntities<S>(label);
            INSTANCES.put(label, instance);
        }
        return instance;
    }

    /**
     * 新しいエンティティファイルを選択エンティティとして追加する
     * 
     * @param entity 追加するエンティティ
     * @param source 変更者
     */
    public void add(final T entity, final Observer source) {

        if (null == entity) {
            throw new NullPointerException();
        }

        this.selectedEntities.add(entity);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * 新しいエンティティ群を選択エンティティとして追加する
     * 
     * @param entities 追加するエンティティ群
     * @param source 変更者
     */
    public void addAll(final Collection<T> entities, final Observer source) {

        if (null == entities) {
            throw new NullPointerException();
        }

        this.selectedEntities.addAll(entities);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * エンティティを選択エンティティから削除する
     * 
     * @param entity 削除するエンティティ
     * @param source 変更者
     */
    public void remove(final T entity, final Observer source) {

        if (null == entity) {
            throw new NullPointerException();
        }

        this.selectedEntities.remove(entity);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * エンティティ群を選択エンティティから削除する
     * 
     * @param entities 削除するエンティティ群
     * @param source 変更者
     */
    public void removeAll(final Collection<T> entities, final Observer source) {

        if (null == entities) {
            throw new NullPointerException();
        }

        this.selectedEntities.removeAll(entities);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * 新しいエンティティを選択エンティティとしてセットする．既に選択されていたエンティティは全て削除される
     * 
     * @param entity セットするエンティティ
     * @param source 変更者
     */
    public void set(final T entity, final Observer source) {

        if (null == entity) {
            throw new NullPointerException();
        }

        this.selectedEntities.clear();
        this.selectedEntities.add(entity);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * 新しいエンティティ群を選択エンティティとしてセットする．既に選択されていたエンティティは全て削除される
     * 
     * @param entities セットするエンティティ群
     * @param source 変更者
     */
    public void setAll(final Collection<T> entities, final Observer source) {

        if (null == entities) {
            throw new NullPointerException();
        }

        this.selectedEntities.clear();
        this.selectedEntities.addAll(entities);
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * 選択エンティティが存在するかどうかを返す．
     * 
     * @return １つ以上のエンティティが選択されている場合 true，全くエンティティが登録されていない場合 false
     */
    public boolean isSet() {
        return !this.selectedEntities.isEmpty();
    }

    /**
     * 選択エンティティを全て削除する
     * 
     * @param source 変更者
     */
    public void clear(final Observer source) {

        this.selectedEntities.clear();
        this.source = source;

        this.setChanged();
        this.notifyObservers(source);
    }

    /**
     * 現在選択中のエンティティの Set を返す
     * 
     * @return 現在選択中のエンティティの Set を返す
     */
    public SortedSet<T> get() {
        return Collections.unmodifiableSortedSet(this.selectedEntities);
    }

    /**
     * このオブジェクトを変更した最後のオブジェクト返す．
     * 
     * @return このオブジェクトを変更した最後のオブジェクト
     */
    public Observer getSource() {
        return this.source;
    }

    /**
     * このオブジェクトのラベルを返す
     * 
     * @return このオブジェクトのラベル
     */
    public String getLabel() {
        return this.label;
    }

    private static final Map<String, SelectedEntities<? extends Entity>> INSTANCES = new HashMap<String, SelectedEntities<? extends Entity>>();

    private SelectedEntities(final String label) {

        if (null == label) {
            throw new NullPointerException();
        }

        this.selectedEntities = new TreeSet<T>();
        this.source = null;
        this.label = label;
    }

    private final SortedSet<T> selectedEntities;

    private Observer source;

    private final String label;
}
