package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedInitializerInfo;


/**
 * イニシャライザのビルダ
 * 
 * @author g-yamada
 *
 * @param <T>　未解決イニシャライザ
 */
public abstract class InitializerBuilder<T extends UnresolvedInitializerInfo<? extends InitializerInfo>>
        extends CallableUnitBuilder<T> {
    protected InitializerBuilder(final BuildDataManager buildDataManager,
            final CallableUnitStateManager stateManager) {
        super(buildDataManager, stateManager);
    }

    @Override
    protected T startUnitDefinition(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        T initializer = super.startUnitDefinition(fromLine, fromColumn, toLine, toColumn);
        registToOwnerClass(initializer);
        return initializer;

    }

    /**
     * 自分の親クラスに自身を登録する
     * 
     * @param initializer 親クラスに登録するイニシャライザ
     */
    protected abstract void registToOwnerClass(final T initializer);

    @Override
    protected abstract T createUnresolvedCallableUnitInfo(int fromLine, int fromColumn, int toLine,
            int toColumn);

    @Override
    protected final void registName() {
        // i have no name
    }

    @Override
    protected final void registType() {
        // and no type
    }
}
