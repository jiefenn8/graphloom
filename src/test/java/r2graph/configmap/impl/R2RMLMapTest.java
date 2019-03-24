package r2graph.configmap.impl;

import r2graph.configmap.AbstractConfigMapTest;
import r2graph.configmap.ConfigMap;

public class R2RMLMapTest extends AbstractConfigMapTest {
    @Override
    public ConfigMap createInstance() {
        return new R2RMLMap();
    }
}
