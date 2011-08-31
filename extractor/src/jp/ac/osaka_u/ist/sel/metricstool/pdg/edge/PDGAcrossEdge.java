package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;

/**
 * メソッドをまたがる依存辺であることを表すインターフェース
 * 
 * @author higo
 * 
 */
public interface PDGAcrossEdge {

	/**
	 * この依存辺の生成元になったメソッドorコンストラクタ呼び出しを返す
	 * 
	 * @return
	 */
	CallInfo<?> getHolder();
}
