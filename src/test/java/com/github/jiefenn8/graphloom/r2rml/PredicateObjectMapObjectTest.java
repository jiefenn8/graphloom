package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.configmap.AbstractObjectMapTest;
import com.github.jiefenn8.graphloom.configmap.ObjectMap;

public class PredicateObjectMapObjectTest extends AbstractObjectMapTest {

    @Override
    public ObjectMap createInstance() {
        return new PredicateObjectMap();
    }
}