package com.github.jiefenn8.graphloom.configmap;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for PredicateMap code. Currently skeletal till further
 * iteration of the project or feature.
 */
public abstract class AbstractPredicateMapTest {

    private final String predicate = "http://example.com/ns#name";
    private PredicateMap predicateMap;

    public abstract PredicateMap createInstance();

    @Before
    public void setUp() throws Exception {
        predicateMap = createInstance();
    }

    /**
     * Tests that the PredicateMap instance returns a valid predicate String
     * set by {@code PredicateMap}.
     */
    @Test
    public void WhenPredicateExists_ShouldReturnValue() {
        predicateMap.setPredicate(predicate);
        String result = predicateMap.getPredicate();
        assertThat(result, is(predicate));
    }
}
