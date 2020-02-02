/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class JoinConditionTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private JoinCondition joinCondition;

    @Test
    public void GivenNorParentAndChild_WhenCreateInstance_ThenThrowException() {
        expectedException.expect(NullPointerException.class);

        new JoinCondition(null, null);
    }

    @Test
    public void GivenNoParentWithChild_WhenCreateInstance_ThenThrowException() {
        expectedException.expect(NullPointerException.class);

        new JoinCondition(null, "CHILD");
    }

    @Test
    public void GivenNoChildWithParent_WhenCreateInstance_ThenThrowException() {
        expectedException.expect(NullPointerException.class);

        new JoinCondition("PARENT", null);
    }

    @Test
    public void GivenChild_WhenGetChild_ThenReturnString() {
        joinCondition = new JoinCondition("PARENT", "CHILD");
        String result = joinCondition.getChild();
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenParent_WhenGetParent_ThenReturnString() {
        joinCondition = new JoinCondition("PARENT", "CHILD");
        String result = joinCondition.getParent();
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenEqualJoinConditions_WhenCompare_ThenReturnTrue() {
        String parentValue = "PARENT";
        String childValue = "CHILD";
        joinCondition = new JoinCondition(parentValue, childValue);
        JoinCondition value = new JoinCondition(parentValue, childValue);
        boolean result = joinCondition.equals(value);
        assertThat(result, is(true));
    }

    @Test
    public void GivenUnequalJoinConditions_WhenCompare_ThenReturnFalse() {
        joinCondition = new JoinCondition("PARENT", "CHILD");
        JoinCondition value = new JoinCondition("1", "2");
        boolean result = joinCondition.equals(value);
        assertThat(result, is(false));
    }

    @Test
    public void GivenParentAndChild_WhenGenerateHashCode_ThenReturnExpectedInt() {
        String parentValue = "PARENT";
        String childValue = "CHILD";
        int expected = Objects.hash(parentValue, childValue);

        joinCondition = new JoinCondition(parentValue, childValue);
        int result = joinCondition.hashCode();
        assertThat(result, is(equalTo(expected)));
    }

}
