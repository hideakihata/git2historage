package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class JavaUnresolvedExternalMethodInfo {

    public JavaUnresolvedExternalMethodInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.returnType = null;
        this.argumentTypes = new LinkedList<String>();
        this.modifiers = new HashSet<String>();
        this.typeParameters = new LinkedList<String>();
        this.thrownExceptions = new LinkedList<String>();
    }

    public void setName(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public void setReturnType(final String returnType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new IllegalArgumentException();
        }

        this.returnType = returnType;
    }

    public void addArgumentType(final String argumentType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == argumentType) {
            throw new IllegalArgumentException();
        }

        this.argumentTypes.add(argumentType);
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

    public void addThrownException(final String thrownException) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == thrownException) {
            throw new IllegalArgumentException();
        }

        this.thrownExceptions.add(thrownException);
    }

    public String getName() {
        return this.name;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public List<String> getArgumentTypes() {
        return Collections.unmodifiableList(this.argumentTypes);
    }

    public Set<String> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    public List<String> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    public List<String> getThrownExceptions() {
        return Collections.unmodifiableList(this.thrownExceptions);
    }

    private String name;

    private String returnType;

    private final List<String> argumentTypes;

    private final Set<String> modifiers;

    private final List<String> typeParameters;

    private final List<String> thrownExceptions;
}
