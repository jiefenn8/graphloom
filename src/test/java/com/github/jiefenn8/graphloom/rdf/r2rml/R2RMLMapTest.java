/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.EntityMap;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

public class R2RMLMapTest {

    private static final String rrPrefix = "rr";
    private R2RMLMap r2rmlMap;

    @Test
    public void GivenNoNamespace_WhenGetNamespaceMap_ThenReturnMap() {
        r2rmlMap = new R2RMLMap.Builder()
                .build();

        Map<String, String> result = r2rmlMap.getNamespaceMap();
        assertThat(result, notNullValue());
    }

    @Test
    public void GivenNamespace_WhenGetNamespaceMap_ThenReturnMapWithNamespace() {
        r2rmlMap = new R2RMLMap.Builder()
                .addNsPrefix("ex", "http://www.example.org/ns#")
                .build();

        Map<String, String> result = r2rmlMap.getNamespaceMap();
        assertThat(result, is(not(anEmptyMap())));
    }

    @Test
    public void GivenNoRRNamespace_WhenGetNamespaceMap_ThenReturnMapWithRRNamespace() {
        r2rmlMap = new R2RMLMap.Builder()
                .build();

        String expected = R2RMLSyntax.getURI();
        String result = r2rmlMap.getNamespaceMap().get(rrPrefix);
        assertThat(result, is(equalTo(expected)));
    }

    @Test
    public void GivenNoTriplesMap_WhenListEntityMaps_ThenReturnEmptyList() {
        r2rmlMap = new R2RMLMap.Builder()
                .addTriplesMap(mock(TriplesMap.class))
                .build();

        List<EntityMap> result = r2rmlMap.listEntityMaps();
        assertThat(result, is(not(empty())));
    }

    @Test
    public void GivenTriplesMap_WhenListEntityMaps_ThenReturnListWithEntityMap() {
        r2rmlMap = new R2RMLMap.Builder()
                .addTriplesMap(mock(TriplesMap.class))
                .build();

        List<EntityMap> result = r2rmlMap.listEntityMaps();
        assertThat(result, is(not(empty())));
    }
}