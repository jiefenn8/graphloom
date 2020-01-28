/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.exceptions;

/**
 * Superclass of exceptions arising from GraphLoom.Mapper related code
 * extending from {@link GraphLoomException}.
 */
public class MapperException extends GraphLoomException {

    /**
     * Constructs a mapper exception with no message or cause.
     */
    public MapperException() {
        super();
    }

    /**
     * Constructs a mapper exception with the specified detail message.
     *
     * @param message the message the exception will contain
     */
    public MapperException(String message) {
        super(message);
    }

    /**
     * Constructs a mapper exception with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public MapperException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a mapper exception with the specified cause and
     * message.
     *
     * @param message the message the exception with contain
     * @param cause   the cause of the exception
     */
    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
