/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityMap;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class R2RMLMapTest {

    @Mock private Map<String, String> mockNamespaceMap;
    private R2RMLMap r2rmlMap;

    @Test
    public void WhenNoNamespaceMapGiven_ThenReturnMap() {
        r2rmlMap = new R2RMLMap(mockNamespaceMap);
        Map<String, String> result = r2rmlMap.getNamespaceMap();
        assertThat(result, notNullValue());
    }

    @Test
    public void WhenNamespaceMapGiven_ThenReturnNonEmptyMap() {
        Map<String, String> namespaceMap = ImmutableMap.of("rr", "http://www.w3.org/ns/r2rml#");
        r2rmlMap = new R2RMLMap(namespaceMap);
        boolean result = r2rmlMap.getNamespaceMap().isEmpty();
        assertThat(result, is(false));
    }

    @Test
    public void WhenEntityMapGiven_ThenReturnNonEmptyList() {
        r2rmlMap = new R2RMLMap(mockNamespaceMap);
        r2rmlMap.addTriplesMap(mock(EntityMap.class));
        boolean result = r2rmlMap.listEntityMaps().isEmpty();
        assertThat(result, is(false));
    }
}