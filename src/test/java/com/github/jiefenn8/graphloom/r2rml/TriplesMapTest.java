package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.configmap.AbstractEntityMapTest;
import com.github.jiefenn8.graphloom.configmap.EntityMap;

/**
 * Tests for ConfigMapFactory code using {@link AbstractEntityMapTest} test cases.
 * Currently skeletal till further iteration of the project or feature.
 */
public class TriplesMapTest extends AbstractEntityMapTest {
    @Override
    public EntityMap createInstance() {
        return new TriplesMap();
    }
}
