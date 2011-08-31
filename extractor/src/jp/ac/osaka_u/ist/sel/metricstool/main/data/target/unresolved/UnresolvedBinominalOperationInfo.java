package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


/**
 * 未解決二項演算を格納するためのクラス
 * 
 * @author higo
 * 
 */
public class UnresolvedBinominalOperationInfo extends
        UnresolvedExpressionInfo<BinominalOperationInfo> {

    /**
     * 演算子と2つのオペランドを与えて初期化する
     * 
     * @param operator 演算子
     * @param firstOperand 第一（未解決）オペランド
     * @param secondOperand 第二（未解決）オペランド
     */
    public UnresolvedBinominalOperationInfo(final OPERATOR operator,
            final UnresolvedExpressionInfo<?> firstOperand,
            final UnresolvedExpressionInfo<?> secondOperand) {

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.resolvedInfo = null;
    }

    /**
     * 未解決二項演算を解決し，その型を返す．
     * 
     * @param usingClass 未解決二項演算が行われているクラス
     * @param usingMethod 未解決二項演算が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み二項演算（つまり，演算結果の型）
     */
    @Override
    public BinominalOperationInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final OPERATOR operator = this.getOperator();
        final UnresolvedExpressionInfo<?> unresolvedFirstOperand = this.getFirstOperand();
        final UnresolvedExpressionInfo<?> unresolvedSecondOperand = this.getSecondOperand();
        final ExpressionInfo firstOperand = unresolvedFirstOperand.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        final ExpressionInfo secondOperand = unresolvedSecondOperand.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        /*// 要素使用のオーナー要素を返す
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);*/

        this.resolvedInfo = new BinominalOperationInfo(operator, firstOperand, secondOperand,
                usingMethod, fromLine, fromColumn, toLine, toColumn);
        /*this.resolvedInfo.setOwnerExecutableElement(ownerExecutableElement);*/

        return this.resolvedInfo;
    }

    /**
     * 演算子を取得する
     * 
     * @return 演算子
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * 第一（未解決）オペランドを取得する
     * 
     * @return 第一（未解決）オペランド
     */
    public UnresolvedExpressionInfo<?> getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * 第二（未解決）オペランドを取得する
     * 
     * @return 第二（未解決）オペランド
     */
    public UnresolvedExpressionInfo<?> getSecondOperand() {
        return this.secondOperand;
    }

    /**
     * 演算子をセットする
     * 
     * @param operator 演算子
     */
    public void setOperator(final OPERATOR operator) {

        if (null == operator) {
            throw new NullPointerException();
        }

        this.operator = operator;
    }

    /**
     * 第一（未解決）オペランドをセットする
     * 
     * @param firstOperand 第一（未解決）オペランド
     */
    public void setFirstOperand(final UnresolvedExpressionInfo<?> firstOperand) {

        if (null == firstOperand) {
            throw new NullPointerException();
        }

        this.firstOperand = firstOperand;
    }

    /**
     * 第二（未解決）オペランドをセットする
     * 
     * @param secondOperand 第二（未解決）オペランド
     */
    public void setSecondOperand(final UnresolvedExpressionInfo<?> secondOperand) {

        if (null == secondOperand) {
            throw new NullPointerException();
        }

        this.secondOperand = secondOperand;
    }

    private OPERATOR operator;

    private UnresolvedExpressionInfo<?> firstOperand;

    private UnresolvedExpressionInfo<?> secondOperand;

}
