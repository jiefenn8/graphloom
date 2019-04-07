package r2graph.configmap;

/**
 * Object Map
 * <p>
 * This interface defines the base methods that manages the mapping
 * configuration of predicate between an entity and object specified
 * by the {@link ObjectMap}.
 */
public interface PredicateMap {

    /**
     * Returns the predicate String used to describe the predicate (relationship)
     * between the parent entity and object.
     *
     * @return the String of the predicate name
     */
    String getPredicate();

    /**
     * Sets the String to use for predicate URI generation and to describe
     * the relationship between the parent entity and objects.
     *
     * @param predicate the predicate String to use for generation
     */
    void setPredicate(String predicate);
}
