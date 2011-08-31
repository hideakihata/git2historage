package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
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
import jp.ac.osaka_u.ist.sel.metricstool.main.util.UnitStack;


/**
 * ビルダーが構築する情報を管理して，情報全体の整合性を取るクラス.
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
public class DefaultBuildDataManager implements BuildDataManager {

    public DefaultBuildDataManager() {
        innerInit();
    }

    public void reset() {
        innerInit();
    }

    public void addField(final UnresolvedFieldInfo field) {
        if (this.unitStack.isClassAtPeek()) {
            this.unitStack.getLatestClass().addDefinedField(field);
            addScopedVariable(field);
        }
    }

    public void addVariableUsage(
            UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> usage) {
        final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this
                .getCurrentLocalSpace();
        if (null != currentLocal) {
            currentLocal.addVariableUsage(usage);
        }
    }

    public void addLocalParameter(final UnresolvedLocalVariableInfo localParameter) {
        if (this.unitStack.isCallableUnitAtPeek()) {
            this.unitStack.getLatestCallableUnit().addLocalVariable(localParameter);
            addNextScopedVariable(localParameter);
        } else if (this.unitStack.isBlockAtPeek()) {
            this.unitStack.getLatestBlock().addLocalVariable(localParameter);
            addNextScopedVariable(localParameter);
        }
    }

    public void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {
        if (this.unitStack.isCallableUnitAtPeek()) {
            this.unitStack.getLatestCallableUnit().addLocalVariable(localVariable);
            addScopedVariable(localVariable);
        } else if (this.unitStack.isBlockAtPeek()) {
            this.unitStack.getLatestBlock().addLocalVariable(localVariable);
            addScopedVariable(localVariable);
        }
    }

    public void addMethodCall(
            UnresolvedCallInfo<? extends CallInfo<? extends CallableUnitInfo>> memberCall) {
        final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocalSpace = this
                .getCurrentLocalSpace();
        if (null != currentLocalSpace) {
            currentLocalSpace.addCall(memberCall);
        }
    }

    public void addMethodParameter(final UnresolvedParameterInfo parameter) {
        if (this.unitStack.isCallableUnitAtPeek()) {
            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> method = this.unitStack
                    .getLatestCallableUnit();
            method.addParameter(parameter);
            addNextScopedVariable(parameter);
        }
    }

    /**
     * 現在のブロックスコープに変数を追加する.
     * @param var 追加する変数
     */
    private void addScopedVariable(
            UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> var) {
        if (!scopeStack.isEmpty()) {
            scopeStack.peek().addVariable(var);
        }
    }

    /**
     * 現在から次のブロック終了までスコープが有効な変数を追加する.
     * @param var　追加する変数
     */
    private void addNextScopedVariable(
            UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> var) {
        nextScopedVariables.add(var);
    }

    public void addTypeParameger(UnresolvedTypeParameterInfo typeParameter) {
        if (this.unitStack.isClassAtPeek()) {
            this.unitStack.getLatestClass().addTypeParameter(typeParameter);
        } else if (this.unitStack.isCallableUnitAtPeek()) {
            this.unitStack.getLatestCallableUnit().addTypeParameter(typeParameter);
        }
    }

    public void addUsingAlias(final String alias, final UnresolvedImportStatementInfo<?> importStatement){
        if (null == alias || null == importStatement){
            throw new IllegalArgumentException("import statement is null");
        }
        if (!this.scopeStack.isEmpty()) {
            final BlockScope scope = this.scopeStack.peek();
            scope.addAlias(alias, importStatement);

            //名前のエイリアス情報が変化したのでキャッシュをリセット
            aliasNameSetCache = null;
            allAvaliableNameSetCache = null;
        }
    }

    public void addUsingNameSpace(final UnresolvedImportStatementInfo<?> importStatement) {
        if (null == importStatement){
            throw new IllegalArgumentException("import statement is null");
        }
        if (!this.scopeStack.isEmpty()) {
            final BlockScope scope = this.scopeStack.peek();
            scope.addUsingNameSpace(importStatement);

            //名前空間情報が変化したのでキャッシュをリセット
            availableNameSpaceSetCache = null;
            allAvaliableNameSetCache = null;
        }
    }

    public void addStatement(final UnresolvedStatementInfo<? extends StatementInfo> statement) {
        if (null == statement) {
            throw new IllegalArgumentException("statement is null");
        }

        final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this
                .getCurrentLocalSpace();
        assert null != currentLocal;
        if (null != currentLocal) {
            currentLocal.addStatement(statement);
        }
    }

    @Override
    public void addLabel(UnresolvedLabelInfo label) {
        if (null == label) {
            throw new IllegalArgumentException("label is null");
        }

        this.availableLabelsStack.peek().add(label);
        this.addStatement(label);
    }

    public UnresolvedLabelInfo getAvailableLabel(final String labelName) {
        if (this.availableLabelsStack.isEmpty()) {
            return null;
        }

        final List<UnresolvedLabelInfo> availableLabels = new ArrayList<UnresolvedLabelInfo>(
                this.availableLabelsStack.peek());
        Collections.reverse(availableLabels);
        for (final UnresolvedLabelInfo label : availableLabels) {
            if (label.getName().equals(labelName)) {
                return label;
            }
        }

        return null;
    }

    public void endScopedBlock() {
        if (!this.scopeStack.isEmpty()) {
            this.scopeStack.pop();
            nextScopedVariables.clear();

            //名前情報キャッシュをリセット
            aliasNameSetCache = null;
            availableNameSpaceSetCache = null;
            allAvaliableNameSetCache = null;
        }
    }

    public UnresolvedClassInfo endClassDefinition() {
        if (!this.unitStack.isClassAtPeek()) {
            return null;
        } else {
            final UnresolvedClassInfo classInfo = (UnresolvedClassInfo) (this.unitStack.pop());

            //外側のクラスがない場合にだけ登録を行う（現在はすべてのクラスを登録する）
            //if (this.unitStack.isClassEmpty()) {
            DataManager.getInstance().getUnresolvedClassInfoManager().addClass(classInfo);
            //}

            if (null == this.unitStack.getLatestCallableUnit()) {
                //TODO methodStack.peek().addInnerClass(classInfo);
            }

            return classInfo;
        }
    }

    @SuppressWarnings("unchecked")
    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> endCallableUnitDefinition() {
        if (!this.unitStack.isCallableUnitAtPeek()) {
            return null;
        } else {
            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> callableUnit = (UnresolvedCallableUnitInfo<? extends CallableUnitInfo>) this.unitStack
                    .pop();

            this.nextScopedVariables.clear();
            this.availableLabelsStack.pop();
            return callableUnit;
        }
    }

    @SuppressWarnings("unchecked")
    public UnresolvedBlockInfo<? extends BlockInfo> endInnerBlockDefinition() {
        if (!this.unitStack.isBlockAtPeek()) {
            return null;
        } else {
            final UnresolvedBlockInfo<? extends BlockInfo> blockInfo = (UnresolvedBlockInfo<? extends BlockInfo>) this.unitStack
                    .pop();
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> outerSpace = blockInfo
                    .getOuterSpace();

            outerSpace.addChildSpaceInfo(blockInfo);

            return blockInfo;
        }
    }

    public void enterClassBlock() {
        assert this.unitStack.isClassAtPeek() : "class must be top of unitStack";

        final int classDepth = this.unitStack.getClassStack().size();
        if (classDepth >= 2) {
            /* 以下の実装は一時的なもの．outerUnitの修正が終わった段階で下のブロックコメントの実装に変更すべき */
            final Stack<UnresolvedClassInfo> classStack = this.unitStack.getClassStack();
            final UnresolvedClassInfo currentInnerClass = classStack.peek();
            final UnresolvedClassInfo outerClass = classStack.get(classDepth - 2);

            outerClass.addInnerClass(currentInnerClass);

            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> outerCallable = this.unitStack
                    .getLatestCallableUnit();
            UnresolvedUnitInfo<? extends UnitInfo> outerUnitForCurrentInnerClass = null;
            for (int i = this.unitStack.size() - 2; i >=0; i--){
                if (outerCallable == this.unitStack.get(i)){
                    outerUnitForCurrentInnerClass = outerCallable;
                } else if (outerClass == this.unitStack.get(i)){
                    outerUnitForCurrentInnerClass = outerClass;
                }
            }
            currentInnerClass.setOuterUnit(outerUnitForCurrentInnerClass);

            /* 以下outerUnit関連の修正が行われた時の実装 */
            // 現在構築中のクラスが内部クラスであった時の処理．
            // 内部クラスに外側のユニットを登録し，さらに外側のクラスに内部クラスを登録する
            /*final Stack<UnresolvedClassInfo> classStack = this.unitStack.getClassStack();
            final UnresolvedClassInfo currentInnerClass = classStack.peek();
            final UnresolvedClassInfo outerClass = classStack.get(classDepth - 2);
            
            outerClass.addInnerClass(currentInnerClass);

            // 下のは冗長．UnresolvedClassInfoのコンストラクタでouterUnitが正しく設定されているべき
            //currentInnerClass.setOuterUnit(this.unitStack.get(this.unitStack.size() - 2));*/
        }
    }

    public void enterMethodBlock() {

    }

    public List<UnresolvedImportStatementInfo<?>> getAllAvaliableNames() {
        //      nullじゃなければ変化してないのでキャッシュ使いまわし
        if (null != allAvaliableNameSetCache) {
            return allAvaliableNameSetCache;
        }

        List<UnresolvedImportStatementInfo<?>> resultSet = getAvailableAliasSet();
        for (UnresolvedImportStatementInfo<?> info : getAvailableNameSpaceSet()) {
            resultSet.add(info);
        }

        allAvaliableNameSetCache = resultSet;

        return resultSet;
    }
    
    /**
     * getAllAvailableNamesの結果を，インポート文の出現位置でソートして返す
     * @return 現在のスコープから見えるインポート文の集合
     */
    public List<UnresolvedImportStatementInfo<?>> getAllSortedAvailableNames(){
        final List<UnresolvedImportStatementInfo<?>> result = this.getAllAvaliableNames();
        Collections.sort(result);
        return result;
    }

    public List<UnresolvedImportStatementInfo<?>> getAvailableNameSpaceSet() {
        //nullじゃなければ変化してないのでキャッシュ使いまわし
        if (null != availableNameSpaceSetCache) {
            return availableNameSpaceSetCache;
        }

        final List<UnresolvedImportStatementInfo<?>> result = new LinkedList<UnresolvedImportStatementInfo<?>>();
        //まず先に今の名前空間を登録
        if (null == currentNameSpaceCache) {
            currentNameSpaceCache = new UnresolvedClassImportStatementInfo(getCurrentNameSpace(),
                    true);
        }
        result.add(currentNameSpaceCache);

        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {//Stackの実体はVectorなので後ろからランダムアクセス
            final BlockScope scope = this.scopeStack.get(i);
            final List<UnresolvedImportStatementInfo<?>> scopeLocalNameSpaceSet = scope
                    .getAvailableNameSpaces();
            for (final UnresolvedImportStatementInfo<?> info : scopeLocalNameSpaceSet) {
                result.add(info);
            }
        }
        availableNameSpaceSetCache = result;

        return result;
    }

    public List<UnresolvedImportStatementInfo<?>> getAvailableAliasSet() {
        //nullじゃなければ変化してないのでキャッシュ使いまわし
        if (null != aliasNameSetCache) {
            return aliasNameSetCache;
        }

        final List<UnresolvedImportStatementInfo<?>> result = new LinkedList<UnresolvedImportStatementInfo<?>>();
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {//Stackの実体はVectorなので後ろからランダムアクセス
            final BlockScope scope = this.scopeStack.get(i);
            final List<UnresolvedImportStatementInfo<?>> scopeLocalNameSpaceSet = scope
                    .getAvailableAliases();
            for (final UnresolvedImportStatementInfo<?> info : scopeLocalNameSpaceSet) {
                result.add(info);
            }
        }

        aliasNameSetCache = result;

        return result;
    }

    public String[] getAliasedName(final String alias) {
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {//Stackの実体はVectorなので後ろからランダムアクセス
            final BlockScope scope = this.scopeStack.get(i);
            if (scope.hasAlias(alias)) {
                return scope.replaceAlias(alias);
            }
        }
        return EMPTY_NAME;
    }

    public UnresolvedUnitInfo<? extends UnitInfo> getCurrentUnit() {
        return this.unitStack.isEmpty() ? null : this.unitStack.peek();
    }
    
    public UnresolvedUnitInfo<? extends UnitInfo> getOuterUnit() {
        if (this.unitStack.size() < 2){
            return null;
        }
        return this.unitStack.get(this.unitStack.size() - 2);
    }

    @Override
    public UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> getCurrentLocalSpace() {
        UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = null;

        if (this.unitStack.isCallableUnitAtPeek()) {
            currentLocal = this.getCurrentCallableUnit();
        } else if (this.unitStack.isBlockAtPeek()) {
            currentLocal = this.getCurrentBlock();
        }

        return currentLocal;
    }

    public UnresolvedClassInfo getCurrentClass() {
        return this.unitStack.getLatestClass();
    }

    public UnresolvedCallableUnitInfo<? extends CallableUnitInfo> getCurrentCallableUnit() {
        return this.unitStack.getLatestCallableUnit();
    }

    public UnresolvedBlockInfo<? extends BlockInfo> getCurrentBlock() {
        return this.unitStack.getLatestBlock();
    }

    public int getAnonymousClassCount(UnresolvedClassInfo classInfo) {
        if (null == classInfo) {
            throw new NullPointerException("classInfo is null.");
        }

        if (anonymousClassCountMap.containsKey(classInfo)) {
            int count = anonymousClassCountMap.get(classInfo);
            anonymousClassCountMap.put(classInfo, ++count);
            return count;
        } else {
            anonymousClassCountMap.put(classInfo, 1);
            return 1;
        }
    }

    /**
     * 現在の名前空間名を返す．
     * 
     * @return
     */
    public String[] getCurrentNameSpace() {
        final List<String> nameSpaceList = new ArrayList<String>();

        for (final String[] nameSpace : this.nameSpaceStack) {
            for (final String nameSpaceString : nameSpace) {
                nameSpaceList.add(nameSpaceString);
            }
        }

        return nameSpaceList.toArray(new String[nameSpaceList.size()]);
    }

    /**
     * スタックにつまれているクラスのクラス名も付けた名前空間を返す.
     * @return
     */
    public String[] getCurrentFullNameSpace() {
        final List<String> nameSpaceList = new ArrayList<String>();

        for (final String[] nameSpace : this.nameSpaceStack) {
            for (final String nameSpaceString : nameSpace) {
                nameSpaceList.add(nameSpaceString);
            }
        }

        for (final UnresolvedUnitInfo<? extends UnitInfo> unit : this.unitStack) {
            if (unit instanceof UnresolvedClassInfo) {
                final UnresolvedClassInfo classInfo = (UnresolvedClassInfo) unit;
                final String className = classInfo.getClassName();
                nameSpaceList.add(className);
            }
        }

        return nameSpaceList.toArray(new String[nameSpaceList.size()]);
    }

    public UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> getCurrentScopeVariable(
            String name) {

        for (UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> var : nextScopedVariables) {
            if (name.equals(var.getName())) {
                return var;
            }
        }

        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {
            final BlockScope scope = this.scopeStack.get(i);
            if (scope.hasVariable(name)) {
                return scope.getVariable(name);
            }
        }
        return null;
    }

    public UnresolvedTypeParameterTypeInfo getTypeParameterType(String name) {
        for (int i = unitStack.size() - 1; i >= 0; i--) {
            UnresolvedUnitInfo<? extends UnitInfo> currentUnit = unitStack.get(i);

            if (currentUnit instanceof UnresolvedClassInfo) {
                final UnresolvedClassInfo classInfo = (UnresolvedClassInfo) currentUnit;
                for (UnresolvedTypeParameterInfo param : classInfo.getTypeParameters()) {
                    if (param.getName().equals(name)) {
                        return new UnresolvedTypeParameterTypeInfo(param);
                    }
                }
            } else if (currentUnit instanceof UnresolvedCallableUnitInfo) {
                UnresolvedCallableUnitInfo<?> methodInfo = (UnresolvedCallableUnitInfo<?>) currentUnit;
                for (UnresolvedTypeParameterInfo param : methodInfo.getTypeParameters()) {
                    if (param.getName().equals(name)) {
                        return new UnresolvedTypeParameterTypeInfo(param);
                    }
                }
            }
        }

        return null;
    }

    public int getCurrentTypeParameterCount() {
        int count = -1;
        if (this.unitStack.isClassAtPeek()) {
            count = this.unitStack.getLatestClass().getTypeParameters().size();
        } else if (this.unitStack.isCallableUnitAtPeek()) {
            count = this.unitStack.getLatestCallableUnit().getTypeParameters().size();
        }

        return count;
    }

    public int getCurrentParameterCount() {
        int count = -1;
        if (this.unitStack.isCallableUnitAtPeek()) {
            count = this.unitStack.getLatestCallableUnit().getParameters().size();
        }
        return count;
    }

    public boolean hasAlias(final String name) {
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {
            final BlockScope scope = this.scopeStack.get(i);
            if (scope.hasAlias(name)) {
                return true;
            }
        }
        return false;
    }

    public void startScopedBlock() {
        BlockScope newScope = new BlockScope();
        this.scopeStack.push(newScope);

        for (UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> var : nextScopedVariables) {
            newScope.addVariable(var);
        }
    }

    public void pushNewNameSpace(final String[] nameSpace) {
        if (null == nameSpace) {
            throw new NullPointerException("nameSpace is null.");
        }

        if (0 == nameSpace.length) {
            throw new IllegalArgumentException("nameSpace has no entry.");
        }
        this.nameSpaceStack.push(nameSpace);
    }

    public String[] popNameSpace() {
        if (this.nameSpaceStack.isEmpty()) {
            return null;
        } else {
            return this.nameSpaceStack.pop();
        }
    }

    public String[] resolveAlias(String[] name) {
        if (name == null) {
            throw new NullPointerException("empty name.");
        }

        if (0 == name.length) {
            throw new IllegalArgumentException("empty name.");
        }

        List<String> resolvedName = new ArrayList<String>();
        int startPoint = 0;
        if (hasAlias(name[0])) {
            startPoint++;
            String[] aliasedName = getAliasedName(name[0]);
            for (String str : aliasedName) {
                resolvedName.add(str);
            }
        }

        for (int i = startPoint; i < name.length; i++) {
            resolvedName.add(name[i]);
        }

        return resolvedName.toArray(new String[resolvedName.size()]);
    }

    public void startClassDefinition(final UnresolvedClassInfo classInfo) {
        if (null == classInfo) {
            throw new NullPointerException("class info was null.");
        }

        classInfo.setNamespace(this.getCurrentFullNameSpace());

        final BlockScope currentScope = scopeStack.peek();
        classInfo.addImportStatements(currentScope.getAvailableAliases());
        classInfo.addImportStatements(currentScope.getAvailableNameSpaces());

        this.unitStack.push(classInfo);
    }

    public void startCallableUnitDefinition(
            final UnresolvedCallableUnitInfo<? extends CallableUnitInfo> callableUnit) {
        if (null == callableUnit) {
            throw new NullPointerException("method info was null.");
        }

        this.unitStack.push(callableUnit);

        this.availableLabelsStack.push(new ArrayList<UnresolvedLabelInfo>());
    }

    public void startInnerBlockDefinition(final UnresolvedBlockInfo<? extends BlockInfo> blockInfo) {
        if (null == blockInfo) {
            throw new IllegalArgumentException("block info was null.");
        }

        this.unitStack.push(blockInfo);
    }

    protected static class BlockScope {
        private final Map<String, UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>>> variables = new LinkedHashMap<String, UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>>>();

        //        private final Map<String, String[]> nameAliases = new LinkedHashMap<String, String[]>();
        private final Map<String, UnresolvedImportStatementInfo<?>> nameAliases = new LinkedHashMap<String, UnresolvedImportStatementInfo<?>>();

        private final List<UnresolvedImportStatementInfo<?>> availableNameSpaces = new LinkedList<UnresolvedImportStatementInfo<?>>();

        public void addVariable(
                final UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> variable) {
            this.variables.put(variable.getName(), variable);
        }

        public void addAlias(final String alias, final UnresolvedImportStatementInfo<?> importStatement) {
            if (null == importStatement) {
                throw new IllegalArgumentException();
            }

            this.nameAliases.put(alias, importStatement);
        }

        public void addUsingNameSpace(final UnresolvedImportStatementInfo<?> importStatement) {
            if (null == importStatement) {
                throw new IllegalArgumentException();
            }

            this.availableNameSpaces.add(importStatement);
        }

        public List<UnresolvedImportStatementInfo<?>> getAvailableNameSpaces() {
            return this.availableNameSpaces;
        }

        public List<UnresolvedImportStatementInfo<?>> getAvailableAliases() {
            List<UnresolvedImportStatementInfo<?>> resultSet = new LinkedList<UnresolvedImportStatementInfo<?>>();
            for (UnresolvedImportStatementInfo<?> info : this.nameAliases.values()) {
                resultSet.add(info);
            }
            return resultSet;
        }

        public UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>> getVariable(
                String name) {
            return this.variables.get(name);
        }

        public boolean hasVariable(final String varName) {
            return this.variables.containsKey(varName);
        }

        public boolean hasAlias(final String alias) {
            return this.nameAliases.containsKey(alias);
        }

        public String[] replaceAlias(final String alias) {
            Set<String[]> cycleCheckSet = new HashSet<String[]>();

            String aliasString = alias;
            if (this.nameAliases.containsKey(aliasString)) {
                String[] result = this.nameAliases.get(aliasString).getImportName();
                cycleCheckSet.add(result);

                if (result.length == 1) {
                    aliasString = result[0];
                    while (this.nameAliases.containsKey(aliasString)) {
                        result = this.nameAliases.get(aliasString).getImportName();
                        if (result.length == 1) {
                            if (cycleCheckSet.contains(result)) {
                                return result;
                            } else {
                                cycleCheckSet.add(result);
                                aliasString = result[0];
                            }
                        } else {
                            return result;
                        }
                    }
                } else {
                    return result;
                }
            }
            return EMPTY_NAME;
        }
    }

    private void innerInit() {
        this.unitStack.clear();
        this.nameSpaceStack.clear();
        this.scopeStack.clear();
        this.availableLabelsStack.clear();

        this.scopeStack.add(new BlockScope());

        aliasNameSetCache = null;
        availableNameSpaceSetCache = null;
        allAvaliableNameSetCache = null;
        currentNameSpaceCache = null;
    }

    private static final String[] EMPTY_NAME = new String[0];

    private List<UnresolvedImportStatementInfo<?>> aliasNameSetCache = null;

    private List<UnresolvedImportStatementInfo<?>> availableNameSpaceSetCache = null;

    private List<UnresolvedImportStatementInfo<?>> allAvaliableNameSetCache = null;

    private UnresolvedClassImportStatementInfo currentNameSpaceCache = null;

    private final Stack<BlockScope> scopeStack = new Stack<BlockScope>();

    private final Stack<String[]> nameSpaceStack = new Stack<String[]>();

    //    private final Stack<UnresolvedClassInfo> classStack = new Stack<UnresolvedClassInfo>();

    //    private final Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>> callableUnitStack = new Stack<UnresolvedCallableUnitInfo<? extends CallableUnitInfo>>();

    //    private final Stack<UnresolvedBlockInfo<? extends BlockInfo>> blockStack = new Stack<UnresolvedBlockInfo<? extends BlockInfo>>();

    private final UnitStack unitStack = new UnitStack();

    private final Set<UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>>> nextScopedVariables = new HashSet<UnresolvedVariableInfo<? extends VariableInfo<? extends UnitInfo>, ? extends UnresolvedUnitInfo<? extends UnitInfo>>>();

    private final Map<UnresolvedClassInfo, Integer> anonymousClassCountMap = new HashMap<UnresolvedClassInfo, Integer>();

    private final Stack<List<UnresolvedLabelInfo>> availableLabelsStack = new Stack<List<UnresolvedLabelInfo>>();
}
