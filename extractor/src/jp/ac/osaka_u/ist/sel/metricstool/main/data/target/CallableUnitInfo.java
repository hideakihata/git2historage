package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 呼び出し可能な単位(メソッドやコンストラクタ)を表すクラス
 * 
 * @author higo
 */

@SuppressWarnings("serial")
public abstract class CallableUnitInfo extends LocalSpaceInfo implements Visualizable, Modifier,
        TypeParameterizable {

    /**
     * オブジェクトを初期化する
     * 
     * @param modifiers 修飾子のSet
     * @param ownerClass 所有クラス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    CallableUnitInfo(final Set<ModifierInfo> modifiers, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.parameters = new LinkedList<ParameterInfo>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.thrownExceptions = new LinkedList<ReferenceTypeInfo>();

        this.unresolvedUsage = new HashSet<UnresolvedExpressionInfo<?>>();

        this.callers = new TreeSet<CallableUnitInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(super.getDefinedVariables());
        definedVariables.addAll(this.getParameters());
        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * メソッド間の順序の時は，定義されているクラスを考慮するために定義している．
     */
    @Override
    final public int compareTo(final Position o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (o instanceof CallableUnitInfo) {

            final ClassInfo ownerClass = this.getOwnerClass();
            final ClassInfo correspondOwnerClass = ((CallableUnitInfo) o).getOwnerClass();
            final int classOrder = ownerClass.compareTo(correspondOwnerClass);
            if (classOrder != 0) {
                return classOrder;
            }
        }

        return super.compareTo(o);
    }

    /**
     * 呼び出しユニットのハッシュコードを返す
     */
    @Override
    final public int hashCode() {
        return this.getFromLine() + this.getFromColumn() + this.getToLine() + this.getToColumn();
    }

    public int compareArgumentsTo(final CallableUnitInfo target) {
        // 引数の個数で比較
        final int parameterNumber = this.getParameterNumber();
        final int correspondParameterNumber = target.getParameterNumber();
        if (parameterNumber < correspondParameterNumber) {
            return 1;
        } else if (parameterNumber > correspondParameterNumber) {
            return -1;
        } else {

            // 引数の型で比較．第一引数から順番に．
            final Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
            final Iterator<ParameterInfo> correspondParameterIterator = target.getParameters()
                    .iterator();
            while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                final ParameterInfo parameter = parameterIterator.next();
                final ParameterInfo correspondParameter = correspondParameterIterator.next();
                final String typeName = parameter.getType().getTypeName();
                final String correspondTypeName = correspondParameter.getType().getTypeName();
                final int typeOrder = typeName.compareTo(correspondTypeName);
                if (typeOrder != 0) {
                    return typeOrder;
                }
            }

            return 0;
        }
    }

    /**
     * このオブジェクトが，引数で与えられた情報を使って呼び出すことができるかどうかを判定する．
     * 
     * @param actualParameters 実引数のリスト
     * @return 呼び出せる場合は true，そうでない場合は false
     */
    boolean canCalledWith(final List<ExpressionInfo> actualParameters) {

        if (null == actualParameters) {
            throw new IllegalArgumentException();
        }

        final ExpressionInfo[] actualParameterArray = actualParameters
                .toArray(new ExpressionInfo[0]);
        final ParameterInfo[] dummyParameterArray = this.getParameters().toArray(
                new ParameterInfo[0]);
        int checkedActualIndex = -1;

        for (int index = 0; index < dummyParameterArray.length; index++) {

            final ParameterInfo dummyParameter = dummyParameterArray[index];
            final TypeInfo dummyType = dummyParameter.getType();

            //仮引数が可変長引数の場合
            if (dummyParameter instanceof VariableLengthParameterInfo) {

                // TODO 現在のところ条件なしでOKにしている
                return true;
            }

            // 可変長引数以外の場合
            else {

                // 実引数の数が足りない場合は呼び出し不可               
                if (!(index < actualParameterArray.length)) {
                    return false;
                }

                final ExpressionInfo actualParameter = actualParameterArray[index];
                TypeInfo actualType = actualParameter.getType();

                // <?> や <? super A>の場合はjava.lang.Objectに変換する
                if (actualType instanceof ArbitraryTypeInfo || actualType instanceof SuperTypeInfo) {
                    final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                            .getClassInfo(new String[] { "java", "lang", "Object" });
                    actualType = new ClassTypeInfo(objectClass);
                }

                // <? extends B>の場合は Bに変換する
                else if (actualType instanceof ExtendsTypeInfo) {
                    actualType = ((ExtendsTypeInfo) actualType).getExtendsType();
                }

                if (!canCallWith(dummyType, actualType)) {
                    return false;
                }

                checkedActualIndex = index;
                continue;
            }
        }

        return (actualParameterArray.length - 1) == checkedActualIndex;
    }

    private static boolean canCallWith(final TypeInfo dummyType, final TypeInfo actualType) {

        //仮引数がクラス参照型の場合
        if (dummyType instanceof ClassTypeInfo) {

            final ClassInfo dummyClass = ((ClassTypeInfo) dummyType).getReferencedClass();

            // 実引数の型がUnknownTypeInfoのときはどうしようもないのでOKにする
            if (actualType instanceof UnknownTypeInfo) {
                return true;
            }

            // 仮引数がObject型のときは，クラス参照型，配列型，型パラメータ型がOK
            final ClassInfo objectClass = DataManager.getInstance().getClassInfoManager()
                    .getClassInfo(new String[] { "java", "lang", "Object" });
            if (((ClassTypeInfo) dummyType).getReferencedClass().equals(objectClass)) {
                if (actualType instanceof ReferenceTypeInfo) {
                    return true;
                }
            }

            // AutoBoxing, InBoxingの可能性を考慮
            if (PrimitiveTypeInfo.isJavaWrapperType((ClassTypeInfo) dummyType)
                    && actualType instanceof PrimitiveTypeInfo) {
                if (PrimitiveTypeInfo.getPrimitiveType((ClassTypeInfo) dummyType) == actualType) {
                    return true;
                } else {
                    return false;
                }
            }

            if (!(actualType instanceof ClassTypeInfo)) {
                return false;
            }

            final ClassInfo actualClass = ((ClassTypeInfo) actualType).getReferencedClass();

            // 仮引数，実引数共に対象クラスである場合は，その継承関係を考慮する．
            // つまり，実引数が仮引数のサブクラスでない場合は，呼び出し可能ではない
            if ((actualClass instanceof TargetClassInfo) && (dummyClass instanceof TargetClassInfo)) {

                // 実引数が仮引数と同じ参照型（クラス）でもなく，仮引数のサブクラスでもない場合は該当しない
                if (actualClass.equals(dummyClass)) {
                    return true;

                } else if (actualClass.isSubClass(dummyClass)) {
                    return true;

                } else {
                    return false;
                }
            }

            // 仮引数，実引数ともに外部クラスである場合は，条件なしで呼び出し可能とする．
            // 等しくないとダメという条件は厳しすぎて正しく判定できない場合がある．
            // 仮引数，実引数共に外部クラスである場合は，/*等しい場合のみ呼び出し*/可能とする
            else if ((actualClass instanceof ExternalClassInfo)
                    && (dummyClass instanceof ExternalClassInfo)) {
                return true;
            }

            // 仮引数が外部クラス，実引数が対象クラスの場合は，呼び出し可能とする
            // 等しくないとダメという条件は厳しすぎて正しく判定できない場合がある．
            else if ((actualClass instanceof TargetClassInfo)
                    && (dummyClass instanceof ExternalClassInfo)) {
                return true;
            }

            // 仮引数が対象クラス，実引数が外部クラスの場合は，呼び出し可能とする
            // 等しくないとダメという条件は厳しすぎて正しく判定できない場合がある．
            else {
                return true;
            }
        }

        // 仮引数がプリミティブ型の場合
        else if (dummyType instanceof PrimitiveTypeInfo) {

            // 実引数の型がUnknownTypeInfoのときはどうしようもないのでOKにする
            if (actualType instanceof UnknownTypeInfo) {
                return true;
            }

            // autoboxing, inboxingの可能性を考慮
            if (actualType instanceof ClassTypeInfo
                    && PrimitiveTypeInfo.isJavaWrapperType((ClassTypeInfo) actualType)) {
                if (PrimitiveTypeInfo.getPrimitiveType((ClassTypeInfo) actualType) == dummyType) {
                    return true;
                } else {
                    return false;
                }
            }

            // 実引数がプリミティブ型でない場合は呼び出し不可
            if (!(actualType instanceof PrimitiveTypeInfo)) {
                return false;
            }

            return true;
        }

        // 仮引数が配列型の場合
        else if (dummyType instanceof ArrayTypeInfo) {

            // 実引数の型がUnknownTypeInfoのときはどうしようもないのでOKにする
            if (actualType instanceof UnknownTypeInfo) {
                return true;
            }

            // 実引数が配列型でない場合は呼び出し不可
            if (!(actualType instanceof ArrayTypeInfo)) {
                return false;
            }

            // 次元数が違う場合は呼び出し不可
            final int dummyDimenstion = ((ArrayTypeInfo) dummyType).getDimension();
            final int actualDimenstion = ((ArrayTypeInfo) actualType).getDimension();
            if (dummyDimenstion != actualDimenstion) {
                return false;
            }

            // 要素の型をチェック
            final TypeInfo dummyElementType = ((ArrayTypeInfo) dummyType).getElementType();
            final TypeInfo actualElementType = ((ArrayTypeInfo) actualType).getElementType();
            return canCallWith(dummyElementType, actualElementType);
        }

        // 仮引数が型パラメータ型の場合
        else if (dummyType instanceof TypeParameterTypeInfo) {

            // TODO 今のところ，条件なしでOKにしている．実装の必要あり
            return true;
        }

        assert false : "Here sholdn't be reached!";
        return true;
    }

    /**
     * 引数を追加する
     * 
     * @param parameter 追加する引数
     */
    public final void addParameter(final ParameterInfo parameter) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        this.parameters.add(parameter);
    }

    /**
     * 引数を追加する
     * 
     * @param parameters 追加する引数
     */
    public final void addParameters(final List<ParameterInfo> parameters) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new IllegalArgumentException();
        }

        this.parameters.addAll(parameters);
    }

    /**
     * このメソッドの引数の List を返す．
     * 
     * @return このメソッドの引数の List
     */
    public final List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * このメソッドの引数の数を返す
     * 
     * @return このメソッドの引数の数
     */
    public final int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * 引数で指定された型パラメータを追加する
     * 
     * @param typeParameter 追加する型パラメータ
     */
    @Override
    public final void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * 指定されたインデックスの型パラメータを返す
     * 
     * @param index 型パラメータのインデックス
     * @return　指定されたインデックスの型パラメータ
     */
    @Override
    public final TypeParameterInfo getTypeParameter(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * 型パラメータの List を返す．
     * 
     * @return このクラスの型パラメータの List
     */
    @Override
    public final List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * 引数で与えられた型パラメータがこのユニットで定義されたものであるかを返す
     * 
     * @param typeParameter
     * @return
     */
    public final boolean isDefined(final TypeParameterInfo typeParameter) {

        final List<TypeParameterInfo> typeParameters = this.getTypeParameters();
        if (typeParameters.contains(typeParameter)) {
            return true;
        }

        return false;
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass;
    }

    /**
     * 引数で指定された例外を追加する
     * 
     * @param thrownException 追加する例外
     */
    public final void addThrownException(final ReferenceTypeInfo thrownException) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == thrownException) {
            throw new IllegalArgumentException();
        }

        this.thrownExceptions.add(thrownException);
    }

    /**
     * スローされる例外の List を返す．
     * 
     * @return スローされる例外の List
     */
    public final List<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableList(this.thrownExceptions);
    }

    /**
     * この呼び出しユニット内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しを追加する． プラグインから呼ぶとランタイムエラー．
     * 
     * @param entityUsage 名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出し
     */
    public void addUnresolvedUsage(final UnresolvedExpressionInfo<?> entityUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(entityUsage);
    }

    /**
     * この呼び出しユニット内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しの Set を返す．
     * 
     * @return このメソッド内で，名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しの Set
     */
    public Set<UnresolvedExpressionInfo<?>> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    @Override
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    @Override
    public final boolean isInheritanceVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isInheritanceVisible(this.modifiers);
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    @Override
    public final boolean isNamespaceVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isNamespaceVisible(this.modifiers);
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    @Override
    public final boolean isPublicVisible() {
        final ClassInfo ownerClass = this.getOwnerClass();
        return ownerClass.isInterface() ? true : ModifierInfo.isPublicVisible(this.modifiers);
    }

    /**
     * このメソッドを呼び出しているメソッドまたはコンストラクタを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param caller 追加する呼び出すメソッド
     */
    public final void addCaller(final CallableUnitInfo caller) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caller) {
            return;
        }

        this.callers.add(caller);
    }

    /**
     * このメソッドを呼び出しているメソッドまたはコンストラクタの SortedSet を返す．
     * 
     * @return このメソッドを呼び出しているメソッドの SortedSet
     */
    public final SortedSet<CallableUnitInfo> getCallers() {
        return Collections.unmodifiableSortedSet(this.callers);
    }

    /**
     * 外部クラスのコンストラクタ、メソッドの位置情報に入れるダミーの値をかえす 
     */
    protected final static int getDummyPosition() {
        return dummyPosition--;
    }

    /**
     * このCallableUnitInfoのシグネチャのテキスト表現を返す
     * 
     * @return このCallableUnitInfoのシグネチャのテキスト表現
     */
    public abstract String getSignatureText();

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * 型パラメータを保存する変数
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * スローされる例外を保存する変数
     */
    private final List<ReferenceTypeInfo> thrownExceptions;

    /**
     * 引数のリストの保存するための変数
     */
    private final List<ParameterInfo> parameters;

    /**
     * このメソッドを呼び出しているメソッド一覧を保存するための変数
     */
    private final SortedSet<CallableUnitInfo> callers;

    /**
     * 名前解決できなかったクラス参照，フィールド参照・代入，メソッド呼び出しなどを保存するための変数
     */
    private final transient Set<UnresolvedExpressionInfo<?>> unresolvedUsage;

    /**
     * 外部クラスのコンストラクタ、メソッドの位置情報に入れるダミーの値。
     */
    private static int dummyPosition = -1;
}
