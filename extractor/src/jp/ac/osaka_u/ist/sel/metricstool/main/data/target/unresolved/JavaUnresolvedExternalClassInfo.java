package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * バイトコードを解析した状態での未解決ライブラリクラスを格納するための情報．
 * 現在は，対象言語がJavaのみのため，このような簡単な実装になっているが，
 * 他の言語に対してもライブラリクラスを作成する場合は，
 * 共通の抽象クラスを作成するなど，アーキテクチャの変更が必要．
 * 
 * @author higo
 *
 */
public class JavaUnresolvedExternalClassInfo {

    public JavaUnresolvedExternalClassInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.superTypes = new ArrayList<String>();
        this.methods = new HashSet<JavaUnresolvedExternalMethodInfo>();
        this.fields = new HashSet<JavaUnresolvedExternalFieldInfo>();
        this.modifiers = new HashSet<String>();
        this.typeParameters = new LinkedList<String>();
        this.inner = false;
        this.anonymous = false;
    }

    public void setName(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public void isInterface(final boolean isInterface) {
        this.isInterface = isInterface;
    }

    public void addSuperType(final String superType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superType) {
            throw new IllegalArgumentException();
        }

        this.superTypes.add(superType);
    }

    public void addMethod(final JavaUnresolvedExternalMethodInfo method) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == method) {
            throw new IllegalArgumentException();
        }

        this.methods.add(method);
    }

    public void addField(final JavaUnresolvedExternalFieldInfo field) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == field) {
            throw new IllegalArgumentException();
        }

        this.fields.add(field);
    }

    public void addModifier(final String modifier) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new IllegalArgumentException();
        }

        this.modifiers.add(modifier);
    }

    public void addTypeParameter(final String typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new IllegalArgumentException();
        }

        this.typeParameters.add(typeParameter);
    }

    public void setInner(final boolean inner) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.inner = inner;
    }

    public void setAnonymous(final boolean anonymous) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.anonymous = anonymous;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getSuperTypes() {
        return Collections.unmodifiableList(this.superTypes);
    }

    public Set<JavaUnresolvedExternalMethodInfo> getMethods() {
        return Collections.unmodifiableSet(this.methods);
    }

    public Set<JavaUnresolvedExternalFieldInfo> getFields() {
        return Collections.unmodifiableSet(this.fields);
    }

    public Set<String> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    public List<String> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof JavaUnresolvedExternalClassInfo) {
            return this.getName().equals(((JavaUnresolvedExternalClassInfo) o).getName());
        }

        return false;
    }

    public boolean isInterface() {
        return this.isInterface;
    }

    public boolean isInner() {
        return this.inner;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }

    private String name;

    private boolean isInterface;

    private final List<String> superTypes;

    private final Set<JavaUnresolvedExternalMethodInfo> methods;

    private final Set<JavaUnresolvedExternalFieldInfo> fields;

    private final Set<String> modifiers;

    private final List<String> typeParameters;

    private boolean inner;

    private boolean anonymous;
}
