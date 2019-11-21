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

import com.github.jiefenn8.graphloom.api.HashRecord;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class HashRecordTest {

    private HashRecord record;

    @Before
    public void setUp() {
        record = new HashRecord("PROPERTY", "VALUE");
    }

    //addProperty

    @Test
    public void GivenNewProperty_WhenAddNewProperty_ThenReturnNull() {
        String value = "VALUE2";
        String result = record.addProperty("PROPERTY2", value);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void GivenExistingProperty_WhenAddNewProperty_ThenReturnPreviousValue() {
        String expected = "VALUE";
        String value = "VALUE2";
        String result = record.addProperty("PROPERTY", value);
        assertThat(result, is(equalTo(expected)));
    }

    //hasProperty

    @Test
    public void GivenExistingProperty_WhenSearchProperty_ThenReturnTrue() {
        boolean result = record.hasProperty("PROPERTY");
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewProperty_WhenSearchProperty_ThenReturnFalse() {
        boolean result = record.hasProperty("OTHER");
        assertThat(result, is(false));
    }

    //hasValue

    @Test
    public void GivenExistingValue_WhenSearchValue_TheReturnTrue() {
        boolean result = record.hasValue("VALUE");
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewValue_WhenSearchValue_ThenReturnFalse() {
        boolean result = record.hasValue("OTHER");
        assertThat(result, is(false));
    }

    //getProperty

    @Test
    public void GivenExistingProperty_WhenRetrieveProperty_ThenReturnValue() {
        String expectedValue = "VALUE";
        String result = record.getPropertyValue("PROPERTY");
        assertThat(result, is(equalTo(expectedValue)));
    }

    @Test
    public void GivenNewProperty_WhenRetrieveProperty_ThenReturnNull() {
        String result = record.getPropertyValue("PROPERTY2");
        assertThat(result, is(nullValue()));
    }

    //removeProperty

    @Test
    public void GivenExistingProperty_WhenRemoveProperty_ThenReturnPreviousValue() {
        String propertyValue = "VALUE";
        String result = record.removeProperty("PROPERTY");
        assertThat(result, is(equalTo(propertyValue)));
    }

    @Test
    public void GivenNewProperty_WhenRemoveProperty_ThenReturnNull() {
        String result = record.removeProperty("PROPERTY2");
        assertThat(result, is(nullValue()));
    }

    //equals()

    @Test
    public void GivenSameRecord_WhenCompareRecord_ThenReturnTrue() {
        HashRecord record2 = new HashRecord("PROPERTY", "VALUE");
        boolean result = record.equals(record2);
        assertThat(result, is(true));
    }

    @Test
    public void GivenDiffRecord_WhenCompareRecord_ThenReturnFalse() {
        HashRecord record2 = new HashRecord("PROPERTY", "VALUE2");
        boolean result = record.equals(record2);
        assertThat(result, is(false));
    }

    //hashCode()

    //Check hashcode generation on different property name.
    @Test
    public void WhenTwoDiffRecordGenerateHashCode_ThenBothHashCodeNotMatch() {
        HashRecord record2 = new HashRecord("PROPERTY2", "VALUE");
        int result = record2.hashCode();
        assertThat(result, is(not(equalTo(record.hashCode()))));
    }

    //Negation case of WhenTwoDiffRecordGenerateHashCode_ThenBothHashCodeNotMatch() for verification.
    @Test
    public void WhenTwoSameRecordGenerateHashCode_ThenBothHashCodeMatch() {
        HashRecord record2 = new HashRecord("PROPERTY", "VALUE");
        int result = record2.hashCode();
        assertThat(result, is(equalTo(record.hashCode())));
    }
}
