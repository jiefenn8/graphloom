/*
 *    Copyright (c) 2019 - Javen Liu (github.com/jiefenn8)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.jiefenn8.graphloom.exceptions;

import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;

/**
 * Base tests for GraphLoom exception code. Currently skeletal till further
 * iteration of the project or feature.
 */
@RunWith(Parameterized.class)
public class BaseRuntimeExceptionTest {

    @Rule private ExpectedException exceptionRule = ExpectedException.none();
    private Class sutClass;

    public BaseRuntimeExceptionTest(Class sut) {
        sutClass = sut;
    }

    @Parameters(name = "{index}: {0}")
    public static Collection<Class<? extends GraphLoomException>> sutProvider() {
        return Arrays.asList(
                GraphLoomException.class,
                MapperException.class,
                ParserException.class);
    }

    @Test
    public void WhenThrowException_ShouldCatchExpected() throws Throwable {
        exceptionRule.expect(sutClass);
        Constructor<?> ctor = sutClass.getConstructor();
        Throwable object = (Throwable) ctor.newInstance();
        throw object;
    }

    @Test
    public void WhenThrowExceptionMessage_ShouldCatchExpected() throws Throwable {
        String expected = "message_1";
        exceptionRule.expect(sutClass);
        exceptionRule.expectMessage(expected);
        Constructor<?> ctor = sutClass.getConstructor(String.class);
        Throwable object = (Throwable) ctor.newInstance(new Object[]{expected});
        throw object;
    }

    @Test
    public void WhenThrowExceptionCause_ShouldCatchExpected() throws Throwable {
        Throwable expected = new IllegalStateException();
        exceptionRule.expect(GraphLoomException.class);
        exceptionRule.expectCause(IsEqual.equalTo(expected));
        Constructor<?> ctor = sutClass.getConstructor(Throwable.class);
        Throwable object = (Throwable) ctor.newInstance(new Object[]{expected});
        throw object;
    }

    @Test
    public void WhenThrowExceptionMessageCause_ShouldCatchExpected() throws Throwable {
        Throwable expected = new IllegalStateException();
        String expectedMessage = "message_1";
        exceptionRule.expect(GraphLoomException.class);
        exceptionRule.expectCause(IsEqual.equalTo(expected));
        exceptionRule.expectMessage(expectedMessage);
        Constructor<?> ctor = sutClass.getConstructor(String.class, Throwable.class);
        Throwable object = (Throwable) ctor.newInstance(new Object[]{expectedMessage, expected});
        throw object;
    }
}