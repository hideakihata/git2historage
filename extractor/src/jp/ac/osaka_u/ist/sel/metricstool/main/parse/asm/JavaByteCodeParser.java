package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JavaPredefinedModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.JavaUnresolvedExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class JavaByteCodeParser implements ClassVisitor {

    public JavaByteCodeParser() {
        this.classInfo = new JavaUnresolvedExternalClassInfo();
    }

    @Override
    public void visit(final int version, final int access, final String name,
            final String signature, final String superName, final String[] interfaces) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == name) {
            throw new IllegalArgumentException();
        } else {
            this.classInfo.setName(name);
            final int index = name.lastIndexOf('$');
            if (0 <= index) {
                this.classInfo.setInner(true);
                this.classInfo.setAnonymous(isNumber(name.substring(index + 1)));
            } else {
                this.classInfo.setInner(false);
                this.classInfo.setAnonymous(false);
            }
        }

        this.classInfo.isInterface(0 != (access & Opcodes.ACC_INTERFACE));

        final String[] modifiers = this.getModifiers(access);
        for (final String modifier : modifiers) {
            this.classInfo.addModifier(modifier);
        }

        // signature がnullでないとき
        // つまり，型パラメータの使用があるとき
        if (null != signature) {
            final String[] typeParameters = this.getTypeParameters(signature);
            for (final String typeParameter : typeParameters) {
                this.classInfo.addTypeParameter(typeParameter);
            }

            final String[] superTypes = this.getSuperTypes(signature);
            for (final String superType : superTypes) {
                this.classInfo.addSuperType(superType);
            }
        }

        // signature がnullのとき
        // つまり，型パラメータの使用がないとき
        else {
            {
                final StringBuilder superType = new StringBuilder();
                superType.append("L");
                superType.append(superName);
                superType.append(";");
                this.classInfo.addSuperType(superType.toString());
            }

            for (final String interfaceName : interfaces) {
                final StringBuilder superType = new StringBuilder();
                superType.append("L");
                superType.append(interfaceName);
                superType.append(";");
                this.classInfo.addSuperType(superType.toString());
            }
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visitAttribute(final Attribute attr) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visitEnd() {
        // TODO Auto-generated method stub

    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc,
            final String signature, final Object value) {

        final JavaUnresolvedExternalFieldInfo field = new JavaUnresolvedExternalFieldInfo();
        if (null == name) {
            throw new IllegalArgumentException();
        } else {
            field.setName(name);
        }

        final String[] modifiers = this.getModifiers(access);
        for (final String modifier : modifiers) {
            field.addModifier(modifier);
        }

        if (null == desc) {
            throw new IllegalArgumentException();
        } else {
            field.setType(desc);
        }

        this.classInfo.addField(field);
        return null;
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName,
            final int value) {
        // 内部クラスを追加する処理が必要
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc,
            final String signature, final String[] exceptions) {

        final JavaUnresolvedExternalMethodInfo method = new JavaUnresolvedExternalMethodInfo();
        if (null == name) {
            throw new IllegalArgumentException();
        } else {
            method.setName(name);
        }

        final String[] modifiers = this.getModifiers(access);
        for (final String modifier : modifiers) {
            method.addModifier(modifier);
        }

        if (null != signature) {

            final String[] typeParameters = this.getTypeParameters(signature);
            for (final String typeParameter : typeParameters) {
                method.addTypeParameter(typeParameter);
            }

            final String[] parameters = this.getParameters(signature);
            for (final String parameter : parameters) {
                method.addArgumentType(parameter);
            }

            final String returnType = this.getReturnType(signature);
            method.setReturnType(returnType);

            final String[] thrownExceptions = this.getThrownExceptions(signature);
            for (final String thrownException : thrownExceptions) {
                method.addThrownException(thrownException);
            }

        } else {

            if (null == desc) {
                throw new IllegalArgumentException();
            }

            final String[] parameters = this.getParameters(desc);
            for (final String parameter : parameters) {
                method.addArgumentType(parameter);
            }

            final String returnType = this.getReturnType(desc);
            method.setReturnType(returnType);

            final String[] thrownExceptions = this.getThrownExceptions(desc);
            for (final String thrownException : thrownExceptions) {
                method.addThrownException(thrownException);
            }
        }

        this.classInfo.addMethod(method);
        return null;
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
    }

    @Override
    public void visitSource(final String source, final String debug) {
        // TODO Auto-generated method stub

    }

    public JavaUnresolvedExternalClassInfo getClassInfo() {

        if (null == this.classInfo.getName()) {
            throw new IllegalStateException();
        }

        return this.classInfo;
    }

    /**
     * 引数で与えられた修飾子の一覧に含まれる修飾子を返す
     * 
     * @param access
     * @return
     */
    private String[] getModifiers(final int access) {

        final List<String> modifiers = new LinkedList<String>();

        // ここから修飾子
        if (0 != (access & Opcodes.ACC_PUBLIC)) {
            modifiers.add(JavaPredefinedModifierInfo.PUBLIC_STRING);
        }

        if (0 != (access & Opcodes.ACC_PROTECTED)) {
            modifiers.add(JavaPredefinedModifierInfo.PROTECTED_STRING);
        }

        if (0 != (access & Opcodes.ACC_PRIVATE)) {
            modifiers.add(JavaPredefinedModifierInfo.PRIVATE_STRING);
        }

        if (0 != (access & Opcodes.ACC_FINAL)) {
            modifiers.add(JavaPredefinedModifierInfo.FINAL_STRING);
        }

        if (0 != (access & Opcodes.ACC_STATIC)) {
            modifiers.add(JavaPredefinedModifierInfo.STATIC_STRING);
        }

        if (0 != (access & Opcodes.ACC_ABSTRACT)) {
            modifiers.add(JavaPredefinedModifierInfo.ABSTRACT_STRING);
        }

        if (0 != (access & Opcodes.ACC_SYNCHRONIZED)) {
            modifiers.add(JavaPredefinedModifierInfo.SYNCHRONIZED_STRING);
        }

        return modifiers.toArray(new String[0]);
    }

    /**
     * 与えられたメソッドのsignatureに含まれる型パラメータを返す
     * 
     * @param signature
     * @return
     */
    private String[] getTypeParameters(final String signature) {

        if (null == signature) {
            throw new IllegalArgumentException();
        }

        if (!signature.startsWith("<")) {
            return new String[0];
        }

        if (-1 == signature.indexOf('>')) {
            throw new IllegalArgumentException();
        }

        // ジェネリクス情報を1つ１つ分解し，Setに入れる
        final List<String> typeParameters = new LinkedList<String>();
        int startIndex = 1;
        int endIndex = 0;
        for (int nestLevel = 0; endIndex < signature.length(); endIndex++) {

            if ('<' == signature.charAt(endIndex)) {
                nestLevel++;
            }

            else if ('>' == signature.charAt(endIndex)) {
                nestLevel--;
                if (0 == nestLevel) { // ジェネリクス部分が終わったのでループ処理を終了する
                    break;
                }
            }

            else if ((';' == signature.charAt(endIndex)) && (':' != signature.charAt(endIndex + 1))
                    && (1 == nestLevel)) {

                final String typeParameter = signature.substring(startIndex, endIndex + 1);
                typeParameters.add(typeParameter);
                startIndex = endIndex + 1;
            }
        }

        return typeParameters.toArray(new String[0]);
    }

    /**
     * 与えられたメソッドのsignatureに含まれる引数を返す
     * 
     * @param signature
     * @return
     */
    private String[] getParameters(final String signature) {

        if (null == signature) {
            throw new IllegalArgumentException();
        }

        final int openParenIndex = signature.indexOf('(');
        final int closeParenIndex = signature.indexOf(')');

        return JavaByteCodeUtility.separateTypes(signature.substring(openParenIndex + 1,
                closeParenIndex));
    }

    /**
     * 与えられたメソッドのsignatureに含まれる返り値を返す
     * 
     * @param signature
     * @return
     */
    private String getReturnType(final String signature) {

        if (null == signature) {
            throw new IllegalArgumentException();
        }

        final int closeParenIndex = signature.indexOf(')');
        final int exceptionIndex = signature.indexOf('^');
        return (-1 == exceptionIndex) ? signature.substring(closeParenIndex + 1) : signature
                .substring(closeParenIndex + 1, exceptionIndex);
    }

    /**
     * 与えられたメソッドのsignatureに含まれるスローされる例外を返す
     * 
     * @param signature
     * @return
     */
    private String[] getThrownExceptions(final String signature) {

        if (null == signature) {
            throw new IllegalArgumentException();
        }

        final int exceptionIndex = signature.indexOf('^');
        if (-1 == exceptionIndex) {
            return new String[0];
        }

        return JavaByteCodeUtility.separateTypes(signature.substring(exceptionIndex));
    }

    /**
     * 与えられたメソッドのsignatureに含まれるスーパータイプを返す
     * 
     * @param signature
     * @return
     */
    private String[] getSuperTypes(final String signature) {

        int startIndex = 0;
        if (signature.startsWith("<")) { // 型パラメータがある場合は読み飛ばす処理が必要
            for (int index = 0, nestLevel = 0; index < signature.length(); index++) {

                if ('<' == signature.charAt(index)) {
                    nestLevel++;
                }

                else if ('>' == signature.charAt(index)) {
                    nestLevel--;
                    if (0 == nestLevel) {
                        startIndex = index + 1;
                        break;
                    }
                }
            }
        }

        final List<String> superTypes = new ArrayList<String>();
        for (int index = startIndex, nestLevel = 0; index < signature.length(); index++) {

            if ('<' == signature.charAt(index)) {
                nestLevel++;
            }

            else if ('>' == signature.charAt(index)) {
                nestLevel--;
            }

            else if ((';' == signature.charAt(index)) && (0 == nestLevel)) {
                final String superType = signature.substring(startIndex, index + 1);
                superTypes.add(superType);
                startIndex = index + 1;
            }
        }

        return superTypes.toArray(new String[0]);
    }

    private boolean isNumber(final String text) {
        for (int index = 0; index < text.length(); index++) {
            if (!Character.isDigit(text.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    private final JavaUnresolvedExternalClassInfo classInfo;
}
