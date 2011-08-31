package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


/**
 * 
 * この例外は，プラグインをロードするためのクラスローダが生成できない場合や，
 * プラグインクラスのロードに失敗した場合に投げられる．
 * 
 * @author kou-tngt
 */
public class PluginClassLoadException extends PluginLoadException {

    /**
     * 
     */
    private static final long serialVersionUID = -996290505390023096L;

    public PluginClassLoadException() {
        super();
    }

    public PluginClassLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginClassLoadException(String message) {
        super(message);
    }

    public PluginClassLoadException(Throwable cause) {
        super(cause);
    }

}
