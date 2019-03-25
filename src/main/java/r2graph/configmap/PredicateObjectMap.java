package r2graph.configmap;

/**
 * Predicate Object Map
 * <p>
 * This interface defines the base methods that manages the mapping
 * configuration of predicate and any objects related to parent entity
 * by the specified predicate.
 */
public interface PredicateObjectMap {

    /**
     * Sets the String to use for predicate URI generation and to describe
     * the relationship between the parent entity and objects.
     *
     * @param predicate the predicate String to use for generation
     */
    void setPredicate(String predicate);

    /**
     * Returns the predicate String used to describe the predicate (relationship)
     * between the parent entity and object.
     *
     * @return the String of the predicate name
     */
    String getPredicate();

    /**
     * Sets the object source name. Depending on the type of source, the name
     * should be a reference to a database table column or a child id in a file
     * that is storing the object data.
     *
     * @param objectSource the String of the object source to use as reference
     */
    void setObjectSource(String objectSource);

    /**
     * Returns the name of the source where the object data is located.
     * The name should be a reference to a database table column or a child
     * in a file storing the object data.
     *
     * @return the String of the object source name
     */
    String getObjectSource();
}
