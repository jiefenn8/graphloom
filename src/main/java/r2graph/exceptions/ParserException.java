package r2graph.exceptions;

import r2graph.exceptions.base.FeijoaException;

/**
 * Exception to throw for parser operation.
 */
public class ParserException extends FeijoaException {
    public ParserException() {
        super();
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
