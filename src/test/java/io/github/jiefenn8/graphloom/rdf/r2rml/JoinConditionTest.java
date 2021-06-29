/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JoinConditionTest {

    private JoinCondition joinCondition;

    public List<String[]> invalidJoinConditionParams() {
        return List.of(
                new String[]{null, null},
                new String[]{"PARENT", null},
                new String[]{null, "CHILD"}
        );
    }

    @Test
    @Parameters(method = "invalidJoinConditionParams")
    public void GivenInvalidCtorParams_WhenCreateInstance_ThenThrowException(String parent, String child) {
        Assert.assertThrows(
                NullPointerException.class,
                () -> new JoinCondition(parent, child)
        );
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
