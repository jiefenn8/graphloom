/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.exception;

import com.github.jiefenn8.graphloom.exceptions.ParserException;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for {@link ParserException}.
 */
public class ParserExceptionTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void GivenMessage_WhenThrow_ThenThrowWithMessage() {
        String expectedMessage = "Exception message";
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expectedMessage);

        throw new ParserException(expectedMessage);
    }

    @Test
    public void GivenMessageWithArgs_WhenThrow_ThenThrowWithMessageWithArgs() {
        String expectedMessage = "Exception message %s";
        String args = "with args";
        String expectedFormatMessage = String.format(expectedMessage, args);
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectMessage(expectedFormatMessage);

        throw new ParserException(expectedMessage, args);
    }

    @Test
    public void GivenMessageWithCause_WhenThrow_ThenThrowWithMessageAndCause() {
        String expectedMessage = "Exception message";
        Throwable expectedCause = new NullPointerException();
        exceptionRule.expect(ParserException.class);
        exceptionRule.expectCause(IsEqual.equalTo(expectedCause));
        exceptionRule.expectMessage(expectedMessage);

        throw new ParserException(expectedMessage, expectedCause);
    }
}
