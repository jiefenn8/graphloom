package r2graph.r2rml;

import r2graph.configmap.AbstractConfigMapTest;
import r2graph.configmap.ConfigMap;

/**
 * Tests for R2RMLMap code using {@link AbstractConfigMapTest} test cases.
 * Currently skeletal till further iteration of the project or feature.
 */
public class R2RMLMapTest extends AbstractConfigMapTest {
    @Override
    public ConfigMap createInstance() {
        return new R2RMLMap();
    }
}
