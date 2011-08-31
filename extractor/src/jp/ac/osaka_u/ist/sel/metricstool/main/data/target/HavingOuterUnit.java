package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 外側のユニットが存在することを表すインターフェース
 * 
 * @author higo
 *
 */
public interface HavingOuterUnit {

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    UnitInfo getOuterUnit();
    
    /**
     * 外側のユニットを設定する
     * 
     * @param outerUnit 外側のユニット
     */
    void setOuterUnit(UnitInfo outerUnit);
    
    /**
     * 外側のクラスを返す.
     * 
     * @return　外側のクラス
     */
    ClassInfo getOuterClass();

    /**
     * 外側の呼び出し可能なユニット（メソッド，コンストラクタ等）を返す
     * 
     * @return 外側の呼び出し可能なユニット（メソッド，コンストラクタ等）
     */
    CallableUnitInfo getOuterCallableUnit();
}
