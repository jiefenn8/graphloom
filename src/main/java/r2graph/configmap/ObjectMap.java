package r2graph.configmap;

/**
 * Object Map
 * <p>
 * This interface defines the base methods that manages the mapping
 * configuration of any objects related to parent entity
 * by the {@link PredicateMap}.
 */
public interface ObjectMap {
    /**
     * Returns the name of the source where the object data is located.
     * The name should be a reference to a database table column or a child
     * in a file storing the object data.
     *
     * @return the String of the object source name
     */
    String getObjectSource();

    /**
     * Sets the object source name. Depending on the type of source, the name
     * should be a reference to a database table column or a child id in a file
     * that is storing the object data.
     *
     * @param objectSource the String of the object source to use as reference
     */
    void setObjectSource(String objectSource);
}
