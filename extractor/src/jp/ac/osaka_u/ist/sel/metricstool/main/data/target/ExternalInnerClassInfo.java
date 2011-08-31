package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


@SuppressWarnings("serial")
public class ExternalInnerClassInfo extends ExternalClassInfo implements InnerClassInfo {

    public ExternalInnerClassInfo(final String[] fullQualifiedName, final UnitInfo outerUnit) {
        super(fullQualifiedName);
        this.outerUnit = outerUnit;
    }

    public ExternalInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final boolean isInterface) {

        super(modifiers, fullQualifiedName, isInterface);
    }

    /**
     * 外側の所有者を返す
     * 
     * @param outerOwner 外側の所有者
     */
    public void setOuterUnit(final UnitInfo outerUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == outerUnit) {
            throw new IllegalArgumentException();
        }

        this.outerUnit = outerUnit;
    }

    /**
     * 外側のユニットを返す
     * 
     * @return 外側のユニット
     */
    @Override
    public UnitInfo getOuterUnit() {
        return this.outerUnit;
    }

    /**
     * 外側のクラスを返す.
     * 
     * @return　外側のクラス
     */
    @Override
    public final ClassInfo getOuterClass() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            // インナークラスなのでかならず外側のクラスがある
            if (null == outer) {
                throw new IllegalStateException();
            }

            if (outer instanceof ClassInfo) {
                return (ClassInfo) outer;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    /**
     * 外側のメソッドを返す.
     * 
     * @return　外側のメソッド
     */
    @Override
    public final CallableUnitInfo getOuterCallableUnit() {

        UnitInfo outer = this.getOuterUnit();

        while (true) {

            if (null == outer) {
                return null;
            }

            if (outer instanceof CallableUnitInfo) {
                return (CallableUnitInfo) outer;
            }

            if (!(outer instanceof HavingOuterUnit)) {
                return null;
            }

            outer = ((HavingOuterUnit) outer).getOuterUnit();
        }
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        return (TypeParameterizable) this.getOuterUnit();
    }

    /**
     * 外側のユニットのオブジェクトを保存する変数
     */
    private UnitInfo outerUnit;

}
