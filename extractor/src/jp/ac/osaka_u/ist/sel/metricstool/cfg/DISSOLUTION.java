package jp.ac.osaka_u.ist.sel.metricstool.cfg;

/**
 * 文の分解を行うことを表すenum
 * 
 * @author higo
 * 
 */
public enum DISSOLUTION {

	/**
	 * 文の分解を完全に行う
	 */
	TRUE {

	},

	/**
	 * メソッドの引数とreturn文，メソッド呼び出しの返り値の扱い場所についてのみ行う
	 */
	PARTLY {

	},

	/**
	 * 文の分解を全く行わない
	 */
	FALSE {

	};
}
