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

package com.github.jiefenn8.graphloom.io;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.InputStream;


/**
 * Mapping Document
 * <p>
 * This class defines the base methods that manages the document
 * that contains the mapping configurations.
 */
public class MappingDocument {

    private InputStream fileStream;
    private Model graph;

    //Stop usage of this constructor
    private MappingDocument() {
    }

    //Default constructor
    public MappingDocument(InputStream in) {
        this.fileStream = in;
    }

    private Model load() {
        return graph = ModelFactory.createDefaultModel().read(fileStream, null, "TTL");
    }

    /**
     * Returns mapping graph of the loaded mapping document data.
     *
     * @return the {@code Model} containing the mapping
     */
    public Model getMappingGraph() {
        if (graph == null) {
            return load();
        }

        return graph;
    }

}
