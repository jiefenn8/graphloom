/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

/**
 * Unit test class for {@link MutableEntityRecord}.
 */
public class MutableEntityRecordTest {

    private MutableEntityRecord entityRecord;

    //Default setup with have one record inserted into the SUT instance.
    @Before
    public void setUp() {
        //SUT instance creation
        entityRecord = new MutableEntityRecord();

        //Default mock behaviour setup
        Record record = new MutableRecord("PROPERTY", "VALUE");
        entityRecord.addRecord(record);
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
        Record record = new MutableRecord("PROPERTY", "VALUE");
        boolean result = entityRecord.containsRecord(record);
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewRecord_WhenSearchRecord_ThenReturnFalse() {
        Record record = new MutableRecord("PROPERTY2", "VALUE");
        boolean result = entityRecord.containsRecord(record);
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
        Record record = new MutableRecord("PROPERTY", "VALUE");
        boolean result = entityRecord.removeRecord(record);
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewRecord_WhenRemoveRecord_ThenReturnFalse() {
        Record record = new MutableRecord("PROPERTY", "VALUE2");
        boolean result = entityRecord.removeRecord(record);
        assertThat(result, is(false));
    }

    //addRecord

    @Test
    public void GivenNewRecordWithSameProperties_WhenAddRecord_ThenReturnTrue() {
        Record record = new MutableRecord("PROPERTY", "VALUE2");
        boolean result = entityRecord.addRecord(record);
        assertThat(result, is(true));
    }

    @Test
    public void GivenNewRecordWithSamePropertiesAndValues_ThenReturnFalse() {
        Record record = new MutableRecord("PROPERTY", "VALUE");
        boolean result = entityRecord.addRecord(record);
        assertThat(result, is(false));
    }

    @Test
    public void GivenNewRecordWithDiffProperties_WhenAddRecord_ThenReturnFalse() {
        Record record = new MutableRecord("PROPERTY2", "VALUE");
        boolean result = entityRecord.addRecord(record);
        assertThat(result, is(false));
    }

    //isEmpty

    @Test
    public void GivenEntityRecordIsEmpty_WhenCheckIsEmpty_ThenReturnTrue() {
        entityRecord = new MutableEntityRecord(); //Override default
        boolean result = entityRecord.isEmpty();
        assertThat(result, is(true));
    }

    @Test
    public void GivenRecordHasRecord_WhenCheckIsEmpty_ThenReturnFalse() {
        boolean result = entityRecord.isEmpty();
        assertThat(result, is(false));
    }
}