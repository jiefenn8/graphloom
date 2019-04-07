package r2graph.configmap.impl;

import r2graph.configmap.AbstractPredicateObjectMapTest;
import r2graph.configmap.PredicateObjectMap;

@Deprecated
public class PredicateObjectMapComTest extends AbstractPredicateObjectMapTest {
    @Override
    public PredicateObjectMap createInstance() {
        return new PredicateObjectMapCom();
    }
}
