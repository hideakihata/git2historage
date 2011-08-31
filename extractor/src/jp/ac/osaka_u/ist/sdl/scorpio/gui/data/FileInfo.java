package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;


/**
 * 
 * GUIでファイル情報を表すためのクラス
 * 
 * @author higo
 *
 */
public class FileInfo implements Comparable<FileInfo> {

    /**
     * コンストラクタ
     * 
     * @param name ファイル名
     * @param id ファイルのID
     */
    public FileInfo(final String name, final int id, final int loc, final int numberOfPDGNodes) {
        this.name = name;
        this.id = id;
        this.loc = loc;
        this.numberOfPDGNodes = numberOfPDGNodes;
    }

    public FileInfo() {
    }

    /**
     * ファイル名を返す
     * 
     * @return ファイル名
     */
    public String getName() {
        return this.name;
    }

    /**
     * ファイル名を設定する
     * 
     * @param name 設定するファイル名
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * ファイルのIDを返す
     * 
     * @return　ファイルのID
     */
    public int getID() {
        return this.id;
    }

    /**
     * ファイルのIDを設定する
     * 
     * @param id 設定するID
     */
    public void setID(final int id) {
        this.id = id;
    }

    /**
     * ファイルの行数を返す
     * 
     * @return　ファイルの行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * ファイルの行数を設定する
     * 
     * @param loc 設定する行数
     */
    public void setLOC(final int loc) {
        this.loc = loc;
    }

    /**
     * PDGのノード数を返す
     * 
     * @return　PDGのノード数
     */
    public int getNumberOfPDGNodes() {
        return this.numberOfPDGNodes;
    }

    /**
     * PDGのノード数を設定する
     * 
     * @param numberOfPDGNodes 設定するPDGのノード数
     */
    public void setNumberOfPDGNodes(final int numberOfPDGNodes) {
        this.numberOfPDGNodes = numberOfPDGNodes;
    }

    /**
     * 比較関数
     */
    @Override
    public int compareTo(FileInfo o) {
        if (this.getID() < o.getID()) {
            return -1;
        } else if (this.getID() > o.getID()) {
            return 1;
        } else {
            return 0;
        }
    }

    private String name;

    private int id;

    private int loc;

    private int numberOfPDGNodes;
}
