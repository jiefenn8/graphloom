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
 * SourceMap
 * <p>
 * This interface defines the base methods that provides the information
 * needed to locate and retrieve the desired data from the data-source.
 */
public interface SourceMap {
    /**
     * Returns the source id where all of the entity records is stored at.
     *
     * @return the source id of the entity.
     */
    String getSource();
}
