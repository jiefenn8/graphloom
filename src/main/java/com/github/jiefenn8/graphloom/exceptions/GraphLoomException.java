/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.exceptions;

/**
 * Superclass of exceptions arising from GraphLoom code.
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
