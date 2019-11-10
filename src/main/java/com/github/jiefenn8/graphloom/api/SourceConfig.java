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

public interface SourceConfig {

    /**
     * Returns the payload type of this class.
     *
     * @return the payload type
     */
    PayloadType getPayloadType();

    /**
     * Returns the payload string that defines query on what Entity
     * result is needed to continue the mapping.
     *
     * @return the payload containing info needed to execute the query
     */
    String getPayload();

    /**
     * Returns a definition that defines how to iterate through each
     * record of Entity result.
     *
     * @return the value containing the iteration definition
     */
    String getIteratorDef();

    /**
     * Returns the value of a property in this source config.
     *
     * @param propertyName the given property name to retrieve its value
     * @return the value of the given property otherwise null
     */
    String getProperty(String propertyName);

    /**
     * This interface defines the base methods to manage the type of payload
     * the source config can be.
     */
    interface PayloadType {
    }
}
