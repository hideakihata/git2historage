package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.NameBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.TypeParameterBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * Javaの型パラメータ情報を構築する．
 * 
 * {@link TypeParameterBuilder}　の {@link #getUpperBounds()}メソッドをオーバーライドして，
 * 上限クラスが指定されていない場合は，java.lang.Objectを表す型情報を構築する．
 * @author kou-tngt, t-miyake
 *
 */
public class JavaTypeParameterBuilder extends TypeParameterBuilder {

    /**
     * 親クラスのコンストラクタを呼び出す．
     * @param buildDataManager
     */
    public JavaTypeParameterBuilder(BuildDataManager buildDataManager) {
        super(buildDataManager, new NameBuilder(), new JavaTypeBuilder(buildDataManager));
    }

    /**
     * 構築する型パラメータの上限クラスを返す．
     * 親クラスで上限の型が決定できればそれを，できなければjava.lang.Objectを表す型情報を返す．
     * 
     * @return 親クラスで上限の型が決定できればその情報，できなければjava.lang.Objectを表す型情報
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.TypeParameterBuilder#getUpperBounds()
     */
    @Override
    protected List<UnresolvedTypeInfo<? extends TypeInfo>> getUpperBounds() {
        List<UnresolvedTypeInfo<? extends TypeInfo>> extendsTypeInfo = super.getUpperBounds();
        if (null == extendsTypeInfo) {
            ArrayList<UnresolvedTypeInfo<? extends TypeInfo>> object =  new ArrayList<UnresolvedTypeInfo<? extends TypeInfo>>();
            return object;
        } else {
            return extendsTypeInfo;
        }
    }
}
