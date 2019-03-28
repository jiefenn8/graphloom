package r2graph.configmap.impl;

import r2graph.configmap.PredicateObjectMap;

/**
 * Implementation of {@link PredicateObjectMap} interface.
 */
public class PredicateObjectMapCom implements PredicateObjectMap {

    private String predicate;
    private String objectSource;

    //Default constructor
    protected PredicateObjectMapCom(){}

    /**
     * @see PredicateObjectMap#setPredicate(String)
     *
     * @param predicate the predicate String to use for generation
     */
    @Override
    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    /**
     * @see PredicateObjectMap#getPredicate()
     *
     * @return
     */
    @Override
    public String getPredicate() {
        return predicate;
    }

    /**
     * @see PredicateObjectMap#setObjectSource(String)
     *
     * @param objectSource the String of the object source to use as reference
     */
    @Override
    public void setObjectSource(String objectSource) {
        this.objectSource = objectSource;
    }

    /**
     * @see PredicateObjectMap#getObjectSource()
     *
     * @return
     */
    @Override
    public String getObjectSource() {
        return objectSource;
    }
}
