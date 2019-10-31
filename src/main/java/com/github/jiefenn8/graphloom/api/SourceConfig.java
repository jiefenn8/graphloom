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
     * Returns the payload type id of this class.
     *
     * @return the payload type.
     */
    PayloadType getPayloadType();

    /**
     * Returns the payload string that defines query on what Entity
     * result is needed to continue the mapping.
     *
     * @return the String containing info needed to execute a query.
     */
    String getPayload();

    /**
     * Returns a definition that defines how to iterate through each
     * record of Entity result.
     *
     * @return the String containing the iteration definition.
     */
    String getIteratorDef();

    /**
     * Returns the value of a property that is set in this instance.
     *
     * @param propertyName the name of the property to get value.
     * @return the String containing the value of the property else
     * return null;
     */
    String getProperty(String propertyName);

    /**
     * The interface to manage the type of payload.
     * Can be used to implement with enum.
     */
    interface PayloadType {
    }
}
