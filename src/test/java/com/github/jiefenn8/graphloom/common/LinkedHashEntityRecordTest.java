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
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.Mockito.mock;

public class LinkedHashEntityRecordTest {

    private LinkedHashEntityRecord entityRecord;

    @Before
    public void setUp() {
        entityRecord = new LinkedHashEntityRecord();
        HashRecord mockRecord = mock(HashRecord.class);
        entityRecord.addRecord(mockRecord);
    }

    @Test
    public void WhenSizeExist_ThenReturnNonNegInt() {
        int result = entityRecord.size();
        assertThat(result, is(greaterThanOrEqualTo(0)));
    }

    @Test
    public void WhenContainsRecordMatch_ThenReturnTrue() {
        HashRecord record = mock(HashRecord.class);
        entityRecord.addRecord(record);
        boolean result = entityRecord.containsRecord(record);
        assertThat(result, is(true));
    }

    @Test
    public void WhenContainsRecordFail_ThenReturnFalse() {
        HashRecord mockRecord = mock(HashRecord.class);
        boolean result = entityRecord.containsRecord(mockRecord);
        assertThat(result, is(false));
    }

    @Test
    public void WhenIteratorExist_ThenReturnIterator() {
        Iterator result = entityRecord.iterator();
        assertThat(result, notNullValue());
    }

    @Test
    public void WhenRemoveRecordMatch_ThenReturnTrue() {
        HashRecord mockRecord = mock(HashRecord.class);
        entityRecord.addRecord(mockRecord);
        boolean result = entityRecord.removeRecord(mockRecord);
        assertThat(result, is(true));
    }

    @Test
    public void WhenRemoveRecordNoMatch_ThenReturnFalse() {
        HashRecord mockRecord = mock(HashRecord.class);
        boolean result = entityRecord.removeRecord(mockRecord);
        assertThat(result, is(false));
    }

    @Test
    public void WhenAddRecordSucceed_ThenReturnTrue() {
        boolean result = entityRecord.addRecord(mock(HashRecord.class));
        assertThat(result, is(true));
    }

    @Test
    public void WhenNoRecordExist_ThenReturnTrue() {
        entityRecord.clear();
        boolean result = entityRecord.isEmpty();
        assertThat(result, is(true));
    }

    @Test
    public void WhenRecordExist_ThenReturnFalse() {
        entityRecord.addRecord(mock(HashRecord.class));
        boolean result = entityRecord.isEmpty();
        assertThat(result, is(false));
    }
}