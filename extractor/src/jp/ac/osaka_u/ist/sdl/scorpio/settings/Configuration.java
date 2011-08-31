package jp.ac.osaka_u.ist.sdl.scorpio.settings;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.DISSOLUTION;

public final class Configuration {

	public static Configuration INSTANCE = new Configuration();

	private Configuration() {
		this.c = Integer.MAX_VALUE;
		this.d = new HashSet<String>();
		this.l = null;
		this.m = SMALL_METHOD.UNHASHED;
		this.e = MERGE.FALSE;
		this.f = DISSOLUTION.FALSE;
		this.g = OUTPUT_FORMAT.SET;
		this.h = HEURISTICS.ON;
		this.o = null;
		this.p = PDG_TYPE.INTRA;
		this.q = new HashSet<DEPENDENCY_TYPE>();
		this.q.add(DEPENDENCY_TYPE.DATA);
		this.q.add(DEPENDENCY_TYPE.CONTROL);
		this.q.add(DEPENDENCY_TYPE.EXECUTION);
		this.s = 7;
		this.v = VERBOSE.FALSE;
		this.w = 1;
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.z = Integer.MAX_VALUE;
		this.pv = VARIABLE_NORMALIZATION.TYPE;
		this.pi = CALL_NORMALIZATION.NO;
		this.pc = CAST_NORMALIZATION.NO;
		this.po = OPERATION_NORMALIZATION.NO;
		this.pl = LITERAL_NORMALIZATION.TYPE;
		this.pr = REFERENCE_NORMALIZATION.NO;
	}

	public int getC() {
		return this.c;
	}

	public void setC(final int c) {
		this.c = c;
	}

	public Set<String> getD() {
		return Collections.unmodifiableSet(this.d);
	}

	public void addD(final String d) {
		this.d.add(d);
	}

	public MERGE getE() {
		return this.e;
	}

	public void setE(final MERGE e) {
		this.e = e;
	}

	public DISSOLUTION getF() {
		return this.f;
	}

	public void setF(final DISSOLUTION f) {
		this.f = f;
	}

	public OUTPUT_FORMAT getG() {
		return this.g;
	}

	public void setG(final OUTPUT_FORMAT g) {
		this.g = g;
	}

	public HEURISTICS getH() {
		return this.h;
	}

	public void setH(final HEURISTICS h) {
		this.h = h;
	}

	public String getL() {
		return this.l;
	}

	public void setL(final String l) {
		this.l = l;
	}

	public SMALL_METHOD getM() {
		return this.m;
	}

	public void setM(final SMALL_METHOD m) {
		this.m = m;
	}

	public String getO() {
		return this.o;
	}

	public void setO(final String o) {
		this.o = o;
	}

	public PDG_TYPE getP() {
		return this.p;
	}

	public void setP(final PDG_TYPE p) {
		this.p = p;
	}

	public Set<DEPENDENCY_TYPE> getQ() {
		return Collections.unmodifiableSet(this.q);
	}

	public void addQ(final DEPENDENCY_TYPE dependency) {
		this.q.add(dependency);
	}

	public void resetQ() {
		this.q.clear();
	}

	public int getS() {
		return this.s;
	}

	public void setS(final int s) {
		this.s = s;
	}

	public VERBOSE getV() {
		return this.v;
	}

	public void setV(final VERBOSE v) {
		this.v = v;
	}

	public int getW() {
		return this.w;
	}

	public void setW(final int w) {
		this.w = w;
	}

