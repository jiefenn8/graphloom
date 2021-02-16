/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.inputsource.Entity;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This interface defines the base methods that manages the mapping of any
 * source entity to their respective rdf term through the use of template.
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
        this.template = Objects.requireNonNull(template, "Template string must not be null.");
        Objects.requireNonNull(termType, "Term type must not be null.");
        if (termType != TermType.UNDEFINED) {
            this.termType = termType;
        }
    }

    @Override
    public RDFNode generateRDFTerm(Entity entity) {
        Objects.requireNonNull(entity, "Entity is null.");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            throw new MapperException("Template given cannot be matched. Must have: {name}.");
        }

        String value = entity.getPropertyValue(matcher.group(1));
        return value == null ? null : createRDFTerm(template, matcher, value);
    }

    @Override
    public RDFNode generateRDFTerm(Set<JoinCondition> joins, Entity entity) {
        Objects.requireNonNull(entity, "Entity is null.");
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

    private RDFNode createRDFTerm(String template, Matcher matcher, String value) {
        String term = template.replace(matcher.group(0), value);
        return RDFTermHelper.asRDFTerm(term, termType);
    }
}
