package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * 分析対象コンストラクタを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class TargetConstructorInfo extends ConstructorInfo {

    /**
     * 必要な情報を与えてオブジェクトを初期化
     * 
     * @param modifiers 修飾子のセット
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetConstructorInfo(final Set<ModifierInfo> modifiers, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fromLine, fromColumn, toLine, toColumn);
    }
}
