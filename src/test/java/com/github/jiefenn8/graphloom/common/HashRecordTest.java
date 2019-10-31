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

package com.github.jiefenn8.graphloom.common;

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

    @Test
    public void WhenRecordValueExist_TheReturnTrue() {
        boolean result = record.hasValue("VALUE");
        assertThat(result, is(true));
    }

    @Test
    public void WhenRecordValueNotExist_ThenReturnFalse() {
        boolean result = record.hasValue("OTHER");
        assertThat(result, is(false));
    }

    @Test
    public void WhenRecordPropertyExist_ThenReturnTrue() {
        boolean result = record.hasProperty("PROPERTY");
        assertThat(result, is(true));
    }

    @Test
    public void WhenRecordKeyNoExist_ThenReturnFalse() {
        boolean result = record.hasProperty("OTHER");
        assertThat(result, is(false));
    }

    @Test
    public void WhenAddNewProperty_ThenShouldReturnNull() {
        String value = "VALUE2";
        String result = record.addProperty("PROPERTY2", value);
        assertThat(result, nullValue());
    }

    @Test
    public void WhenAddNewPropertyValue_ThenShouldReturnPreviousValue() {
        String expected = "VALUE";
        String value = "VALUE2";
        String result = record.addProperty("PROPERTY", value);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void WhenRemovePropertyValue_TheShouldReturnValue() {
        String propertyValue = "VALUE";
        String result = record.removeProperty("PROPERTY");
        assertThat(result, equalTo(propertyValue));
    }

    //equals()

    @Test
    public void WhenRecordCompareAreEqual_ThenReturnTrue() {
        HashRecord record2 = new HashRecord("PROPERTY", "VALUE");
        boolean result = record.equals(record2);
        assertThat(result, is(true));
    }

    @Test
    public void WhenDiffRecordCompareAreNotEqual_ThenReturnFalse() {
        HashRecord record2 = new HashRecord("PROPERTY", "VALUE2");
        boolean result = record.equals(record2);
        assertThat(result, is(false));
    }

    //hashCode()

    //Check hashcode generation on different property name.
    @Test
    public void WhenDiffRecordGenerateHashCode_ThenBothHashCodeNotMatch() {
        HashRecord record2 = new HashRecord("PROPERTY2", "VALUE");
        int result = record2.hashCode();
        assertThat(result, not(equalTo(record.hashCode())));
    }

    //Negation case of WhenDiffRecordGenerateHashCode_ThenBothHashCodeNotMatch() for verification.
    @Test
    public void WhenSameRecordGenerateHashCode_ThenBothHashCodeMatch() {
        HashRecord record2 = new HashRecord("PROPERTY", "VALUE");
        int result = record2.hashCode();
        assertThat(result, equalTo(record.hashCode()));
    }
}
