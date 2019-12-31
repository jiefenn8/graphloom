/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermType;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class defines the base methods in managing common RDF related
 * operations.
 */
public class RDFTermHelper {

    /**
     * Returns the term created from the given value and term type
     * specified to be mapped to.
     *
     * @param value the value of the term
     * @param type  the term type to map the value into
     * @return the generated term value to the type specified
     */
    public static RDFNode asRDFTerm(String value, TermType type) {
        checkNotNull(value);
        switch (checkNotNull(type)) {
            case IRI:
                return ResourceFactory.createResource(value);
            case BLANK:
                return ResourceFactory.createResource();
            case LITERAL:
                return ResourceFactory.createStringLiteral(value);
            default:
                throw new MapperException("Term type is UNDEFINED.");
        }
    }
}
