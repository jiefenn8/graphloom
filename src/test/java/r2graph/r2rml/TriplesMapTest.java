package r2graph.r2rml;

import r2graph.configmap.AbstractEntityMapTest;
import r2graph.configmap.EntityMap;

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
