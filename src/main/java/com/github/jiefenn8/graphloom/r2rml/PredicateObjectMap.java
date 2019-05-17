package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.configmap.ObjectMap;
import com.github.jiefenn8.graphloom.configmap.PredicateMap;

/**
 * Predicate Object Map
 * <p>
 * Implementation of {@link PredicateMap} and {@link ObjectMap} interface.
 * Manages the mapping configuration of predicate and any objects related
 * to parent entity by the specified predicate.
 */
public class PredicateObjectMap implements PredicateMap, ObjectMap {

    private String predicate;
    private String objectSource;

    //Default constructor
    public PredicateObjectMap() {
    }

    /**
     * @return the String of the predicate name
     * @see PredicateMap#getPredicate()
     */
    @Override
    public String getPredicate() {
        return predicate;
    }

    /**
     * @param predicate the predicate String to use for generation
     * @see PredicateMap#setPredicate(String)
     */
    @Override
    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    /**
     * @return the {@code ObjectMap} containing object mapping configs
     * @see PredicateMap#getObjectMap()
     */
    @Override
    public ObjectMap getObjectMap() {
        return this;
    }

    /**
     * @param objectMap the {@code ObjectMap} storing the object configs
     * @see PredicateMap#setPredicate(String)
     */
    @Override
    public void setObjectMap(ObjectMap objectMap) {
        objectSource = objectMap.getObjectSource();
    }

    /**
     * @return the String of the object source name
     * @see ObjectMap#getObjectSource()
     */
    @Override
    public String getObjectSource() {
        return objectSource;
    }

    /**
     * @param objectSource the String of the object source to use as reference
     * @see ObjectMap#setObjectSource(String)
     */
    @Override
    public void setObjectSource(String objectSource) {
        this.objectSource = objectSource;
    }
}
