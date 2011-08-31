package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.IdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAnonymousClassStateManager.ANONYMOUSCLASS_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


public class JavaAnonymousClassBuilder extends CompoundDataBuilder<UnresolvedClassInfo> {

    public JavaAnonymousClassBuilder(final BuildDataManager buildManager) {
        this(buildManager, new IdentifierBuilder());
    }

    public JavaAnonymousClassBuilder(final BuildDataManager buildManager,
            final IdentifierBuilder identifierBuilder) {
        if (null == buildManager) {
            throw new NullPointerException("buildManager is null.");
        }

        if (null == identifierBuilder) {
            throw new NullPointerException("identifierBuilder is null");
        }

        this.buildDataManager = buildManager;
        this.identifierBuilder = identifierBuilder;

        this.identifierBuilder.deactivate();

        addStateManager(stateManager);

        addInnerBuilder(identifierBuilder);
    }

    @Override
    public void stateChanged(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        if (type.equals(ANONYMOUSCLASS_STATE.ENTER_ANONYMOUSCLASS)) {
            identifierBuilder.deactivate();
            startAnonymousClassDef(event.getTrigger());
        } else if (type.equals(ANONYMOUSCLASS_STATE.EXIT_ANONYMOUSCLASS)) {
            endAnonymousClassDef();
            identifierBuilder.activate();
        } else if (type.equals(ANONYMOUSCLASS_STATE.ENTER_INSTANTIATION)) {
            builtIdentifierCountStack.push(identifierBuilder.getBuiltDataCount());
            identifierBuilder.activate();
        } else if (type.equals(ANONYMOUSCLASS_STATE.EXIT_INSTANTIATION)) {
            final int builtIdentifierCount = identifierBuilder.getBuiltDataCount()
                    - builtIdentifierCountStack.pop();
            for (int i = 0; i < builtIdentifierCount; i++) {
                identifierBuilder.popLastBuiltData();
            }
            if (!stateManager.isInInstantiation()) {
                identifierBuilder.deactivate();
            }
        }
    }

    protected UnresolvedClassInfo buildAnonymousClass(final int builtIdentifierCount) {
        final UnresolvedClassInfo outer = buildDataManager.getCurrentClass();
        final int anonymousCount = buildDataManager.getAnonymousClassCount(outer);

        final FileInfo currentFile = DataManager.getInstance().getFileInfoManager().getCurrentFile(
                Thread.currentThread());
        assert null != currentFile : "Illegal state: the file information was not registered to FileInfoManager";

        final UnresolvedUnitInfo<? extends UnitInfo> currentUnit = this.buildDataManager
                .getCurrentUnit();
        final UnresolvedClassInfo anonymous = new UnresolvedClassInfo(currentFile, currentUnit);
        anonymous.setAnonymous(true);

        anonymous.setClassName(String.valueOf(anonymousCount));
        /*anonymous.setClassName(outer.getClassName() + JAVA_ANONYMOUSCLASS_NAME_MARKER
                + anonymousCount);*/

        String[] builtName = null;
        for (int i = 0; i < builtIdentifierCount; i++) {
            builtName = identifierBuilder.popLastBuiltData();
        }

        if (null != builtName) {
            assert (null != builtName && 0 < builtName.length) : "Illegal state: resolved super type name was empty.";

            final UnresolvedClassTypeInfo superType = new UnresolvedClassTypeInfo(
                    UnresolvedClassImportStatementInfo.getClassImportStatements(buildDataManager
                            .getAllAvaliableNames()), builtName);
            anonymous.addSuperClass(superType);
        }

        //anonymous.setInheritanceVisible(false);
        //anonymous.setPublicVisible(false);
        //anonymous.setNamespaceVisible(false);
        //anonymous.setPrivateVibible(true);
        //anonymous.setInstanceMember(true);

        return anonymous;
    }

    protected void regist(final UnresolvedClassInfo classInfo) {
        registBuiltData(classInfo);
        buildDataManager.startClassDefinition(classInfo);
    }

    protected void endAnonymousClassDef() {
        buildDataManager.endClassDefinition();
    }

    protected void startAnonymousClassDef(AstVisitEvent trigger) {
        final int builtIdentifierCount = identifierBuilder.getBuiltDataCount()
                - builtIdentifierCountStack.peek();

        UnresolvedClassInfo anonymousClass = buildAnonymousClass(builtIdentifierCount);
        anonymousClass.setFromLine(trigger.getStartLine());
        anonymousClass.setFromColumn(trigger.getStartColumn());
        anonymousClass.setToLine(trigger.getEndLine());
        anonymousClass.setToColumn(trigger.getEndColumn());
        regist(anonymousClass);
        buildDataManager.enterClassBlock();
    }

    public final static String JAVA_ANONYMOUSCLASS_NAME_MARKER = "$";

    private final Stack<Integer> builtIdentifierCountStack = new Stack<Integer>();

    private final JavaAnonymousClassStateManager stateManager = new JavaAnonymousClassStateManager();

    private final BuildDataManager buildDataManager;

    private final IdentifierBuilder identifierBuilder;
}
