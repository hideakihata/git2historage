package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.TypeBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * Java用の型構築を行うビルダー．
 * ジェネリクスの型引数でワイルドカード使用時に，親クラスが指定されなかった場合に，
 * java.lang.Objectを登録する．
 * 
 * @author kou-tngt
 *
 */
public class JavaTypeBuilder extends TypeBuilder {

    /**
     * 親クラスの同じ引数を受け取るコンストラクタを呼び出す．
     * 
     * @param buildDataManager
     */
    public JavaTypeBuilder(final BuildDataManager buildDataManager) {
        super(buildDataManager);
    }

    /**
     * ワイルドカード使用時に境界として指定された型を返す．
     * Javaの場合指定されなければjava.lang.Objectを返す．
     * @return 上限クラスとして指定された型を，指定されなければjava.lang.Objectを返す．
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.TypeBuilder#getCurrentUpperBounds()
     */
    @Override
    protected UnresolvedTypeInfo<? extends TypeInfo> getCurrentBounds() {
        final UnresolvedTypeInfo<? extends TypeInfo> type = super.getCurrentBounds();
        if (null == type) {
            return JAVA_LANG_OBJECT;
        } else {
            return type;
        }
    }

    /**
     * java.lang.Objectを表す型参照
     */
    public final static UnresolvedClassTypeInfo JAVA_LANG_OBJECT = new UnresolvedClassTypeInfo(
            new LinkedList<UnresolvedClassImportStatementInfo>(), new String[] { "java", "lang",
                    "Object" });

    public final static UnresolvedClassTypeInfo JAVA_LANG_STRIG = new UnresolvedClassTypeInfo(
            new LinkedList<UnresolvedClassImportStatementInfo>(), new String[] { "java", "lang",
                    "String" });
}
