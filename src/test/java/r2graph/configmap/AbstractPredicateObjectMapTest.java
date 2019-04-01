package r2graph.configmap;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for ConfigMapFactory code. Currently skeletal till further
 * iteration of the project or feature.
 */
public abstract class AbstractPredicateObjectMapTest {

    private final String predicate = "http://example.com/ns#name";
    private final String objectSource = "ENAME";
    private PredicateObjectMap predicateObjectMap;

    public abstract PredicateObjectMap createInstance();

    @Before
    public void setUp() throws Exception {
        predicateObjectMap = createInstance();
    }

    /**
     * Tests that the PredicateObjectMap instance returns a valid predicate String
     * set by {@code PredicateObjectMap} interface.
     */
    @Test
    public void WhenPredicateExists_ShouldReturnValue() {
        predicateObjectMap.setPredicate(predicate);
        String result = predicateObjectMap.getPredicate();
        assertThat(result, is(predicate));
    }

    /**
     * Tests that the PredicateObjectMap instance returns a valid object source
     * String set by {@code PredicateObjectMap} interface.
     */
    @Test
    public void WhenObjectSourceExists_ShouldReturnValue() {
        predicateObjectMap.setObjectSource(objectSource);
        String result = predicateObjectMap.getObjectSource();
        assertThat(result, is(objectSource));
    }
}
