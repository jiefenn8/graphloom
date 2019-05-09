package r2graph.exceptions.base;

import r2graph.exceptions.base.FeijoaException;

/**
 * Exception to throw for validator operation.
 */
public class FeijoaValidatorException extends RuntimeException {
    public FeijoaValidatorException() {
        super();
    }

    public FeijoaValidatorException(String message) {
        super(message);
    }

    public FeijoaValidatorException(Throwable cause) {
        super(cause);
    }

    public FeijoaValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
