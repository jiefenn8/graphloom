/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf;

import io.github.jiefenn8.graphloom.api.ConfigMaps;
import io.github.jiefenn8.graphloom.api.InputSource;
import io.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test class for {@link RDFMapper}.
 */
@RunWith(MockitoJUnitRunner.class)
public class RDFMapperTest {

    @Mock private ConfigMaps mockConfigMaps;
    private RDFMapper rdfMapper;

    @Before
    public void setUp() {
        rdfMapper = new RDFMapper();

        when(mockConfigMaps.getNamespaceMap()).thenReturn(Map.of());
        when(mockConfigMaps.getEntityMaps()).thenReturn(Set.of());
    }

    @Test
    public void WhenEmptyConfigGiven_ThenReturnEmptyGraph() {
        Model model = rdfMapper.mapToGraph(mock(InputSource.class), mockConfigMaps);
        boolean result = model.isEmpty();
        assertThat(result, is(true));
    }

    @Test
    public void WhenNoInputSourceGiven_ThenThrowException() {
        String expected = "Cannot retrieve source data from null input source.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> rdfMapper.mapToGraph(null, mockConfigMaps)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }

    @Test
    public void WhenNoConfigMapsGiven_ThenThrowException() {
        String expected = "Cannot map source from null config maps.";
        Throwable throwable = Assert.assertThrows(
                MapperException.class,
                () -> rdfMapper.mapToGraph(mock(InputSource.class), null)
        );
        String msg = throwable.getMessage();
        assertThat(msg, is(equalTo(expected)));
    }
}