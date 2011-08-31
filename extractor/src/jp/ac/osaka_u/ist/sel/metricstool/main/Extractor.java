package jp.ac.osaka_u.ist.sel.metricstool.main;

import java.util.LinkedHashSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CommentInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
//import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetAnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;

/**
 * 
 * @author hata
 * 
 */
public class Extractor extends MetricsTool {

	public static void main(String[] args) {

        // error情報表示用のリスナを作成
        final MessageListener errListener = new MessageListener() {
            public void messageReceived(MessageEvent event) {
                System.out.print("[MASU] " + event.getSource().getMessageSourceName() + " > "
                        + event.getMessage());
            }
        };
        MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(errListener);

        boolean goodFile = false;
        LinkedHashSet<String> javas = new LinkedHashSet<String>();
        javas.add("java15");
        javas.add("java14");
        javas.add("java13");
        for (String java : javas) {
            // 解析用設定
            Settings.getInstance().setLanguage(java);
            Settings.getInstance().setVerbose(false);
            Settings.getInstance().setStatement(false);
            Settings.getInstance().addTargetDirectory(args[0]);

            // Javaファイルを登録し，解析
            final Extractor extractor = new Extractor();
            extractor.analyzeLibraries();
            extractor.readTargetFiles();
            extractor.analyzeTargetFiles();

            if (DataManager.getInstance().getTargetFileManager().getFiles().first().isCorrectSyntax()) {
                goodFile = true;
                break;
            }
            else {
                System.out.println("[MASU] extractor > java version mismatch (" + java + ")");
                DataManager.clear();
            }
        }
        if (! goodFile) {
            System.out.println("[MASU] extractor > not valid java file");
            System.exit(0);
        }

        /// コメントの処理
        /// 1:CM 2,3:line 4:file
        for (final FileInfo fle : DataManager
        		.getInstance().getFileInfoManager().getFileInfos()) {

        	for (final CommentInfo fi : fle.getComments()) {
                System.out.print("CM@@");
        		System.out.print(fi.getFromLine() + "@@");
        		System.out.print(fi.getToLine() + "@@");
        		System.out.println(fle.getName());
        	}
        }

        /// クラスの処理
        /// 1:CL,IN 2,3:line 4:file 5:class 6:access [7:inner]
        for (final TargetClassInfo cls : DataManager
                .getInstance().getClassInfoManager().getTargetClassInfos()) {

            if (cls instanceof TargetAnonymousClassInfo)
                continue;

            if (cls.isClass()) {
                System.out.print("CL@@");
            }
            else {
                System.out.print("IN@@");
            }
            System.out.print(cls.getFromLine() + "@@");
            System.out.print(cls.getToLine() + "@@");
            System.out.print(cls.getOwnerFile().getName() + "@@");
            System.out.print(cls.getClassName() + "@@");
            
            /// modifier確認
            for(ModifierInfo mi : cls.getModifiers()) {
                if (mi.getName().startsWith("p"))
                    System.out.print(mi.getName());
            }
            System.out.print("@@");

            final StringBuilder text = new StringBuilder();
            for (final InnerClassInfo ic : cls.getInnerClasses()) {
                /// 無名クラス無視
                if (ic instanceof TargetAnonymousClassInfo)
                    continue;

                text.append(((ClassInfo) ic).getClassName() + "@");
            }
            System.out.println(text.toString());
        }

        /// フィールドの処理
        /// 1:FE 2,3:line 4:file 5:class 6:access 7:field_name 8:field_type
        for (final TargetFieldInfo fie : DataManager
                .getInstance().getFieldInfoManager().getTargetFieldInfos()) {

            if (fie.getOwnerClass() instanceof TargetAnonymousClassInfo)
                continue;

            System.out.print("FE@@");
            System.out.print(fie.getFromLine() + "@@");
            System.out.print(fie.getToLine() + "@@");
            System.out.print(((TargetClassInfo) fie.getOwnerClass()).getOwnerFile().getName() + "@@");
            System.out.print(fie.getOwnerClass().getClassName() + "@@");

            /// modifier確認
            for(ModifierInfo mi : fie.getModifiers()) {
                if (mi.getName().startsWith("p"))
                    System.out.print(mi.getName());
            }
            System.out.print("@@");

            System.out.print(fie.getName() + "@@");

            System.out.println(fie.getType().getTypeName());
        }

        /// コンストラクタの処理
        /// 1:CN 2,3:line 4:file 5:class 6:access 7:constructor_signature
        for (final TargetConstructorInfo cstr : DataManager
        		.getInstance().getMethodInfoManager().getTargetConstructorInfos()) {

            if (cstr.getOwnerClass() instanceof TargetAnonymousClassInfo)
                continue;

            //// デフォルトコンストラクタは0となる？
        	if (cstr.getFromLine() == 0)
        		continue;

        	System.out.print("CN@@");
        	System.out.print(cstr.getFromLine() + "@@");
        	System.out.print(cstr.getToLine() + "@@");
        	System.out.print(((TargetClassInfo) cstr.getOwnerClass()).getOwnerFile().getName() + "@@");
            System.out.print(cstr.getOwnerClass().getClassName() + "@@");
			
            /// modifier確認
            for(ModifierInfo mi : cstr.getModifiers()) {
                if (mi.getName().startsWith("p"))
                    System.out.print(mi.getName());
            }
            System.out.print("@@");

        	final StringBuilder text = new StringBuilder();
        	text.append(cstr.getOwnerClass().getClassName());
        	text.append("(");
        	for (final ParameterInfo parameter : cstr.getParameters()) {
        	    text.append(parameter.getType().getTypeName());
        	    text.append(",");
        	}
        	if (0 < cstr.getParameterNumber()) {
        	    text.deleteCharAt(text.length() - 1);
        	}
        	text.append(")");
        	System.out.println(text.toString());
        }

        /// メソッドの処理
        /// 1:MT 2,3:line 4:file 5:class 6:access 7:method_signature (8:return_type)
        for (final TargetMethodInfo mth : DataManager
        		.getInstance().getMethodInfoManager().getTargetMethodInfos()) {

            if (mth.getOwnerClass() instanceof TargetAnonymousClassInfo)
                continue;

            System.out.print("MT@@");
            System.out.print(mth.getFromLine() + "@@");
            System.out.print(mth.getToLine() + "@@");
            System.out.print(((TargetClassInfo) mth.getOwnerClass()).getOwnerFile().getName() + "@@");
            System.out.print(mth.getOwnerClass().getClassName() + "@@");

            /// modifier確認
        	for(ModifierInfo mi : mth.getModifiers()) {
    			if (mi.getName().startsWith("p"))
    			    System.out.print(mi.getName());
    		}
        	System.out.print("@@");

        	final StringBuilder text = new StringBuilder();
        	text.append(mth.getMethodName());
        	text.append("(");
        	for (final ParameterInfo parameter : mth.getParameters()) {
        	    text.append(parameter.getType().getTypeName());
        	    text.append(",");
        	}
        	if (0 < mth.getParameterNumber()) {
        	    text.deleteCharAt(text.length() - 1);
        	}
        	text.append(")");
        	System.out.println(text.toString());

            //System.out.println("@@" + mth.getReturnType().getTypeName());
        }
	}
}
