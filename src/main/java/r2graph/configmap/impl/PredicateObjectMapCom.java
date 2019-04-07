package r2graph.configmap.impl;

import r2graph.configmap.PredicateObjectMap;

/**
 * Implementation of {@link PredicateObjectMap} interface.
 */
@Deprecated
public class PredicateObjectMapCom implements PredicateObjectMap {

    private String predicate;
    private String objectSource;

    //Default constructor
    public PredicateObjectMapCom() {
    }

    /**
     * @return the String of the predicate name
     * @see PredicateObjectMap#getPredicate()
     */
    @Override
    public String getPredicate() {
        return predicate;
    }

    /**
     * @param predicate the predicate String to use for generation
     * @see PredicateObjectMap#setPredicate(String)
     */
    @Override
    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    /**
     * @return
     * @see PredicateObjectMap#getObjectSource()
     */
    @Override
    public String getObjectSource() {
        return objectSource;
    }

    /**
     * @param objectSource the String of the object source to use as reference
     * @see PredicateObjectMap#setObjectSource(String)
     */
    @Override
    public void setObjectSource(String objectSource) {
        this.objectSource = objectSource;
    }
}
