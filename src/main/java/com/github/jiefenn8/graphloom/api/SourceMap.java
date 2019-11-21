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

package com.github.jiefenn8.graphloom.api;

import java.util.function.Consumer;

/**
 * This interface defines the base methods that provides the information
 * needed to locate and retrieve the desired data from the data-source.
 */
public interface SourceMap {

    /**
     * Returns this source map after applying it on the given input source.
     *
     * @param s input source containing the data to map over to graph
     * @return the source map ready to be queried and iterated on
     */
    SourceMap loadInputSource(InputSource s);

    /**
     * This call will consume each record given from the input source after
     * querying and apply the consumer action on each of them. This method
     * is used when it is desired to iterate through all the available entity
     * records from input source with several batches.
     *
     * @param action the action to perform on each record
     */
    void forEachEntityRecord(Consumer<Record> action);

    /**
     * Returns an entity record result based on the batch id; containing
     * records of an entity from applied source map.
     *
     * @return the collection of records for an entity
     */
    EntityRecord getEntityRecord(int batchId);
}
