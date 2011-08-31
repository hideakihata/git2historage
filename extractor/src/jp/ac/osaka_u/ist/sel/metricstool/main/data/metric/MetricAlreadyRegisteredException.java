package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * 既に登録しているメトリクス情報を再度登録しようとした場合に，スローされる例外
 * 
 * @author higo 
 */
public class MetricAlreadyRegisteredException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -763049740565557645L;

    /**
     * 
     */
    public MetricAlreadyRegisteredException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public MetricAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public MetricAlreadyRegisteredException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public MetricAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

}
