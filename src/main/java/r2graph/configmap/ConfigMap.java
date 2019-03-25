package r2graph.configmap;

import java.util.Map;

/**
 * A Configuration Map
 * <p>
 * This interface defines the base methods to express the rules
 * from many configurations that will map data in relational
 * structure and serialisation to a graph model.
 */
public interface ConfigMap {

    /**
     * Appends {@code EntityMap} to the map that will be used to configure
     * the mapping rules of entity, predicate and objects specified in each
     * EntityMap.
     *
     * @param id the id name of the entity map
     * @param entityMap the {@code EntityMap} to appended to map
     * @return the given {@code EntityMap} when successfully appended
     */
    EntityMap addEntityMap(String id, EntityMap entityMap);

    /**
     * Returns map of all {@code EntityMap} that contains the configuration
     * rules for the generation of entity, predicate and object results.
     *
     * @return the map containing all {@code EntityMap}
     */
    Map<String, EntityMap> listEntityMaps();
}
