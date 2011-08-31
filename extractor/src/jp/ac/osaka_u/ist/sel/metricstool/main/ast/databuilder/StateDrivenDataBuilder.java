package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.AstVisitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;


/**
 * AST上で複数の状態に分けて定義されるデータを構築するクラス
 * 各状態に応じた構築処理を行い，最終的に1つのデータを構築する
 * 
 * @author kou-tngt, t-miyake
 *
 * @param <T> 構築されるデータの型
 */
public abstract class StateDrivenDataBuilder<T> extends DataBuilderAdapter<T> implements
        StateChangeListener<AstVisitEvent> {

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void entered(final AstVisitEvent e) {
        if (isActive()) {
            for (final AstVisitListener listener : this.stateManagers) {
                listener.entered(e);
            }
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    @Override
    public void exited(final AstVisitEvent e) throws ASTParseException {
        if (isActive()) {
            for (final AstVisitListener listener : this.stateManagers) {
                listener.exited(e);
            }
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener#stateChangend(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent)
     */
    public abstract void stateChanged(StateChangeEvent<AstVisitEvent> event);

    /**
     * ASTの解析状態をのマネージャーを追加
     * @param stateManager 
     */
    protected final void addStateManager(AstVisitStateManager stateManager) {
        //注：このメソッドのfinal修飾子は絶対に外してはならない．

        this.stateManagers.add(stateManager);
        stateManager.addStateChangeListener(this);
    }

    /**
     * ASTの解析状態のマネージャーを保存するフィールド
     */
    private final Set<AstVisitStateManager> stateManagers = new LinkedHashSet<AstVisitStateManager>();
}
