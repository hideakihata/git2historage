package jp.ac.osaka_u.ist.sel.metricstool.main.plugin.loader;


/**
 * この例外は，プラグインのディレクトリ構成がプラグインの規則に従っていない場合に投げられる．
 * 具体的には，plugin.xmlがプラグインのディレクトリ直下に存在しない場合などである．
 * 
 * @author kou-tngt
 */
public class IllegalPluginDirectoryStructureException extends PluginLoadException {

    /**
     * 
     */
    private static final long serialVersionUID = 8896937178866661003L;

    public IllegalPluginDirectoryStructureException() {
        super();
    }

    public IllegalPluginDirectoryStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPluginDirectoryStructureException(String message) {
        super(message);
    }

    public IllegalPluginDirectoryStructureException(Throwable cause) {
        super(cause);
    }

}
