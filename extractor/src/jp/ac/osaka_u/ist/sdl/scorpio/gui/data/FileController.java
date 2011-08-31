package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * GUIでファイルを管理するためのクラス
 * 
 * @author higo
 */
public class FileController {

    private FileController() {
        this.files = new TreeMap<Integer, FileInfo>();
    }

    /**
     * ファイルを追加する
     * 
     * @param file 追加するファイル
     */
    public void add(final FileInfo file) {

        if (null == file) {
            throw new IllegalArgumentException();
        }

        this.files.put(file.getID(), file);
    }

    /**
     * IDで指定されたファイルを返す
     * 
     * @param id ファイルのID
     * @return IDで指定されたファイル
     */
    public FileInfo getFile(final int id) {
        return this.files.get(id);
    }

    /**
     * ファイルのCollectionを返す
     * 
     * @return ファイルのCollection
     */
    public Collection<FileInfo> getFiles() {
        return Collections.unmodifiableCollection(this.files.values());
    }

    /**
     * ファイルの数を返す
     * 
     * @return　ファイルの数
     */
    public int getNumberOfFiles() {
        return this.files.size();
    }

    /**
     * ファイルをすべて消す
     */
    public void clear() {
        this.files.clear();
    }

    private final SortedMap<Integer, FileInfo> files;

    /**
     * 指定されたFileControllerを返す
     * 
     * @param id
     * @return
     */
    public static FileController getInstance(final String id) {

        FileController controller = map.get(id);
        if (null == controller) {
            controller = new FileController();
            map.put(id, controller);
        }

        return controller;
    }

    private static final Map<String, FileController> map = new HashMap<String, FileController>();
}
