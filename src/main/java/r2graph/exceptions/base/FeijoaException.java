package r2graph.exceptions.base;

/**
 * Superclass of exceptions happening from R2Graph code.
 */
public class FeijoaException extends RuntimeException {

    public FeijoaException() {
        super();
    }

    public FeijoaException(String message) {
        super(message);
    }

    public FeijoaException(Throwable cause) {
        super(cause);
    }

    public FeijoaException(String message, Throwable cause) {
        super(message, cause);
    }
}
