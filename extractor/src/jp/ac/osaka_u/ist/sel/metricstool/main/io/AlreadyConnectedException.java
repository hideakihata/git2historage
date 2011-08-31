package jp.ac.osaka_u.ist.sel.metricstool.main.io;



/**
 * 重複が許可されていない接続を二重に張ろうとした時に発生する.
 * 具体的には，進捗報告用の接続を同じプラグインから２つ作ろうとした場合などである.
 * @author kou-tngt
 *
 */
public class AlreadyConnectedException extends ConnectionException {

    /**
     * 
     */
    private static final long serialVersionUID = 7388323759983838646L;

    public AlreadyConnectedException() {
        super();
    }

    public AlreadyConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyConnectedException(String message) {
        super(message);
    }

    public AlreadyConnectedException(Throwable cause) {
        super(cause);
    }

}
