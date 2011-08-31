package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


/**
 * 登録されていないメトリクス情報にアクセスした場合にスローされる例外
 * 
 * @author higo
 *
 */
public class MetricNotRegisteredException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5771740792340202250L;

    /**
     * 
     */
    public MetricNotRegisteredException() {
        super();
    }

    /**
     * 
     * @param message
     * @param cause
     */
    public MetricNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param message
     */
    public MetricNotRegisteredException(String message) {
        super(message);
    }

    /**
     * 
     * @param cause
     */
    public MetricNotRegisteredException(Throwable cause) {
        super(cause);
    }

}
