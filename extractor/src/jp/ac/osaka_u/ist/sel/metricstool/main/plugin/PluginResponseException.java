package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


/**
 * プラグインからの応答が指定時間以内に帰ってこなかった場合にスローされる.
 * @author kou-tngt
 *
 */
public class PluginResponseException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 9203607800507437330L;

    public PluginResponseException() {
        super();
    }

    public PluginResponseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PluginResponseException(final String message) {
        super(message);
    }

    public PluginResponseException(final Throwable cause) {
        super(cause);
    }

}
