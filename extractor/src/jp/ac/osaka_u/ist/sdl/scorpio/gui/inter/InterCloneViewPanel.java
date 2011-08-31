package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jp.ac.osaka_u.ist.sdl.scorpio.ScorpioGUI;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.cloneset.CloneSetListView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.codeclone.CodeCloneListView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.method.MethodGraphView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.sourcecode.SourceCodeView;

/**
 * GUIのメインウィンドウ
 * 
 * @author higo
 * 
 */
public class InterCloneViewPanel extends JFrame {

	public InterCloneViewPanel() {

		this.setTitle("ScorpioGUI for Interprocedural Code Clones");

		final CloneSetListView clonesetListView = new CloneSetListView(
				CodeCloneController.getInstance(ScorpioGUI.ID).getCloneSets());
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(clonesetListView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(clonesetListView);
		SelectedEntities.<MethodInfo> getInstance(MethodInfo.METHOD)
				.addObserver(clonesetListView);
		clonesetListView.scrollPane.setBorder(new TitledBorder(new LineBorder(
				Color.black), "Clone Set List"));

		final CodeCloneListView codecloneListView = new CodeCloneListView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(codecloneListView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(codecloneListView);
		SelectedEntities.<MethodInfo> getInstance(MethodInfo.METHOD)
				.addObserver(codecloneListView);
		codecloneListView.scrollPane.setBorder(new TitledBorder(new LineBorder(
				Color.black), "Code Clone List"));

		final MethodGraphView methodGraphView = new MethodGraphView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(methodGraphView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(methodGraphView);
		SelectedEntities.<MethodInfo> getInstance(MethodInfo.METHOD)
				.addObserver(methodGraphView);
		methodGraphView.setBorder(new TitledBorder(new LineBorder(Color.black),
				"Method Graph"));

		final SourceCodeView sourceCodeView = new SourceCodeView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(sourceCodeView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(sourceCodeView);
		SelectedEntities.<MethodInfo> getInstance(MethodInfo.METHOD)
				.addObserver(sourceCodeView);

		final JSplitPane westPanel1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		westPanel1.setTopComponent(codecloneListView.scrollPane);
		westPanel1.setBottomComponent(methodGraphView);

		final JSplitPane westPanel2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		westPanel2.setTopComponent(clonesetListView.scrollPane);
		westPanel2.setBottomComponent(westPanel1);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(westPanel2, BorderLayout.WEST);
		this.getContentPane().add(sourceCodeView, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
