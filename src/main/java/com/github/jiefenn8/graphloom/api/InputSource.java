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

/**
 * Input Source
 * <p>
 * This interface defines the base methods to retrieve data from
 * a data source.
 */
public interface InputSource {

    /**
     * Returns a collection of {@code Record} for an entity containing data
     * representing it properties and their values. If the query result
     * is a large data-set, the InputSource may split the result and return
     * several batches of the result. The retrieval of specific batch can be
     * controlled by specifying the seq number.
     * E.g. 2000 items, fetch size of 500 = 4 batches; batch 2 will be the 2nd
     * collection of data.
     *
     * @param config  to setup the query for this entity.
     * @param batchId the batch to return
     * @return the collection of Records as EntityRecord. Else return empty
     * {@code EntityRecord} if there is no more Records to return.
     */
    EntityRecord getEntityRecord(SourceConfig config, int batchId);

    /**
     * Returns the number of batches to expect for a {@code SourceConfig}.
     *
     * @param config containing the Entity query info.
     * @return the number of batches possible.
     */
    int calculateNumOfBatches(SourceConfig config);
}
