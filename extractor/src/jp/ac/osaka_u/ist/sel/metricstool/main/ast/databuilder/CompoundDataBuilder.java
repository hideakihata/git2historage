package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * 複数のビルダを組み合わせて，複合的な情報を構築する抽象クラス．
 * 事前に登録されたビルダに対するイベント通知処理の代行を行う．
 * 
 * @author kou-tngt
 *
 * @param <T> このクラスを継承したクラスが構築するデータの型
 */
public abstract class CompoundDataBuilder<T> extends StateDrivenDataBuilder<T> {

    /**
     * イベントの通知をしたいビルダを登録する．
     * 登録されたビルダは初期状態で非アクティブ化されるため，各サブクラスは，任意のタイミングで適切にビルダをアクティブにしなければならない．
     * サブクラスのコンストラクタから呼び出されることを考慮してあるので，finalを外してはならない．
     * 
     * @param builder　登録するビルダ
     */
    public final void addInnerBuilder(final DataBuilder<?> builder) {
        if (null != builder) {
            builder.deactivate();
            this.builders.add(builder);
        }
    }

    /**
     * ASTノードが任意のノードの中に入った時に呼び出される．
     * まず状態管理を行う親クラスの {@link StateDrivenDataBuilder#entered(AstVisitEvent)} メソッドを呼び出し，
     * 状態変化をさせた後で，アクティブ状態にあるビルダに対してイベントを通知する．
     * @param e ビジターのASTビジットイベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent e) {
        super.entered(e);

        if (this.isActive()) {
            for (final DataBuilder<?> builder : this.builders) {
                builder.entered(e);
            }
        }
    }

    /**
     * ASTノードが任意のノードから出た時に呼び出される．
     * 先にアクティブ状態にあるビルダに対してイベントを通知した後に，
     * 親クラスの {@link StateDrivenDataBuilder#entered(AstVisitEvent)} メソッドを呼び出し状態変化を発生させる．
     * @param e ビジターのASTビジットイベント
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent e) throws ASTParseException {
        if (this.isActive()) {
            for (final DataBuilder<?> builder : this.builders) {
                builder.exited(e);
            }
        }

        super.exited(e);
    }

    /**
     * 登録された内部ビルダを削除する
     * 
     * @param builder　削除するビルダ
     */
    public final void removeInnerBuilder(final DataBuilder<?> builder) {
        this.builders.remove(builder);
    }

    /**
     * ビルだの状態をリセットする．
     * 全ての内部ビルダの状態もリセットする．
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter#reset()
     */
    @Override
    public void reset() {
        super.reset();
        for (final DataBuilder<?> builder : this.builders) {
            builder.reset();
        }
    }

    /**
     * 利用する内部ビルだのセット
     */
    private final Set<DataBuilder<?>> builders = new LinkedHashSet<DataBuilder<?>>();
}
