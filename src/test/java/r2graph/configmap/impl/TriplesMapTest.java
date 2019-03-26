package r2graph.configmap.impl;

import r2graph.configmap.AbstractEntityMapTest;
import r2graph.configmap.EntityMap;

public class TriplesMapTest extends AbstractEntityMapTest {
    @Override
    public EntityMap createInstance() {
        return new TriplesMap();
    }
}
