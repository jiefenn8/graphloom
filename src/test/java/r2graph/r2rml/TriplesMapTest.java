package r2graph.r2rml;

import r2graph.configmap.AbstractEntityMapTest;
import r2graph.configmap.EntityMap;
import r2graph.r2rml.TriplesMap;

public class TriplesMapTest extends AbstractEntityMapTest {
    @Override
    public EntityMap createInstance() {
        return new TriplesMap();
    }
}
