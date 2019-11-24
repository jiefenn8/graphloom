/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MutableRecordTest {

    private MutableRecord record;

    @Before
    public void setUp() {
        record = new MutableRecord("PROPERTY", "VALUE");
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
        MutableRecord record2 = new MutableRecord("PROPERTY", "VALUE");
        boolean result = record.equals(record2);
        assertThat(result, is(true));
    }

    @Test
    public void GivenDiffRecord_WhenCompareRecord_ThenReturnFalse() {
        MutableRecord record2 = new MutableRecord("PROPERTY", "VALUE2");
        boolean result = record.equals(record2);
        assertThat(result, is(false));
    }

    //hashCode()

    //Check hashcode generation on different property name.
    @Test
    public void WhenTwoDiffRecordGenerateHashCode_ThenBothHashCodeNotMatch() {
        MutableRecord record2 = new MutableRecord("PROPERTY2", "VALUE");
        int result = record2.hashCode();
        assertThat(result, is(not(equalTo(record.hashCode()))));
    }

    //Negation case of WhenTwoDiffRecordGenerateHashCode_ThenBothHashCodeNotMatch() for verification.
    @Test
    public void WhenTwoSameRecordGenerateHashCode_ThenBothHashCodeMatch() {
        MutableRecord record2 = new MutableRecord("PROPERTY", "VALUE");
        int result = record2.hashCode();
        assertThat(result, is(equalTo(record.hashCode())));
    }
}
