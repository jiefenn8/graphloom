package r2graph.r2rml;

import r2graph.configmap.AbstractConfigMapTest;
import r2graph.configmap.ConfigMap;
import r2graph.r2rml.R2RMLMap;

public class R2RMLMapTest extends AbstractConfigMapTest {
    @Override
    public ConfigMap createInstance() {
        return new R2RMLMap();
    }
}
