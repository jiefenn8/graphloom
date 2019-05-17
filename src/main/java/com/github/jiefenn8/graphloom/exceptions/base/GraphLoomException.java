package com.github.jiefenn8.graphloom.exceptions.base;

/**
 * Superclass of exceptions happening from GraphLoom code.
 */
public class GraphLoomException extends RuntimeException {

    public GraphLoomException() {
        super();
    }

    public GraphLoomException(String message) {
        super(message);
    }

    public GraphLoomException(Throwable cause) {
        super(cause);
    }

    public GraphLoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
