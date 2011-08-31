package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


/**
 * ビルダーが構築する情報を管理して，情報全体の整合性を取るインタフェース．
 * 以下の3種類の機能を連携して行う.
 * 
 * 1. 構築中のデータに関する情報の管理，提供及び構築状態の管理
 * 
 * 2. 名前空間，エイリアス，変数などのスコープ管理
 * 
 * 3. クラス情報，メソッド情報，変数代入，変数参照，メソッド呼び出し情報などの登録作業の代行
 * 
 * @author kou-tngt
 *
 */
public interface BuildDataManager {

    /**
     * 構築中のクラスにフィールド情報を追加する
     * 
     * @param field
     */
    public void addField(UnresolvedFieldInfo field);

    /**
     * 構築中のメソッドにフィールド使用情報を追加する
     * 
     * @param usage
     */
    public void addVariableUsage(
            UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usage);

    /**
     * 構築中のメソッドにローカルパラメータ（for文中で宣言される変数のように，
     * 宣言された場所から次のブロックの終わりまでスコープが有効な変数）情報を追加する
     * 
     * @param localParameter
     */
    public void addLocalParameter(UnresolvedLocalVariableInfo localParameter);

    /**
     * 構築中のメソッドにローカル変数情報を追加する
     * 
     * @param localVariable
     */
    public void addLocalVariable(UnresolvedLocalVariableInfo localVariable);

    /**
     * 構築中のメソッドにメソッド呼び出し情報を追加する
     * @param memberCall
     */
    public void addMethodCall(
            UnresolvedCallInfo<? extends CallInfo<? extends CallableUnitInfo>> memberCall);

    /**
     * 構築中のメソッドに引数情報を追加する
     * 
     * @param parameter
     */
    public void addMethodParameter(UnresolvedParameterInfo parameter);

    /**
     * 構築中のデータに適切に型パラメータをセットする．
     * @param typeParameter　セットする型パラメータ
     */
    public void addTypeParameger(UnresolvedTypeParameterInfo typeParameter);

    /**
     * 現在のブロックスコープ内で有効な名前エイリアスを追加する
     * 
     * @param alias インポート文により置き換えられた名前 
     * @param importStatement 未解決インポート文
     */
    public void addUsingAlias(String alias, final UnresolvedImportStatementInfo<?> importStatement);

    /**
     * 現在のブロックスコープ内で有効な，名前空間利用情報を追加する
     * 
     * @param importStatement 未解決インポート文
     */
    public void addUsingNameSpace(final UnresolvedImportStatementInfo<?> importStatement);

    public void addStatement(final UnresolvedStatementInfo<? extends StatementInfo> statement);

    public void addLabel(final UnresolvedLabelInfo label);

    public UnresolvedLabelInfo getAvailableLabel(final String labelName);

    /**
     * クラス構築が終了する時に呼ばれる．
     * 構築中の
     * 
     * @return
     */
    public UnresolvedClassInfo endClassDefinition();

    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> endCallableUnitDefinition();

    public UnresolvedBlockInfo<? extends BlockInfo> endInnerBlockDefinition();

    public void endScopedBlock();

    public void enterClassBlock();

    public void enterMethodBlock();

    public List<UnresolvedImportStatementInfo<?>> getAvailableNameSpaceSet();

    public List<UnresolvedImportStatementInfo<?>> getAvailableAliasSet();

    public List<UnresolvedImportStatementInfo<?>> getAllAvaliableNames();

    public String[] getAliasedName(String name);

    public int getAnonymousClassCount(UnresolvedClassInfo classInfo);

    public UnresolvedUnitInfo<? extends UnitInfo> getCurrentUnit();
    
    public UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit();
    
    public UnresolvedClassInfo getCurrentClass();

    public String[] getCurrentNameSpace();

    public UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> getCurrentLocalSpace();

    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getCurrentCallableUnit();

    public UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> getCurrentScopeVariable(
            String name);

    public UnresolvedTypeParameterTypeInfo getTypeParameterType(String name);

    public boolean hasAlias(String name);

    public void reset();

    public String[] popNameSpace();

    public void pushNewNameSpace(String[] nameSpace);

    public String[] resolveAlias(String[] name);

    public void startScopedBlock();

    public void startClassDefinition(UnresolvedClassInfo classInfo);

    public void startCallableUnitDefinition(
            UnresolvedCallableUnitInfo<? extends CallableUnitInfo> methodInfo);

    public void startInnerBlockDefinition(UnresolvedBlockInfo<? extends BlockInfo> blockInfo);

    public UnresolvedBlockInfo<? extends BlockInfo> getCurrentBlock();

    public int getCurrentTypeParameterCount();

    public int getCurrentParameterCount();

}
