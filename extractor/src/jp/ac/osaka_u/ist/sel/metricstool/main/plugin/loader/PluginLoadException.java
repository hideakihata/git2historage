package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


/**
 * 
 * この例外はプラグインのロードを試み，失敗した時に投げられる．
 * 
 * @author kou-tngt
 */
public class PluginLoadException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 6849558492623155760L;

    public PluginLoadException() {
        super();
    }

    public PluginLoadException(String message) {
        super(message);
    }

    public PluginLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginLoadException(Throwable cause) {
        super(cause);
    }
}
