package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 演算子を表す列挙型
 * 
 * @author higo
 * 
 */
public enum OPERATOR_TYPE {

    /**
     * 算術演算子を表す定数． 具体的には，+, -, *, /, %．
     */
    ARITHMETIC,

    /**
     * 比較演算子を表す定数． 具体的には，<, >, <=, >=, ==, !=.
     */
    COMPARATIVE,

    /**
     * 論理演算子を表す定数． 具体的には，&&, ||, !.
     */
    LOGICAL,

    /**
     * ビット演算子を表す定数． 具体的には，&, |, ^, ~．
     */
    BITS,

    /**
     * シフト（ビット操作）演算子を表す定数． 具体的には，>>, >>>, <<.
     */
    SHIFT,

    /**
     * 代入演算子を表す定数． = だけでなく，+= や -= など左辺に代入される変数を置く全ての演算子を表す．
     */
    ASSIGNMENT;
    
}
