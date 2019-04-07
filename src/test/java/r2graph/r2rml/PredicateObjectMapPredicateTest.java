package r2graph.r2rml;

import r2graph.configmap.AbstractPredicateMapTest;
import r2graph.configmap.PredicateMap;

public class PredicateObjectMapPredicateTest extends AbstractPredicateMapTest {

    @Override
    public PredicateMap createInstance() {
        return new PredicateObjectMap();
    }
}