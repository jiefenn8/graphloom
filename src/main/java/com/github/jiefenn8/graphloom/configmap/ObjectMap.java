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

package com.github.jiefenn8.graphloom.configmap;

/**
 * Object Map
 * <p>
 * This interface defines the base methods that manages the mapping
 * configuration of any objects related to parent entity
 * by the {@link PredicateMap}.
 */
public interface ObjectMap {
    /**
     * Returns the name of the source where the object data is located.
     * The name should be a reference to a database table column or a child
     * in a file storing the object data.
     *
     * @return the String of the object source name
     */
    String getObjectSource();

    /**
     * Sets the object source name. Depending on the type of source, the name
     * should be a reference to a database table column or a child id in a file
     * that is storing the object data.
     *
     * @param objectSource the String of the object source to use as reference
     */
    void setObjectSource(String objectSource);
}
