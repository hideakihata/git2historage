package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;

/**
 * データビルダーのインターフェース．
 * 
 * ビルダのアクティブ，非アクティブの切り替え処理や，過去に構築したデータの管理，取得などを行うメソッド群を実装する．
 * 
 * @author kou-tngt, t-miyake
 * 
 * @param <T> ビルドされるデータの型
 */
/**
 * @author t-miyake
 *
 * @param <T>
 */
public interface DataBuilder<T> extends AstVisitListener{
    
    /**
     * ビルダをアクティブにする．
     */
    public void activate();
    
    /**
     * 過去に構築したデータをクリアする．
     */
    public void clearBuiltData();
    
    /**
     * ビルダを非アクティブにする．
     */
    public void deactivate();
    
    /**
     * 過去に構築されたデータのリストを取得．
     * 
     * @return 過去に構築されたデータのリスト
     */
    public List<T> getBuiltDatas();
    
    /**
     * 過去に構築されたデータの数を取得．
     * 
     * @return 過去に構築されたデータの数
     */
    public int getBuiltDataCount();
    
    /**
     * 過去に構築されたデータのうち最も古いデータを取得．
     * 
     * @return 過去に構築されたデータのうち最も古いデータ
     */
      public T getFirstBuiltData();
    
    /**
     * 過去に構築されたデータのうち最も新しいデータを取得．
     * 
     * @return 過去に構築されたデータのうち最も新しいデータ
     */
    public T getLastBuildData();
    
    /**
     * スタック内に残っているデータで，最も新しく構築されたデータをスタックから取り出して返す.
     * @return スタック内に残っているデータで，最も新しく構築されたデータ，データが無ければnull
     */
    public T popLastBuiltData();
    
    /**
     * 過去に構築されたデータを１つ以上持っているかどうかを返す.
     * 
     * @return 過去に構築されたデータが１つ以上存在する場合はtrue
     */
    public boolean hasBuiltData();
    
    /**
     * ビルダがアクティブかどうか返す．
     * 
     * @return ビルダがアクティブの場合はtrue
     */
    public boolean isActive();
    
    /**
     * ビルダを初期化．
     * 過去に構築されたデータは全て削除される．
     */
    public void reset();
}
