/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * This interface defines the base methods that manages the mapping of any
 * source record to their respective rdf term through the use of template.
 */
public class TemplateTermMap implements TermMap {

    private static final Pattern pattern = Pattern.compile("\\{(.*?)}");
    private String templateStr;
    private TermType termType = TermType.IRI;

    /**
     * Constructs a TemplateTermMap with the specified template pattern and
     * term type to map into.
     *
     * @param templateStr the template pattern to use
     * @param termType    the term type to map the value into
     */
    protected TemplateTermMap(String templateStr, TermType termType) {
        this.templateStr = checkNotNull(templateStr, "Template string must not be null.");
        checkNotNull(termType, "Term type must not be null.");
        if (termType != TermType.UNDEFINED) {
            this.termType = termType;
        }
    }

    @Override
    public RDFNode generateRDFTerm(Record record) {
        checkNotNull(record, "Record is null.");
        Matcher matcher = pattern.matcher(templateStr);
        if (!matcher.find()) throw new MapperException("Invalid template string given.");

        String generatedTerm = templateStr.replace(matcher.group(0), record.getPropertyValue(matcher.group(1)));
        return RDFTermHelper.asRDFTerm(generatedTerm, termType);
    }
}
