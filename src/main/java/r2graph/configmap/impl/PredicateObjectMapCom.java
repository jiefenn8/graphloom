package r2graph.configmap.impl;

import r2graph.configmap.PredicateObjectMap;

import java.util.HashMap;

public class PredicateObjectMapCom implements PredicateObjectMap {

    private String predicate;
    private String objectSource;

    protected PredicateObjectMapCom(){}

    @Override
    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    @Override
    public String getPredicate() {
        return predicate;
    }

    @Override
    public void setObjectSource(String objectSource) {
        this.objectSource = objectSource;
    }

    @Override
    public String getObjectSource() {
        return objectSource;
    }
}
