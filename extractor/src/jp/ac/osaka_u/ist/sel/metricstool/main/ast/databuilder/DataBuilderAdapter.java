package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Collections;
import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * データビルダーのアダプト実装．
 * 
 * ビルダのアクティブ，非アクティブの切り替え処理や，過去に構築したデータの管理，取得などを行うメソッド群を実装する．
 * 
 * @author kou-tngt, t-miyake
 * 
 * @param <T> ビルドされるデータの型
 */
public abstract class DataBuilderAdapter<T> implements DataBuilder<T> {

    /**
     * ビルダをアクティブにする．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#activate()
     */
    public final void activate() {
        this.active = true;
    }

    /**
     * 過去に構築したデータをスタックから削除する．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#clearBuiltData()
     */
    public void clearBuiltData() {
        this.builtDataStack.clear();
    }

    /**
     * ビルダを非アクティブにする．
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#deactivate()
     */
    public final void deactivate() {
        this.active = false;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public abstract void entered(final AstVisitEvent e);

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public abstract void exited(final AstVisitEvent e) throws ASTParseException;

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getBuiltDatas()
     */
    public final List<T> getBuiltDatas() {
        return Collections.unmodifiableList(this.builtDataStack);
    }

    /**
     * スタック内に残っているデータの数を返す．
     * @return 構築したデータの数
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getBuiltDataCount()
     */
    public int getBuiltDataCount() {
        return this.builtDataStack.size();
    }

    /**
     * スタック内に残っているデータで，最も古く構築されたデータを返す．
     * @return スタック内に残っているデータで，最も古く構築されたデータ，データが無ければnullを返す
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getFirstBuiltData()
     */
    public T getFirstBuiltData() {
        if (!this.builtDataStack.isEmpty()) {
            return this.builtDataStack.firstElement();
        } else {
            return null;
        }
    }

    /**
     * スタック内に残っているデータで，最も新しく構築されたデータを返す．
     * @return スタック内に残っているデータで，最も新しく構築されたデータ，データが無ければnullを返す
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#getLastBuildData()
     */
    public T getLastBuildData() {
        if (!this.builtDataStack.isEmpty()) {
            return this.builtDataStack.peek();
        } else {
            return null;
        }
    }

    /**
     * スタック内に残っているデータで，最も新しく構築されたデータをスタックから取り出して返す．
     * @return スタック内に残っているデータで，最も新しく構築されたデータ，データが無ければnullを返す
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#popLastBuiltData()
     */
    public T popLastBuiltData() {
        if (!this.builtDataStack.isEmpty()) {
            return this.builtDataStack.pop();
        } else {
            return null;
        }
    }

    /**
     * 過去に構築したデータを一つ以上持っているかどうかを返す．
     * @return 過去に構築したデータを一つ以上持っている場合はtrue
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#hasBuiltData()
     */
    public boolean hasBuiltData() {
        return !this.builtDataStack.isEmpty();
    }

    /**
     * ビルダが現在アクティブ状態であるかどうかを返す．
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#isActive()
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * ビルダを初期化する．
     * 過去に構築したデータは削除される．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilder#reset()
     */
    public void reset() {
        this.clearBuiltData();
    }

    /**
     * 何もしない．
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener#visited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public final void visited(final AstVisitEvent e) {
        // 使わないことにする
    }

    /**
     * 構築したデータをスタックに登録する
     * @param data 登録したいデータ
     */
    protected final void registBuiltData(final T data) {
        this.builtDataStack.add(data);
    }

    /**
     * このビルダが現在アクティブであるかどうか
     */
    private boolean active = true;

    /**
     * 構築したデータを管理するスタック
     */
    private final Stack<T> builtDataStack = new Stack<T>();
}
