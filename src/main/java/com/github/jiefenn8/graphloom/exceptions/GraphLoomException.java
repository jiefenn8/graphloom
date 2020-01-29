/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.exceptions;

/**
 * Superclass of exceptions arising from GraphLoom related code, extending
 * from {@link RuntimeException}.
 */
public class GraphLoomException extends RuntimeException {

    private static final long serialVersionUID = 8393174563699723669L;

    /**
     * Constructs a GraphLoom exception with the specified detail message.
     *
     * @param message the message the exception will contain
     */
    public GraphLoomException(String message) {
        super(message);
    }

    /**
     * Constructs a GraphLoom exception with the specified cause and
     * message.
     *
     * @param message the message the exception with contain
     * @param cause   the cause of the exception
     */
    public GraphLoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
