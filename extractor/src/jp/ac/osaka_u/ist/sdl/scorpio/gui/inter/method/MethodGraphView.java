package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.method;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jp.ac.osaka_u.ist.sdl.scorpio.ScorpioGUI;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodCallInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodController;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodInfo;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class MethodGraphView extends JPanel implements Observer {

	/**
	 * オブザーバパターン用メソッド
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(CodeCloneInfo.CODECLONE)) {

				if (null != this.viewer) {
					this.remove(this.viewer);
					this.validate();
					this.repaint();
				}

				if (SelectedEntities.<CodeCloneInfo> getInstance(
						CodeCloneInfo.CODECLONE).isSet()) {

					final CodeCloneInfo codeclone = SelectedEntities
							.<CodeCloneInfo> getInstance(
									CodeCloneInfo.CODECLONE).get().first();
					final SortedSet<MethodInfo> methods = codeclone
							.getOwnerMethods();
					final Graph<MethodInfo, MethodCallInfo> graph = new DirectedSparseGraph<MethodInfo, MethodCallInfo>();
					for (final MethodInfo method : methods) {
						graph.addVertex(method);
					}

					final SortedSet<MethodCallInfo> calls = codeclone
							.getCalls();
					for (final MethodCallInfo call : calls) {
						final int callerID = call.getCallerID();
						final MethodInfo caller = MethodController.getInstance(
								ScorpioGUI.ID).getMethod(callerID);
						assert null != caller : "caller is null!";

						final int calleeID = call.getCalleeID();
						final MethodInfo callee = MethodController.getInstance(
								ScorpioGUI.ID).getMethod(calleeID);
						assert null != callee : "callee is null!";
						graph.addEdge(call, caller, callee);
					}

					final Layout<MethodInfo, MethodCallInfo> layout = new CircleLayout<MethodInfo, MethodCallInfo>(
							graph);
					layout.setSize(new Dimension(this.getWidth(), this
							.getHeight()));

					this.viewer = new VisualizationViewer<MethodInfo, MethodCallInfo>(
							layout);
					this.setGraphMode();

					this.viewer.getRenderContext().setVertexLabelTransformer(
							new ToStringLabeller<MethodInfo>());
					this.viewer.getRenderContext().setEdgeLabelTransformer(
							new ToStringLabeller<MethodCallInfo>());

					this.viewer.addMouseListener(new MouseListener() {

						@Override
						public void mouseReleased(MouseEvent e) {
							final Set<MethodInfo> methods = MethodGraphView.this.viewer
									.getPickedVertexState().getPicked();

							// コードクローンの情報をセット
							for (final MethodInfo method : methods) {
								method.setCodeClone(codeclone);
							}

							SelectedEntities.<MethodInfo> getInstance(
									MethodInfo.METHOD).setAll(methods,
									MethodGraphView.this);
						}

						@Override
						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub

						}
					});

					this.add(this.viewer, BorderLayout.CENTER);
					this.validate();
					this.repaint();
				}
			}
		}
	}

	public MethodGraphView() {
		this.transformingButton = new JRadioButton("TRANSFORMING");
		this.pickingButton = new JRadioButton("PICKING");
		final ButtonGroup group = new ButtonGroup();
		group.add(this.transformingButton);
		group.add(this.pickingButton);
		this.pickingButton.setSelected(true);
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.transformingButton);
		buttonPanel.add(this.pickingButton);
		this.setLayout(new BorderLayout());
		this.add(buttonPanel, BorderLayout.NORTH);
		this.viewer = null;

		this.transformingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MethodGraphView.this.setGraphMode();
			}
		});

		this.pickingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MethodGraphView.this.setGraphMode();
			}
		});
	}

	private void setGraphMode() {
		if (this.transformingButton.isSelected()) {
			if (null != MethodGraphView.this.viewer) {
				DefaultModalGraphMouse<MethodInfo, MethodCallInfo> gm = new DefaultModalGraphMouse<MethodInfo, MethodCallInfo>();
				gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
				MethodGraphView.this.viewer.setGraphMouse(gm);
			}
		} else if (this.pickingButton.isSelected()) {
			if (null != MethodGraphView.this.viewer) {
				DefaultModalGraphMouse<MethodInfo, MethodCallInfo> gm = new DefaultModalGraphMouse<MethodInfo, MethodCallInfo>();
				gm.setMode(ModalGraphMouse.Mode.PICKING);
				MethodGraphView.this.viewer.setGraphMouse(gm);
			}
		}
	}

	private VisualizationViewer<MethodInfo, MethodCallInfo> viewer;

	private final JRadioButton transformingButton;

	private final JRadioButton pickingButton;

}
