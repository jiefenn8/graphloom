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

package com.github.jiefenn8.graphloom.api;

import com.google.common.collect.ImmutableList;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.List;

import static com.github.jiefenn8.graphloom.api.SourceConfig.*;
import static com.github.jiefenn8.graphloom.api.SourceConfig.PayloadType;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class InputSourceConfigTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private final String payload = "PAYLOAD";
    private final String iteratorDef = "";
    private InputSourceConfig sourceConfig;

    public List<PayloadType> payloadTypeParameters(){
        return ImmutableList.of(DefaultType.UNDEFINED);
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenNullPayload_WhenCreateInstance_ThenThrowException(PayloadType t){
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Payload must not be null.");
        sourceConfig = new InputSourceConfig(null, t, iteratorDef);
    }

    @Test
    public void GivenNullPayloadType_WhenCreateInstance_ThenThrowException(){
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Payload type must not be null.");
        sourceConfig = new InputSourceConfig(payload, null, iteratorDef);
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenNullIteratorDef_WhenCreateInstance_ThenThrowException(PayloadType t){
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Iterator definition must not be null.");
        sourceConfig = new InputSourceConfig(payload, t, null);
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPayloadType_WhenGetPayloadType_ThenReturnDatabaseType(PayloadType t){
        sourceConfig = new InputSourceConfig(payload, t, iteratorDef);
        PayloadType result = sourceConfig.getPayloadType();
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPayload_WhenGetPayload_ThenReturnString(PayloadType t){
        sourceConfig = new InputSourceConfig(payload, t, iteratorDef);
        String result = sourceConfig.getPayload();
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenIteratorDef_WhenGetIteratorDef_ThenReturnString(PayloadType t){
        sourceConfig = new InputSourceConfig(payload, t, iteratorDef);
        String result = sourceConfig.getIteratorDef();
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPropertyWithValue_WhenGetProperty_ThenReturnString(PayloadType t){
        sourceConfig = new InputSourceConfig(payload, t, iteratorDef);
        sourceConfig.setProperty("PROPERTY", "VALUE");
        String result = sourceConfig.getProperty("PROPERTY");
        assertThat(result, is(notNullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenPropertyWithNullValue_WhenGetProperty_ThenReturnNull(PayloadType t){
        sourceConfig = new InputSourceConfig(payload, t, iteratorDef);
        sourceConfig.setProperty("PROPERTY", null);
        String result = sourceConfig.getProperty("PROPERTY");
        assertThat(result, is(nullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenProperty_WhenGetProperty_ThenReturnNull(PayloadType t){
        sourceConfig = new InputSourceConfig(payload, t, iteratorDef);
        sourceConfig.setProperty("PROPERTY", null);
        String result = sourceConfig.getProperty("PROPERTY");
        assertThat(result, is(nullValue()));
    }

    @Test
    @Parameters(method = "payloadTypeParameters")
    public void GivenNullProperty_WhenSetProperty_ThenThrowException(PayloadType t){
        expectedException.expect(NullPointerException.class);
        sourceConfig = new InputSourceConfig(payload, t, iteratorDef);
        sourceConfig.setProperty(null, null);
    }
}
