package r2graph.r2rml.impl;

import r2graph.r2rml.PredicateObjectMap;
import r2graph.r2rml.TriplesMap;

import java.util.ArrayList;
import java.util.List;

public class TriplesMapImpl implements TriplesMap {

    private String tableName;
    private String template;
    private String classType;
    private List<PredicateObjectMap> predicateObjectMaps = new ArrayList<>();

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public String getClassType() {
        return classType;
    }

    @Override
    public List<PredicateObjectMap> getPredicateObjectMaps() {
        return predicateObjectMaps;
    }


    protected void setTableName(String tableName) {
        this.tableName = tableName;
    }

    protected void setTemplate(String template) {
       this.template = template;
    }

    protected void setClassType(String classType) {
        this.classType = classType;
    }

    protected void addPredicateObjectMap(PredicateObjectMap predicateObjectMap) {
        predicateObjectMaps.add(predicateObjectMap);
    }
}
