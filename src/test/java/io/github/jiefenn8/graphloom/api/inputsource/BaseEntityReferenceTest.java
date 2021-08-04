/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.api.inputsource;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test class for {@link BaseEntityReference}.
 */
@RunWith(JUnitParamsRunner.class)
public class BaseEntityReferenceTest {

    private static final String payload = "PAYLOAD";
    private static final String iteratorDef = "";
    private BaseEntityReference sourceConfig;

    public List<EntityReference.PayloadType> payloadTypeParameters() {
        return List.of(EntityReference.DefaultType.UNDEFINED);
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenNullPayload_WhenCreateInstance_ThenThrowException(EntityReference.PayloadType type) {
        String expected = "Payload must not be null.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> new BaseEntityReference(null, type, iteratorDef)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void GivenNullPayloadType_WhenCreateInstance_ThenThrowException() {
        String expected = "Payload type must not be null.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> new BaseEntityReference(payload, null, iteratorDef)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenNullIteratorDef_WhenCreateInstance_ThenThrowException(EntityReference.PayloadType type) {
        String expected = "Iterator definition must not be null.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> new BaseEntityReference(payload, type, null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPayloadType_WhenGetPayloadType_ThenReturnDatabaseType(EntityReference.PayloadType type) {
        sourceConfig = new BaseEntityReference(payload, type, iteratorDef);
        EntityReference.PayloadType result = sourceConfig.getPayloadType();
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPayload_WhenGetPayload_ThenReturnString(EntityReference.PayloadType type) {
        sourceConfig = new BaseEntityReference(payload, type, iteratorDef);
        String result = sourceConfig.getPayload();
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenIteratorDef_WhenGetIteratorDef_ThenReturnString(EntityReference.PayloadType type) {
        sourceConfig = new BaseEntityReference(payload, type, iteratorDef);
        String result = sourceConfig.getIteratorDef();
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPropertyWithValue_WhenGetProperty_ThenReturnString(EntityReference.PayloadType type) {
        sourceConfig = new BaseEntityReference(payload, type, iteratorDef);
        sourceConfig.setProperty("PROPERTY", "VALUE");
        String result = sourceConfig.getProperty("PROPERTY");
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPropertyWithNullValue_WhenGetProperty_ThenReturnNull(EntityReference.PayloadType type) {
        sourceConfig = new BaseEntityReference(payload, type, iteratorDef);
        sourceConfig.setProperty("PROPERTY", null);
        String result = sourceConfig.getProperty("PROPERTY");
        assertThat(result, is(nullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenProperty_WhenGetProperty_ThenReturnNull(EntityReference.PayloadType type) {
        sourceConfig = new BaseEntityReference(payload, type, iteratorDef);
        sourceConfig.setProperty("PROPERTY", null);
        String result = sourceConfig.getProperty("PROPERTY");
        assertThat(result, is(nullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenNullProperty_WhenSetProperty_ThenThrowException(EntityReference.PayloadType type) {
        Assert.assertThrows(
                NullPointerException.class,
                () -> {
                    sourceConfig = new BaseEntityReference(payload, type, iteratorDef);
                    sourceConfig.setProperty(null, null);
                }
        );
    }
}
