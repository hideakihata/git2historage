package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.sourcecode;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.MethodInfo;

/**
 * ソースコード表示用コンポーネント
 * 
 * @author higo
 * 
 */
public class SourceCodeView extends JPanel implements Observer {

	/**
	 * コンストラクタ
	 */
	public SourceCodeView() {
		this.map = new HashMap<MethodInfo, SourceCodePanel>();
	}

	/**
	 * オブザーバパターン用メソッド
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (o instanceof SelectedEntities) {

			final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
			if (selectedEntities.getLabel().equals(MethodInfo.METHOD)) {

				final Set<MethodInfo> methods = SelectedEntities
						.<MethodInfo> getInstance(MethodInfo.METHOD).get();

				// マップから新しく選択されていないメソッド群を削除
				final Set<MethodInfo> oldMethods = new HashSet<MethodInfo>(
						this.map.keySet());
				for (final MethodInfo oldMethod : oldMethods) {
					if (!methods.contains(oldMethod)) {
						this.map.remove(oldMethod);
					}
				}

				for (final MethodInfo newMethod : methods) {
					if (!this.map.containsKey(newMethod)) {
						final SourceCodePanel panel = new SourceCodePanel(
								newMethod);
						this.map.put(newMethod, panel);
						final CodeCloneInfo codeclone = newMethod
								.getCodeClone();
						panel.addHighlight(codeclone);
						panel.display(codeclone);
					}
				}

				// 全てクリア
				this.removeAll();

				// 新しく選択されているコードクローン群のパネルを表示
				this.setLayout(new GridLayout(1, this.map.size()));
				for (final SourceCodePanel panel : this.map.values()) {
					this.add(panel);
				}

				this.validate();
				this.repaint();
			}

			else {
				this.removeAll();
				this.map.clear();
				this.validate();
				this.repaint();
			}
		}

	}

	private final Map<MethodInfo, SourceCodePanel> map;
}
