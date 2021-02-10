/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityReference;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.google.common.collect.ImmutableSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link LogicalTable}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LogicalTableTest {

    @Rule public ExpectedException exceptionRule = ExpectedException.none();

    private LogicalTable logicalTable;
    @Mock private EntityReference mockEntityReference;

    @Test
    public void GivenNullEntityReference_WhenBuildInstance_ThenThrowException() {
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage("Payload must not be null.");
        logicalTable = new LogicalTable.Builder((EntityReference) null).build();
    }

    @Test
    public void GivenEntityReference_WhenGenerateHashCode_ThenReturnExpectedInt() {
        int expected = Objects.hash(mockEntityReference);

        logicalTable = new LogicalTable.Builder(mockEntityReference).build();
        int result = logicalTable.hashCode();
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void GivenEntityReference_WhenCreateBuilder_ThenReturnBuilder() {
        LogicalTable.Builder result = new LogicalTable.Builder(mockEntityReference);
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenEntityReference_WheGetEntityReference_ThenReturnEntityReference() {
        logicalTable = new LogicalTable.Builder(mock(EntityReference.class)).build();
        EntityReference result = logicalTable.getEntityReference();
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenTriplesMap_WhenBuildWithTriplesMap_ThenReturnBuilder() {
        LogicalTable.Builder result = new LogicalTable.Builder(mock(EntityReference.class))
                .withTriplesMap(mock(TriplesMap.class));
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenLogicalTableAndJoinConditions_WhenBuildWithJointSQLQuery_ThenReturnBuilder() {
        EntityReference mockEntityReference = mock(EntityReference.class);
        JoinCondition mockJoinCondition = mock(JoinCondition.class);
        LogicalTable logicalTable = new LogicalTable.Builder(mockEntityReference).build();
        when(mockJoinCondition.getChild()).thenReturn("CHILD");
        when(mockJoinCondition.getParent()).thenReturn("PARENT");
        when(mockEntityReference.getPayload()).thenReturn("PAYLOAD");

        LogicalTable.Builder result = new LogicalTable.Builder(mockEntityReference)
                .withJointQuery(logicalTable, ImmutableSet.of(mockJoinCondition));
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void GivenLogicalTableAndNoJoinConditions_WhenBuildWithJointSQLQuery_ThenThrowException() {
        exceptionRule.expect(NullPointerException.class);

        new LogicalTable.Builder(mock(EntityReference.class))
                .withJointQuery(mock(LogicalTable.class), null);
    }

    @Test
    public void GivenLogicalTableAndEmptyJoinConditions_WhenBuildWithJointSQLQuery_ThenReturnBuilder() {
        exceptionRule.expect(MapperException.class);
        exceptionRule.expectMessage("Expected JoinConditions with joint query creation.");

        new LogicalTable.Builder(mock(EntityReference.class))
                .withJointQuery(mock(LogicalTable.class), ImmutableSet.of());
    }
}