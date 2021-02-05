/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * This interface defines the base methods that manages the mapping of any
 * source record to their respective rdf term through the use of template.
 */
public class TemplateTermMap implements TermMap {

    private static final Pattern pattern = Pattern.compile("\\{(.*?)}");
    private final String template;
    private TermType termType = TermType.IRI;

    /**
     * Constructs a TemplateTermMap with the specified template pattern and
     * term type to map into.
     *
     * @param template the template pattern to use
     * @param termType the term type to map the value into
     */
    protected TemplateTermMap(String template, TermType termType) {
        this.template = checkNotNull(template, "Template string must not be null.");
        checkNotNull(termType, "Term type must not be null.");
        if (termType != TermType.UNDEFINED) {
            this.termType = termType;
        }
    }

    @Override
    public RDFNode generateRDFTerm(Record record) {
        checkNotNull(record, "Record is null.");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            throw new MapperException("Template given cannot be matched. Must have: {name}.");
        }

        String value = record.getPropertyValue(matcher.group(1));
        return value == null ? null : createRDFTerm(template, matcher, value);
    }

    @Override
    public RDFNode generateRDFTerm(Entity entity) {
        checkNotNull(entity, "Record is null.");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            throw new MapperException("Template given cannot be matched. Must have: {name}.");
        }

        String value = entity.getPropertyValue(matcher.group(1));
        return value == null ? null : createRDFTerm(template, matcher, value);
    }

    @Override
    public RDFNode generateRDFTerm(Set<JoinCondition> joins, Record record) {
        checkNotNull(record, "Record is null.");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            throw new MapperException("Template given cannot be matched. Must have: {name}.");
        }

        String alt = "";
        for (JoinCondition join : joins) {
            String parent = join.getParent();
            if (parent.equals(matcher.group(1))) {
                alt = join.getChild();
            }
        }

        String value = record.getPropertyValue(alt);
        return value == null ? null : createRDFTerm(template, matcher, value);
    }

    @Override
    public RDFNode generateRDFTerm(Set<JoinCondition> joins, Entity entity) {
        checkNotNull(entity, "Record is null.");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            throw new MapperException("Template given cannot be matched. Must have: {name}.");
        }

        String alt = "";
        for (JoinCondition join : joins) {
            String parent = join.getParent();
            if (parent.equals(matcher.group(1))) {
                alt = join.getChild();
            }
        }

        String value = entity.getPropertyValue(alt);
        return value == null ? null : createRDFTerm(template, matcher, value);
    }

    private RDFNode createRDFTerm(@NonNull String template, @NonNull Matcher matcher, @NonNull String value) {
        String term = template.replace(matcher.group(0), value);
        return RDFTermHelper.asRDFTerm(term, termType);
    }
}
