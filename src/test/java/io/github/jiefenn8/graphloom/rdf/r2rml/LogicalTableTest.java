/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.EntityReference;
import io.github.jiefenn8.graphloom.exceptions.MapperException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Objects;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link LogicalTable}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LogicalTableTest {

    private LogicalTable logicalTable;
    @Mock private EntityReference mockEntityReference;

    @Test
    public void GivenNullEntityReference_WhenCreateCtor_ThenThrowException() {
        String expected = "Payload must not be null.";
        Throwable throwable = Assert.assertThrows(
                NullPointerException.class,
                () -> new LogicalTable.Builder((EntityReference) null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
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
        LogicalTable.Builder result = new LogicalTable.Builder(mock(EntityReference.class));
        result.withTriplesMap(mock(TriplesMap.class));
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

        LogicalTable.Builder result = new LogicalTable.Builder(mockEntityReference);
        result.withJointQuery(logicalTable, Set.of(mockJoinCondition));
        assertThat(result, is(notNullValue()));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void GivenLogicalTableAndNoJoinConditions_WhenBuildWithJointSQLQuery_ThenThrowException() {
        Assert.assertThrows(
                NullPointerException.class,
                () -> {
                    LogicalTable.Builder builder = new LogicalTable.Builder(mock(EntityReference.class));
                    builder.withJointQuery(mock(LogicalTable.class), null);
                }
        );
    }

    @Test
    public void GivenLogicalTableAndEmptyJoinConditions_WhenBuildWithJointSQLQuery_ThenReturnBuilder() {
        String expected = "Expected JoinConditions with joint query creation.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> {
                    LogicalTable.Builder builder = new LogicalTable.Builder(mock(EntityReference.class));
                    builder.withJointQuery(mock(LogicalTable.class), Set.of());
                }
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }
}