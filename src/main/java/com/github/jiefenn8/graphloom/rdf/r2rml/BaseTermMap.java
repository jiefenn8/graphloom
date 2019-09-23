/*
 *    Copyright (c) 2019 - Javen Liu (github.com/jiefenn8)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermMapType.CONSTANT;
import static com.github.jiefenn8.graphloom.rdf.r2rml.TermMap.TermMapType.TEMPLATE;
import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of R2RML Map using {@link TermMap} interface using
 * @code{TermType} to determine best type to return.
 */
public class BaseTermMap implements TermMap {

    private Pattern pattern = Pattern.compile("\\{(.*?)}");
    private TermMapType termMapType;
    private TermType rdfTermType;
    private RDFNode constant;
    private String template;
    private String column;
    private boolean isReferencedCol;

    //todo: replace the function to use RDF vocabulary instead. More safe.
    //Constant-value term map
    public BaseTermMap(TermMapType mapType, RDFNode constantValue) {
        termMapType = checkNotNull(mapType);
        constant = checkNotNull(constantValue);
    }

    //Template-value term map
    public BaseTermMap(TermMapType termMapType, String templateValue, TermType termType) {
        this.termMapType = checkNotNull(termMapType);
        template = checkNotNull(templateValue);
        rdfTermType = checkNotNull(termType);
    }

    //Column-valued term map
    public BaseTermMap(TermMapType termMapType, String columnValue, TermType termType, boolean isRef) {
        this.termMapType = termMapType;
        column = columnValue;
        rdfTermType = checkNotNull(termType);
        isReferencedCol = isRef;
    }

    @Override
    public TermMapType getTermMapType() {
        return termMapType;
    }

    public RDFNode generateRDFTerm(Map<String, String> entityProps) {
        RDFNode rdfTerm = null;
        switch (termMapType) {
            case TEMPLATE:
                rdfTerm = generateTemplateTerm(entityProps);
                break;
            case COLUMN:
                rdfTerm = generateColumnTerm(entityProps);
                break;
            case CONSTANT:
                rdfTerm = generateConstantTerm();
                break;
        }
        if (rdfTerm == null) throw new MapperException("Failed to generate RDF Term.");
        return rdfTerm;
    }

    @Override
    public RDFNode generateConstantTerm() {
        if (!termMapType.equals(CONSTANT)) throw new MapperException("Invalid operation; Constant Term Map only.");
        return constant;
    }

    @Override
    public RDFNode generateTemplateTerm(Map<String, String> entityRow) {
        if (!termMapType.equals(TEMPLATE)) throw new MapperException("Invalid operation; Template Term Map only.");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) throw new MapperException("Failed to generate template.");
        String generatedTerm = template.replace(matcher.group(0), checkNotNull(entityRow).get(matcher.group(1)));
        return asRDFTerm(generatedTerm, rdfTermType);
    }

    @Override
    public RDFNode generateColumnTerm(Map<String, String> entityRow) {
        return asRDFTerm(checkNotNull(entityRow).get(column), rdfTermType);
    }

    private RDFNode asRDFTerm(String value, TermType type) {
        RDFNode rdfTerm = null;
        switch (type) {
            case IRI:
                rdfTerm = ResourceFactory.createResource(value);
                break;
            case BLANK:
                rdfTerm = ResourceFactory.createResource();
                break;
            case LITERAL:
                rdfTerm = ResourceFactory.createStringLiteral(value);
                break;
        }
        return rdfTerm;
    }
}
