package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermMapType;
import org.apache.jena.rdf.model.*;

public class R2RMLFactory {

    public static LogicalTable createLogicalTable(String source){
        return new LogicalTable(source);
    }

    public static PredicateMap createConstPredicateMap(Property constant){
        return new PredicateMap(TermMapType.CONSTANT, constant);
    }

    public static PredicateMap createTemplatePredicateMap(String template){
        return new PredicateMap(TermMapType.TEMPLATE, template);
    }

    public static SubjectMap createConstSubjectMap(RDFNode constant){
        return new SubjectMap(TermMapType.CONSTANT, constant);
    }

    public static SubjectMap createTemplateSubjectMap(String template){
        return new SubjectMap(TermMapType.TEMPLATE, template);
    }

    public static SubjectMap createColumnSubjectMap(String column, boolean isRef){
        return new SubjectMap(TermMapType.COLUMN, column, isRef);
    }

    public static ObjectMap createConstObjectMap(RDFNode constant){
        return new ObjectMap(TermMapType.CONSTANT, constant);
    }

    public static ObjectMap createTemplateObjectMap(String template){
        return new ObjectMap(TermMapType.TEMPLATE, template);
    }

    public static ObjectMap createColumnObjectMap(String column){
        return new ObjectMap(TermMapType.COLUMN, column);
    }

    public static TriplesMap createTriplesMap(LogicalTable logicalTable, SubjectMap subjectMap){
        return new TriplesMap(logicalTable, subjectMap);
    }
}
