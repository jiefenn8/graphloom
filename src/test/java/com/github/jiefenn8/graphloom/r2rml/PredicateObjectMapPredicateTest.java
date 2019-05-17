package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.configmap.AbstractPredicateMapTest;
import com.github.jiefenn8.graphloom.configmap.PredicateMap;

public class PredicateObjectMapPredicateTest extends AbstractPredicateMapTest {

    @Override
    public PredicateMap createInstance() {
        return new PredicateObjectMap();
    }
}