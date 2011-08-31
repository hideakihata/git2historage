package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


import java.util.Map;
import java.util.WeakHashMap;


/**
 * AST上の各要素の開始行，開始列，終了行，終了列を管理するクラス.
 * 
 * @author kou-tngt
 *
 */
public class DefaultPositionManager implements PositionManager {

    /**
     * 引数keyの開始行を返す.
     * 登録されていない場合は0を返す.
     * @param key 開始行を取得したい要素
     */
    public int getStartLine(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getStartLine();
        } else {
            return 0;
        }
    }

    /**
     * 引数keyの開始列を返す.
     * 登録されていない場合は0を返す.
     * @param key 開始列を取得したい要素
     */
    public int getStartColumn(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getStartColumn();
        } else {
            return 0;
        }
    }

    /**
     * 引数keyの終了行を返す.
     * 登録されていない場合は0を返す.
     * @param key 終了行を取得したい要素
     */
    public int getEndLine(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getEndLine();
        } else {
            return 0;
        }
    }

    /**
     * 引数keyの終了列を返す.
     * 登録されていない場合は0を返す.
     * @param key 終了列を取得したい要素
     */
    public int getEndColumn(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getEndColumn();
        } else {
            return 0;
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setStartLine(java.lang.Object, int)
     */
    public void setStartLine(final Object key, final int line) {
        this.getLineColumn(key).setStartLine(line);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setStartColumn(java.lang.Object, int)
     */
    public void setStartColumn(final Object key, final int column) {
        this.getLineColumn(key).setStartLine(column);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setEndLine(java.lang.Object, int)
     */
    public void setEndLine(final Object key, final int line) {
        this.getLineColumn(key).setEndLine(line);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setEndColumn(java.lang.Object, int)
     */
    public void setEndColumn(final Object key, final int column) {
        this.getLineColumn(key).setEndLine(column);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setPosition(java.lang.Object, int, int, int, int)
     */
    public void setPosition(final Object key, final int startLine, final int startColumn,
            final int endLine, final int endColumn) {
        final Position position = this.getLineColumn(key);
        position.setStartLine(startLine);
        position.setStartColumn(startColumn);
        position.setEndLine(endLine);
        position.setEndColumn(endColumn);
    }

    /**
     * 引数keyの行番号情報を記録するインスタンスを返す.
     * すでにkeyに対応するインスタンスが作成されていればそれを返し，まだ作成されていなければ新たに作成して返す.
     * @param key 情報を記録するインスタンスを作成したい要素
     * @return 引数keyに対応する行番号情報記録インスタンス
     */
    private Position getLineColumn(final Object key) {
        if (null == key) {
            throw new NullPointerException("key is null");
        }

        if (this.dataMap.containsKey(key)) {
            return this.dataMap.get(key);
        } else {
            final Position newInstance = new Position();
            this.dataMap.put(key, newInstance);
            return newInstance;
        }
    }

    /**
     * 行と列の情報を記録する内部クラス
     * 
     * @author kou-tngt
     *
     */
    private static class Position {
        /**
         * 終了列を返す.
         * @return 終了列
         */
        public int getEndColumn() {
            return this.endColumn;
        }

        /**
         * 終了行を返す
         * @return　終了行
         */
        public int getEndLine() {
            return this.endLine;
        }

        /**
         * 開始列を返す
         * @return　開始列
         */
        public int getStartColumn() {
            return this.startColumn;
        }

        /**
         * 開始行を返す
         * @return　開始行
         */
        public int getStartLine() {
            return this.startLine;
        }

        /**
         * 終了列をセットする
         * @param endColumn　終了列
         */
        public void setEndColumn(final int endColumn) {
            this.endColumn = endColumn;
        }

        /**
         * 終了行をセットする
         * @param endLine　終了行
         */
        public void setEndLine(final int endLine) {
            this.endLine = endLine;
        }

        /**
         * 開始列をセットする
         * @param startColumn　開始列
         */
        public void setStartColumn(final int startColumn) {
            this.startColumn = startColumn;
        }

        /**
         * 開始行をセットする
         * @param startLine　開始行
         */
        public void setStartLine(final int startLine) {
            this.startLine = startLine;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return this.startLine + "." + this.startColumn + " - " + this.endLine + "."
                    + this.endColumn;
        }

        /**
         * 開始行
         */
        private int startLine;

        /**
         * 開始列
         */
        private int startColumn;

        /**
         * 終了行
         */
        private int endLine;

        /**
         * 終了列
         */
        private int endColumn;
    }

    private final Map<Object, Position> dataMap = new WeakHashMap<Object, Position>();
}
