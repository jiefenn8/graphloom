/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.exceptions;

import java.io.Serial;

/**
 * Superclass of exceptions arising from GraphLoom.Parser code.
 */
public class ParserException extends GraphLoomException {

    @Serial private static final long serialVersionUID = 2244226488195283431L;

    /**
     * Constructs a parser exception with the specified detail message.
     *
     * @param message the message the exception will contain
     */
    public ParserException(String message) {
        super(message);
    }

    /**
     * Constructs a parser exception with the specified message with
     * the arguments referenced by the format specifiers in the message
     * string.
     *
     * @param message the message the exception will contain
     * @param args    the arguments to append into the message
     */
    public ParserException(String message, Object... args) {
        super(String.format(message, args));
    }

    /**
     * Constructs a parser exception with the specified cause and
     * message.
     *
     * @param message the message the exception with contain
     * @param cause   the cause of the exception
     */
    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
