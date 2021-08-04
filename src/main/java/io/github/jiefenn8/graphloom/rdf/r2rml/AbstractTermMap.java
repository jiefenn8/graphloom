/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;

import io.github.jiefenn8.graphloom.api.inputsource.Entity;
import io.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abstract class defines the implementation of the base methods specified
 * in the {@link TermMap} interface.
 */
public abstract class AbstractTermMap implements TermMap {

    private static final Pattern pattern = Pattern.compile("\\{(.*?)}");
    protected final ValuedType valuedType;
    protected final String lang;
    protected final RDFDatatype dataType;
    private final RDFNode baseValue;
    private final TermType termType;

    /**
     * Constructs a TermMap with the specified TermMap Builder containing the
     * data to initialise an immutable instance.
     *
     * @param builder the TermMap Builder to build instance from
     */
    public AbstractTermMap(AbstractBuilder<?> builder) {
        this.baseValue = builder.baseValue;
        this.termType = builder.termType;
        this.valuedType = builder.valuedType;
        this.lang = builder.lang;
        this.dataType = builder.dataType;
    }

    @Override
    public RDFNode generateRDFTerm(Entity entity) {
        Objects.requireNonNull(entity, "Entity is null.");
        return switch (valuedType) {
            case CONSTANT -> createConstantTerm();
            case TEMPLATE -> createTemplateTerm(entity);
            case COLUMN -> createColumnTerm(entity);
        };
    }

    /**
     * Returns the base value as a constant RDF term. Constant term does not
     * require any further interaction with any source to generate RDF term.
     *
     * @return the constant value to use as RDF term
     */
    private RDFNode createConstantTerm() {
        return baseValue;
    }

    /**
     * Returns a generated RDF term using the base value as template with the
     * value from entity source. The base value (as template) MUST specify a
     * valid column name identifier that can be used with entity to retrieve
     * the target data. Column name syntax rules as follows:
     * </p>
     * - At least one column name in template.
     * - Column names are enclosed by curly braces '{column_name}'.
     * - Any curly braces within the column_name must be escaped.
     * - Multiple column names should be separate from each other.
     *
     * @param entity the entity source containing the data for generation
     * @return the term generated, otherwise null if no value cannot be found
     */
    private RDFNode createTemplateTerm(Entity entity) {
        String template = baseValue.asLiteral().getString();
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            throw new MapperException("Template given cannot be matched. Must have: {name}.");
        }
        String value = entity.getPropertyValue(matcher.group(1));
        if (value != null) {
            value = URLEncoder.encode(value, StandardCharsets.UTF_8);
            String term = template.replace(matcher.group(0), value);
            return asRDFTerm(term, termType);
        }
        return null;
    }

    /**
     * Returns a generated RDF term using the base value as the column name
     * identifier to retrieve the value from entity source
     *
     * @param entity the entity source containing the data for generation
     * @return the value retrieved from source as RDF term
     */
    private RDFNode createColumnTerm(Entity entity) {
        String column = baseValue.asLiteral().getString();
        String value = entity.getPropertyValue(column);
        return value == null ? null : asRDFTerm(value, termType);
    }

    /**
     * Returns the term created from the given value and term type
     * specified to be mapped to.
     *
     * @param term the String value of the term to turn into RDF
     * @param type the term type to map the value into
     * @return the generated term value to the type specified
     */
    protected RDFNode asRDFTerm(String term, TermType type) {
        Objects.requireNonNull(term);
        return switch (type) {
            case IRI -> ResourceFactory.createResource(term);
            case BLANK -> ResourceFactory.createResource();
            case LITERAL -> generateSpecificLiteral(term);
            default -> handleDefaultGeneration(term);
        };
    }

    private Literal generateSpecificLiteral(String term) {
        if (!lang.isEmpty()) {
            return ResourceFactory.createLangLiteral(term, lang);
        }
        if (dataType != null) {
            return ResourceFactory.createTypedLiteral(term, dataType);
        }
        return ResourceFactory.createStringLiteral(term);
    }

    /**
     * Returns the term created from the given value with the default
     * generation rule defined.
     *
     * @param term the String value of the term to turn into RDF
     * @return the generated term value with the default generation defined
     */
    protected abstract RDFNode handleDefaultGeneration(String term);

    /**
     * ENUM to manage different valued TermMaps.
     */
    protected enum ValuedType {
        CONSTANT, TEMPLATE, COLUMN
    }

    /**
     * Abstract class for TermMap builder.
     * <p>
     * It is recommended to defined your extended functionality to this class
     * and then call them before calling the abstract methods below; Except for
     * overrides.
     */
    public abstract static class AbstractBuilder<T extends AbstractTermMap> {

        private final RDFNode baseValue;
        private final ValuedType valuedType;
        private TermType termType;
        private String lang = StringUtils.EMPTY;
        private RDFDatatype dataType;

        /**
         * Constructs an instance of AbstractBuilder with specified base value
         * and TermMap valued type.
         *
         * @param baseValue  to be used to generate RDF term for this TermMap
         * @param valuedType to identify the TermMap valued type, this will
         *                   determine how the base value w
         */
        public AbstractBuilder(RDFNode baseValue, ValuedType valuedType) {
            this.baseValue = baseValue;
            this.valuedType = valuedType;
            this.termType = TermType.UNDEFINED;
        }

        /**
         * Set the RDF type the TermMap should return after generating a
         * RDF term.
         *
         * @param termType the term type to return term output as
         * @return this builder for method chaining
         */
        public AbstractBuilder<T> termType(TermType termType) {
            this.termType = termType;
            return this;
        }

        /**
         * Set the RDF term language tag for this TermMap.
         *
         * @param lang the term language tag that this term will be generated as
         * @return this builder for method chaining
         */
        public AbstractBuilder<T> language(String lang) {
            this.lang = lang;
            return this;
        }

        /**
         * Set the RDF term datatype fpr this TermMap.
         *
         * @param dataType the datatype that this term will be generated as
         * @return this builder for method chaining
         */
        public AbstractBuilder<T> dataType(String dataType) {
            this.dataType = NodeFactory.getType(dataType);
            return this;
        }

        /**
         * Returns an instance of TermMap with the specified data given to
         * this builder.
         *
         * @return TermMap instance with populated fields from builder
         */
        public abstract T build();
    }
}
