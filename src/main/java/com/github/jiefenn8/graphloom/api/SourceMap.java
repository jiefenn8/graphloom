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
 * SourceMap
 * <p>
 * This interface defines the base methods that provides the information
 * needed to locate and retrieve the desired data from the data-source.
 */
public interface SourceMap {

    /**
     * Sets and prepare the main source where the Entity and its properties should
     * be retrieved from as EntityRecord.
     *
     * @param source of the Entity and its properties.
     * @return this SourceMap after validating the source.
     */
    SourceMap loadInputSource(InputSource source);

    /**
     * Takes in a {@code Consumer} that will consume each Record from the
     * retrieved {@code EntityRecord} from {@code InputSource}.
     * <p>
     * This method is used when user want to iterate through all the available
     * Entity records from {@code InputSource} with several batches.
     *
     * @param action the action to perform.
     */
    void forEachEntityRecord(Consumer<Record> action);


    /**
     * Returns an EntityRecord collection based on the batch id; containing
     * Records of Entity properties.
     *
     * @return the collection of Records as EntityRecord.
     */
    EntityRecord getEntityRecord(int batchId);
}
