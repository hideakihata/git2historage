package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class JavaUnresolvedExternalFieldInfo {

    public JavaUnresolvedExternalFieldInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.type = null;
        this.modifiers = new HashSet<String>();
    }

    public void setName(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public void setType(final String type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new IllegalArgumentException();
        }

        this.type = type;
    }

    public void addModifier(final String modifier) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new IllegalArgumentException();
        }

        this.modifiers.add(modifier);
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public Set<String> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    private String name;

    private String type;

    private final Set<String> modifiers;
}
