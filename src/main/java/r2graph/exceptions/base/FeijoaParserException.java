package r2graph.exceptions.base;

import r2graph.exceptions.base.FeijoaException;

/**
 * Exception to throw for parser operation.
 */
public class FeijoaParserException extends RuntimeException {
    public FeijoaParserException() {
        super();
    }

    public FeijoaParserException(String message) {
        super(message);
    }

    public FeijoaParserException(Throwable cause) {
        super(cause);
    }

    public FeijoaParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
