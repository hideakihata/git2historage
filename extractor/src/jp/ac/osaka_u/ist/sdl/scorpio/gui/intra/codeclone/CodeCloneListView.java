package jp.ac.osaka_u.ist.sdl.scorpio.gui.intra.codeclone;


import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.SelectedEntities;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;


/**
 * コードクローン一覧を表示するためのパネル
 * 
 * @author higo
 *
 */
public class CodeCloneListView extends JTable implements Observer {

    /**
     * コードクローンの選択を制御するためのクラス
     * 
     * @author higo
     *
     */
    class SelectionEventHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {

                final int[] selectedRow = CodeCloneListView.this.getSelectedRows();
                final SortedSet<CodeCloneInfo> selectedCodeFragments = new TreeSet<CodeCloneInfo>();
                for (int i = 0; i < selectedRow.length; i++) {

                    final int modelIndex = CodeCloneListView.this
                            .convertRowIndexToModel(selectedRow[i]);
                    final CodeCloneListViewModel model = (CodeCloneListViewModel) CodeCloneListView.this
                            .getModel();
                    final CodeCloneInfo codeFragment = model.getCodeClone(modelIndex);
                    selectedCodeFragments.add(codeFragment);
                }

                SelectedEntities.<CodeCloneInfo> getInstance(CodeCloneInfo.CODECLONE).setAll(
                        selectedCodeFragments, CodeCloneListView.this);
            }
        }
    }

    /**
     * コンストラクタ
     */
    public CodeCloneListView() {

        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.selectionEventHandler = new SelectionEventHandler();
        this.getSelectionModel().addListSelectionListener(this.selectionEventHandler);
    }

    /**
     * オブザーバパターン用メソッド
     */
    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof SelectedEntities) {

            final SelectedEntities<?> selectedEntities = (SelectedEntities<?>) o;
            if (selectedEntities.getLabel().equals(CloneSetInfo.CLONESET)) {

                final CloneSetInfo cloneSet = SelectedEntities.<CloneSetInfo> getInstance(
                        CloneSetInfo.CLONESET).get().first();

                final CodeCloneListViewModel model = new CodeCloneListViewModel(cloneSet);
                this.setModel(model);
                final RowSorter<CodeCloneListViewModel> sorter = new TableRowSorter<CodeCloneListViewModel>(
                        model);
                this.setRowSorter(sorter);                
                this.setRowSelectionInterval(0, 1); //あったほうが便利
            }
        }

    }

    final public JScrollPane scrollPane;

    final SelectionEventHandler selectionEventHandler;
}
