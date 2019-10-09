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

import org.apache.jena.rdf.model.RDFNode;

import java.util.Map;

public interface TermMap {

    /**
     * Returns the TermMapType that this instance
     * is initialised as.
     *
     * @return the type this class is set as.
     */
    TermMapType getTermMapType();

    /**
     * Returns a generated RDF Term using the provided entity
     * properties collections. The type of RDF term returned
     * depends on the class {@code TermMapType}.
     *
     * @param entityProps the entity properties collection to use.
     * @return the RDF term generated.
     */
    RDFNode generateRDFTerm(Map<String, String> entityProps);

    /**
     * Returns a generated constant RDF Term. The constant value
     * must be an IRI or literal that is given to this instance
     * and will ignore any {@code TermType} specified.
     *
     * @return the constant RDF term generated.
     */
    RDFNode generateConstantTerm();

    /**
     * Returns a generated constant RDF Term. The term value
     * must be an IRI unless specified by {@code TermType} as
     * {@code LITERAL}.
     *
     * @param entityProps the entity properties collection to use.
     * @return the RDF term generated.
     */
    RDFNode generateTemplateTerm(Map<String, String> entityProps);

    /**
     * Returns a generated constant RDF Term. The term value
     * must be a literal.
     * <p>
     * todo: Expand to type literal and other types if applicable.
     *
     * @param entityProps the entity properties collection to use.
     * @return the RDF term generated.
     */
    RDFNode generateColumnTerm(Map<String, String> entityProps);

    /**
     * The TermMapType that this instance represent.
     */
    enum TermMapType {
        CONSTANT, TEMPLATE, COLUMN
    }

    /**
     * The TermType to return generated term as.
     */
    enum TermType {
        IRI, BLANK, LITERAL
    }
}
