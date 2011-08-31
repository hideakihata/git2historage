package jp.ac.osaka_u.ist.sel.metricstool.main;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.csharp.CSharpAntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java13AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java14AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.Java15AntlrAstTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.java.JavaAstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitorManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr.AntlrAstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFile;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CSharpLexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CSharpParser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CommonASTWithLineNumber;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java13Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java13Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java14Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java14Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Lexer;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.Java15Parser;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.MasuAstFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import antlr.ASTFactory;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;


/**
 * ASTをパースし，未解決クラス情報をの構築をマルチスレッドを行うためのクラス
 * 
 * @author higo
 *
 */
class TargetFileParser implements Runnable {

    TargetFileParser(final TargetFile[] files, final AtomicInteger index, final MessagePrinter out,
            final MessagePrinter err) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == files) || (null == index) || (null == out) || (null == err)) {
            throw new IllegalArgumentException();
        }

        this.files = files;
        this.index = index;
        this.out = out;
        this.err = err;
    }

    @Override
    public void run() {

        while (true) {

            final int i = this.index.getAndIncrement();
            if (!(i < this.files.length)) {
                break;
            }

            try {

                if (Settings.getInstance().isVerbose()) {
                    final StringBuilder text = new StringBuilder();
                    text.append("parsing ");
                    text.append(this.files[i].getName());
                    text.append(" [");
                    text.append(i + 1);
                    text.append("/");
                    text.append(this.files.length);
                    text.append("]");
                    this.out.println(text.toString());
                }

                final FileInfo fileInfo = new FileInfo(this.files[i].getName());
                DataManager.getInstance().getFileInfoManager()
                        .add(fileInfo, Thread.currentThread());

                final BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(this.files[i].getName()), "JISAutoDetect"));

                switch (Settings.getInstance().getLanguage()) {
                case JAVA15:
                    final Java15Lexer java15lexer = new Java15Lexer(reader);
                    java15lexer.setTabSize(1);
                    final Java15Parser java15parser = new Java15Parser(java15lexer);

                    final ASTFactory java15factory = new MasuAstFactory();
                    java15factory.setASTNodeClass(CommonASTWithLineNumber.class);

                    java15parser.setASTFactory(java15factory);

                    java15parser.compilationUnit();
                    this.files[i].setCorrectSytax(true);

                    AstVisitorManager<AST> java15VisitorManager = new JavaAstVisitorManager<AST>(
                            new AntlrAstVisitor(new Java15AntlrAstTranslator()), Settings
                                    .getInstance());
                    java15VisitorManager.visitStart(java15parser.getAST());

                    fileInfo.addAllComments(java15lexer.getCommentSet());
                    fileInfo.setLOC(java15lexer.getLine());

                    break;

                case JAVA14:
                    final Java14Lexer java14lexer = new Java14Lexer(reader);
                    java14lexer.setTabSize(1);
                    final Java14Parser java14parser = new Java14Parser(java14lexer);

                    final ASTFactory java14factory = new MasuAstFactory();
                    java14factory.setASTNodeClass(CommonASTWithLineNumber.class);

                    java14parser.setASTFactory(java14factory);

                    java14parser.compilationUnit();
                    this.files[i].setCorrectSytax(true);

                    AstVisitorManager<AST> java14VisitorManager = new JavaAstVisitorManager<AST>(
                            new AntlrAstVisitor(new Java14AntlrAstTranslator()), Settings
                                    .getInstance());
                    java14VisitorManager.visitStart(java14parser.getAST());

                    fileInfo.addAllComments(java14lexer.getCommentSet());
                    fileInfo.setLOC(java14lexer.getLine());

                    break;

                case JAVA13:
                    final Java13Lexer java13lexer = new Java13Lexer(reader);
                    java13lexer.setTabSize(1);
                    final Java13Parser java13parser = new Java13Parser(java13lexer);

                    final ASTFactory java13factory = new MasuAstFactory();
                    java13factory.setASTNodeClass(CommonASTWithLineNumber.class);

                    java13parser.setASTFactory(java13factory);

                    java13parser.compilationUnit();
                    this.files[i].setCorrectSytax(true);

                    AstVisitorManager<AST> java13VisitorManager = new JavaAstVisitorManager<AST>(
                            new AntlrAstVisitor(new Java13AntlrAstTranslator()), Settings
                                    .getInstance());
                    java13VisitorManager.visitStart(java13parser.getAST());

                    fileInfo.addAllComments(java13lexer.getCommentSet());
                    fileInfo.setLOC(java13lexer.getLine());
                    break;

                case CSHARP:
                    final CSharpLexer csharpLexer = new CSharpLexer(reader);
                    csharpLexer.setTabSize(1);
                    final CSharpParser csharpParser = new CSharpParser(csharpLexer);

                    final ASTFactory cshaprFactory = new MasuAstFactory();
                    cshaprFactory.setASTNodeClass(CommonASTWithLineNumber.class);

                    csharpParser.setASTFactory(cshaprFactory);

                    csharpParser.compilationUnit();
                    this.files[i].setCorrectSytax(true);

                    AstVisitorManager<AST> csharpVisitorManager = new JavaAstVisitorManager<AST>(
                            new AntlrAstVisitor(new CSharpAntlrAstTranslator()), Settings
                                    .getInstance());
                    csharpVisitorManager.visitStart(csharpParser.getAST());

                    fileInfo.setLOC(csharpLexer.getLine());
                    break;

                default:
                    assert false : "here shouldn't be reached!";
                }

                reader.close();

            } catch (FileNotFoundException e) {
                err.println(e.getMessage());
            } catch (RecognitionException e) {
                this.files[i].setCorrectSytax(false);
                err.println(e.getMessage());
                // TODO エラーが起こったことを TargetFileData などに通知する処理が必要
            } catch (TokenStreamException e) {
                this.files[i].setCorrectSytax(false);
                err.println(e.getMessage());
                // TODO エラーが起こったことを TargetFileData などに通知する処理が必要
            } catch (ASTParseException e) {
                err.println(e.getMessage());
            } catch (IOException e) {
                err.println(e.getMessage());
            }
        }

    }

    private final TargetFile[] files;

    private final AtomicInteger index;

    private final MessagePrinter out;

    private final MessagePrinter err;
}
