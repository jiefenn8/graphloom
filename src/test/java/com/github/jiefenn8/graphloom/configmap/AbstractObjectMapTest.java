package com.github.jiefenn8.graphloom.configmap;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for ObjectMap code. Currently skeletal till further
 * iteration of the project or feature.
 */
public abstract class AbstractObjectMapTest {

    private final String objectSource = "ENAME";
    private ObjectMap objectMap;

    public abstract ObjectMap createInstance();

    @Before
    public void setUp() throws Exception {
        objectMap = createInstance();
    }

    /**
     * Tests that the ObjectMap instance returns a valid object source
     * String set by {@code ObjectMap}.
     */
    @Test
    public void WhenObjectSourceExists_ShouldReturnValue() {
        objectMap.setObjectSource(objectSource);
        String result = objectMap.getObjectSource();
        assertThat(result, is(objectSource));
    }
}
