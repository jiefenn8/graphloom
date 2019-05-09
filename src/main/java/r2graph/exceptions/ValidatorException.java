package r2graph.exceptions;

import r2graph.exceptions.base.FeijoaException;

/**
 * Exception to throw for validator operation.
 */
public class ValidatorException extends FeijoaException {
    public ValidatorException() {
        super();
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
