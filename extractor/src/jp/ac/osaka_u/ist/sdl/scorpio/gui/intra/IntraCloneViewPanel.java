package jp.ac.osaka_u.ist.sdl.scorpio.gui.intra;

import java.awt.BorderLayout;
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
import jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.cloneset.CloneSetListView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.codeclone.CodeCloneListView;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.sourcecode.SourceCodeView;

/**
 * GUIのメインウィンドウ
 * 
 * @author higo
 * 
 */
public class IntraCloneViewPanel extends JFrame {

	public IntraCloneViewPanel() {

		this.setTitle("ScorpioGUI for Intraprocedural Code Clones");
		
		final CloneSetListView clonesetListView = new CloneSetListView(
				CodeCloneController.getInstance(ScorpioGUI.ID).getCloneSets());
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(clonesetListView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(clonesetListView);
		clonesetListView.scrollPane.setBorder(new TitledBorder(new LineBorder(
				java.awt.Color.black), "Clone Set List"));

		final CodeCloneListView codecloneListView = new CodeCloneListView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(codecloneListView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(codecloneListView);
		codecloneListView.scrollPane.setBorder(new TitledBorder(new LineBorder(
				java.awt.Color.black), "Code Clone List"));

		final SourceCodeView sourceCodeView = new SourceCodeView();
		SelectedEntities.<CloneSetInfo> getInstance(CloneSetInfo.CLONESET)
				.addObserver(sourceCodeView);
		SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE)
				.addObserver(sourceCodeView);

		final JSplitPane westPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		westPanel.setTopComponent(clonesetListView.scrollPane);
		westPanel.setBottomComponent(codecloneListView.scrollPane);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(westPanel, BorderLayout.WEST);
		this.getContentPane().add(sourceCodeView, BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
