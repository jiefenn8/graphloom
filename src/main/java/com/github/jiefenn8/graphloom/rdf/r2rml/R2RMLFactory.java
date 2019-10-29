package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import org.apache.jena.rdf.model.*;

import java.util.Map;

public class R2RMLFactory {

    //R2RMLMap
    public static R2RMLMap createR2RMLMap(Map<String, String> nsMap){
        return new R2RMLMap(nsMap);
    }

    //LogicalTable
    public static LogicalTable createLogicalTable(String source){
        return new LogicalTable(source);
    }

    //PredicateMap
    public static PredicateMap createConstPredicateMap(Property constant){
        return createPredicateMap(new ConstTermMap(constant));
    }

    public static PredicateMap createTmplPredicateMap(String template){
        return createTmplPredicateMap(template, TermType.IRI);
    }

    public static PredicateMap createTmplPredicateMap(String template, TermType termType){
        return createPredicateMap(new TmplTermMap(template, termType));
    }

    private static PredicateMap createPredicateMap(TermMap termMap){
        return new PredicateMap(termMap);
    }

    //SubjectMap
    public static SubjectMap createConstSubjectMap(RDFNode constant){
        return createSubjectMap(new ConstTermMap(constant));
    }

    public static SubjectMap createTmplSubjectMap(String template){
        return createTmplSubjectMap(template, TermType.IRI);
    }

    public static SubjectMap createTmplSubjectMap(String template, TermType termType){
        return createSubjectMap(new TmplTermMap(template, termType));
    }

    public static SubjectMap createColSubjectMap(String column){
        return createColSubjectMap(column, TermType.LITERAL);
    }

    public static SubjectMap createColSubjectMap(String column, TermType termType){
        return createSubjectMap(new ColTermMap(column, termType));
    }

    private static SubjectMap createSubjectMap(TermMap termMap){
        return new SubjectMap(termMap);
    }

    //ObjectMap
    public static ObjectMap createConstObjectMap(RDFNode constant){
        return createObjectMap(new ConstTermMap(constant));
    }

    public static ObjectMap createTmplObjectMap(String template){
        return createTmplObjectMap(template, TermType.IRI);
    }

    public static ObjectMap createTmplObjectMap(String template, TermType termType){
        return createObjectMap(new TmplTermMap(template, termType));
    }

    public static ObjectMap createColObjectMap(String column){
        return createColObjectMap(column, TermType.LITERAL);
    }

    public static ObjectMap createColObjectMap(String column, TermType termType){
        return new ObjectMap(new ColTermMap(column, termType));
    }

    private static ObjectMap createObjectMap(TermMap termMap){
        return new ObjectMap(termMap);
    }

    //TriplesMap
    public static TriplesMap createTriplesMap(LogicalTable logicalTable, SubjectMap subjectMap){
        return new TriplesMap(logicalTable, subjectMap);
    }
}
