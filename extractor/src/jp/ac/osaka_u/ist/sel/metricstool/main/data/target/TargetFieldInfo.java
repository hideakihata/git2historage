package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 対象クラスに定義されているフィールドの情報を現すクラス．
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class TargetFieldInfo extends FieldInfo {

    /**
     * フィールド情報オブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param name 名前
     * @param definitionClass このフィールドを定義しているクラス
     * @param instance インスタンスメンバーかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TargetClassInfo definitionClass, final boolean instance, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, name, definitionClass, instance, fromLine, fromColumn, toLine, toColumn);

        this.initializer = null;
    }

    /**
     * フィールドの初期化式をセットする
     * 
     * @param initializer フィールドの初期化式
     */
    public final void setInitializer(final ExpressionInfo initializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == initializer) {
            throw new IllegalArgumentException();
        }

        this.initializer = initializer;
    }

    /**
     * フィールドの初期化式を返す
     * 
     * @return　フィールドの初期化式
     */
    public final ExpressionInfo getInitializer() {
        return this.initializer;
    }

    private ExpressionInfo initializer;

}
