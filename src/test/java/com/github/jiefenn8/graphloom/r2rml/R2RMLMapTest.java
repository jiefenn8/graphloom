package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.configmap.AbstractConfigMapTest;
import com.github.jiefenn8.graphloom.configmap.ConfigMap;

/**
 * Tests for R2RMLMap code using {@link AbstractConfigMapTest} test cases.
 * Currently skeletal till further iteration of the project or feature.
 */
public class R2RMLMapTest extends AbstractConfigMapTest {
    @Override
    public ConfigMap createInstance() {
        return new R2RMLMap();
    }
}
