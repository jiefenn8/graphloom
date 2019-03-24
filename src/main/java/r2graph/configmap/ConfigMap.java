package r2graph.configmap;

import java.util.Map;

/**
 * A Configuration Map
 * <p>
 *     This interfaces defines the base methods to express the rules
 *     from many configurations that will map data in relational
 *     structure and serialisation to a graph model.
 */
public interface ConfigMap {
    /**
     * Add an entity map the configuration map.
     *
     * @param id the name of the entity map
     * @param entityMap the EntityMap to add
     * @return the given entityMap when successful
     */
    EntityMap addTriplesMap(String id, EntityMap entityMap);

    /**
     * Return map of all entity maps in the configuration map.
     *
     * @return the map containing all the entity maps
     */
    Map<String, EntityMap> listTriplesMap();
}