	public int getX() {
		return this.x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public int getZ() {
		return this.z;
	}

	public void setZ(final int z) {
		this.z = z;
	}

	public VARIABLE_NORMALIZATION getPV() {
		return this.pv;
	}

	public void setPV(final VARIABLE_NORMALIZATION pv) {
		this.pv = pv;
	}

	public CALL_NORMALIZATION getPI() {
		return this.pi;
	}

	public void setPI(final CALL_NORMALIZATION pi) {
		this.pi = pi;
	}

	public CAST_NORMALIZATION getPC() {
		return this.pc;
	}

	public void setPC(final CAST_NORMALIZATION pc) {
		this.pc = pc;
	}

	public OPERATION_NORMALIZATION getPO() {
		return this.po;
	}

	public void setPO(final OPERATION_NORMALIZATION po) {
		this.po = po;
	}

	public LITERAL_NORMALIZATION getPL() {
		return this.pl;
	}

	public void setPL(final LITERAL_NORMALIZATION pl) {
		this.pl = pl;
	}

	public REFERENCE_NORMALIZATION getPR() {
		return this.pr;
	}

	public void setPR(final REFERENCE_NORMALIZATION pr) {
		this.pr = pr;
	}

	/**
	 * ハッシュ値が同じ文が閾値以上の個数ある場合は，スライス拠点としないためのオプション
	 */
	private int c;

	/**
	 * 解析対象ディレクトリを指定するためのオプション
	 */
	private Set<String> d;

	/**
	 * ハッシュ値が同じで連続して存在している頂点を集約するかを指定するためのオプション
	 */
	private MERGE e;

	/**
	 * 文や式を細粒度まで分解するかどうかを指定するためのオプション
	 */
	private DISSOLUTION f;

	/**
	 * 出力形式を指定するためのオプション
	 */
	private OUTPUT_FORMAT g;

	/**
	 * 経験則に基づくフィルタリングを行うかどうかを指定するためのオプション
	 */
	private HEURISTICS h;

	/**
	 * 解析対象プログラミング言語を指定するためのオプション
	 */
	private String l;

	/**
	 * 閾値よりも小さいメソッドをハッシュ化するかどうかを指定するためのオプション
	 */
	private SMALL_METHOD m;

	/**
	 * 解析結果を出力するファイルを指定するためのオプション
	 */
	private String o;

	/**
	 * 用いるPDGのタイプを指定するためのオプション
	 */
	private PDG_TYPE p;

	/**
	 * PDGで用いる依存関係を指定するためのオプション
	 */
	private Set<DEPENDENCY_TYPE> q;

	/**
	 * 出力するコードクローンのサイズの下限を指定するためのオプション． 大きさは文の数を表す．
	 */
	private int s;

	/**
	 * 冗長な出力を行うためのオプション
	 */
	private VERBOSE v;

	/**
	 * 検出に用いるスレッド数を指定するためのオプション
	 */
	private int w;

	/**
	 * データ依存辺を引く頂点がソースコード上で離れていてもよい上限の値
	 */
	private int x;

	/**
	 * 制御依存を引く頂点がソースコード上で離れていてもよい上限の値
	 */
	private int y;

	/**
	 * 実行依存を引く頂点がソースコード上で離れていてもよい上限の値
	 */
	private int z;

	/**
	 * 変数利用の正規化レベルを指定するためのオプション no: 変数名をそのまま使う type: 変数名を型名に正規化する． all:
	 * 全ての変数を同一字句に正規化する．
	 */
	private VARIABLE_NORMALIZATION pv;

	/**
	 * (メソッドまたはコンストラクタ)呼び出しの正規化レベルを指定するためのオプション no: 呼び出し名はそのまま，引数情報も用いる
	 * type_with_arg: 呼び出し名を返り値の型名に変換する，引数情報も用いる． type_without_arg:
	 * 呼び出し名を返り値の型名に変換する，引数情報は用いない． all: 全ての呼び出しを同一字句に正規化する．引数情報は用いない
	 */
	private CALL_NORMALIZATION pi;

	/**
	 * キャスト使用の正規化レベルを指定するためのオプション 0: キャスト使用をそのまま用いる． 1: キャスト使用を型に正規化する． 2:
	 * 全てのキャスト使用を同一字句に正規化する．
	 */
	private CAST_NORMALIZATION pc;

	/**
	 * 
	 * 単項演算，二項演算，三項演算の正規化レベルを指定するためのオプション no: 演算をそのまま用いる type: 演算をその型に正規化する all:
	 * 全ての演算を同一の字句に正規化する
	 */
	private OPERATION_NORMALIZATION po;

	/**
	 * 
	 * リテラルの正規化レベルを指定するためのオプション no: リテラルをそのまま用いる type: リテラルをその型の正規化する all:
	 * 全てのリテラルを同一の字句に正規化する
	 */
	private LITERAL_NORMALIZATION pl;

	/**
	 * 
	 * クラス参照名を正規化するためのオプション 0: クラス参照は正規化しない 1: 全てのクラス参照を同一字句に正規化する
	 */
	private REFERENCE_NORMALIZATION pr;
}
