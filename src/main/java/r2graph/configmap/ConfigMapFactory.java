package r2graph.configmap;

import r2graph.configmap.impl.R2RMLMap;

/**
 * ConfigMapFactory provides methods for creating Config implementations.
 */
public class ConfigMapFactory {

    /**
     * Returns a new instance of R2RMLMap class.
     * @return a {@code R2RMLMap} instance
     */
    public static ConfigMap createR2RMLMap() {
        return new R2RMLMap();
    }
}
