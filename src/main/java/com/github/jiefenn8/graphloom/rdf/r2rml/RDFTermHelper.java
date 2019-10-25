package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

public class RDFTermHelper {

    public static RDFNode asRDFTerm(String value, TermMap.TermType type) {
        switch (type) {
            case IRI:
                return ResourceFactory.createResource(value);
            case BLANK:
                return ResourceFactory.createResource();
            case LITERAL:
                return ResourceFactory.createStringLiteral(value);
            default:
                throw new MapperException("TermType is invalid.");
        }
    }
}
