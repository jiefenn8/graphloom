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

import com.github.jiefenn8.graphloom.api.Record;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LinkedHashEntityRecordTest {

    private LinkedHashEntityRecord entityRecord;

    //Default setup with have one record inserted into the SUT instance.
    @Before
    public void setUp() {
        //SUT instance creation
        entityRecord = new LinkedHashEntityRecord();

        //Default mock behaviour setup
        HashRecord mockRecord = mock(HashRecord.class);
        entityRecord.addRecord(mockRecord);
        when(mockRecord.properties()).thenReturn(ImmutableSet.of("PROPERTY"));
    }

    //size

    @Test
    public void WhenRetrieveSize_ThenReturnNonNegativeNumber() {
        int result = entityRecord.size();
        assertThat(result, is(greaterThanOrEqualTo(0)));
    }

    //containsRecord

    @Test
    public void GivenExistingRecord_WhenSearchRecord_ThenReturnTrue() {
        HashRecord record = mock(HashRecord.class);
        entityRecord.addRecord(record);
        boolean result = entityRecord.containsRecord(record);
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewRecord_WhenSearchRecord_ThenReturnFalse() {
        HashRecord mockRecord = mock(HashRecord.class);
        boolean result = entityRecord.containsRecord(mockRecord);
        assertThat(result, is(false));
    }

    //iterator

    @Test
    public void WhenRetrieveIterator_ThenReturnIterator() {
        Iterator result = entityRecord.iterator();
        assertThat(result, notNullValue());
    }

    //removeRecord

    @Test
    public void GivenExistingRecord_WhenRemoveRecord_ThenReturnTrue() {
        HashRecord mockRecord = mock(HashRecord.class);
        entityRecord.addRecord(mockRecord);
        boolean result = entityRecord.removeRecord(mockRecord);
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewRecord_WhenRemoveRecord_ThenReturnFalse() {
        HashRecord mockRecord = mock(HashRecord.class);
        boolean result = entityRecord.removeRecord(mockRecord);
        assertThat(result, is(false));
    }

    //addRecord

    @Test
    public void GivenNewRecordWithSameProperties_WhenAddRecord_ThenReturnTrue() {
        boolean result = entityRecord.addRecord(mock(HashRecord.class));
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewRecordWithDiffProperties_WhenAddRecord_ThenReturnFalse() {
        Record mockRecord = mock(HashRecord.class);
        when(mockRecord.properties()).thenReturn(ImmutableSet.of("PROPERTY2"));
        boolean result = entityRecord.addRecord(mockRecord);
        assertThat(result, is(false));
    }

    //isEmpty

    @Test
    public void GivenEntityRecordIsEmpty_WhenCheckIsEmpty_ThenReturnTrue() {
        entityRecord = new LinkedHashEntityRecord();
        boolean result = entityRecord.isEmpty();
        assertThat(result, is(true));
    }

    @Test
    public void GivenRecordHasRecord_WhenCheckIsEmpty_ThenReturnFalse() {
        entityRecord.addRecord(mock(HashRecord.class));
        boolean result = entityRecord.isEmpty();
        assertThat(result, is(false));
    }
}