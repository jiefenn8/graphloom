package r2graph.configmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractPredicateObjectMapTest {

    private final String predicate = "http://example.com/ns#name";
    private final String objectSource = "ENAME";
    private PredicateObjectMap predicateObjectMap;

    public abstract PredicateObjectMap createInstance();

    @Before
    public void setUp() throws Exception {
        predicateObjectMap = createInstance();
    }

    @Test
    public void WhenPredicateExists_ShouldReturnValue(){
        predicateObjectMap.setPredicate(predicate);
        String result = predicateObjectMap.getPredicate();
        assertThat(result, is(predicate));
    }

    @Test
    public void WhenObjectSourceExists_ShouldReturnValue(){
        predicateObjectMap.setObjectSource(objectSource);
        String result = predicateObjectMap.getObjectSource();
        assertThat(result, is(objectSource));
    }
}
