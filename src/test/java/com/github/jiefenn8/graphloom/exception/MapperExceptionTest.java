/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.exception;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for {@link MapperException}.
 */
public class MapperExceptionTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void GivenMessage_WhenThrow_ThenThrowWithMessage() {
        String expectedMessage = "Exception message";
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage(expectedMessage);

        throw new MapperException(expectedMessage);
    }

    @Test
    public void GivenMessageWithCause_WhenThrow_ThenThrowWithMessageAndCause() {
        String expectedMessage = "Exception message";
        Throwable expectedCause = new NullPointerException();
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectCause(IsEqual.equalTo(expectedCause));
        exceptionRule.expectMessage(expectedMessage);

        throw new MapperException(expectedMessage, expectedCause);
    }
}
