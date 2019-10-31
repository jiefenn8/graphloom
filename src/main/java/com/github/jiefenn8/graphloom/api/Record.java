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

import java.util.Map;
import java.util.Set;

/**
 * An Record.
 * <p>
 * This interface defines a set of method to manage a collection of
 * properties with their values as Strings.
 */
public interface Record {

    /**
     * Adds the given property and its value to this Record if it does
     * not exist. If this Record already the property, the old value
     * of that property will be replaced with the given value. Returns
     * any previously associated value of given property or null if the
     * property does not exist.
     *
     * @param property name of the property to add to Record.
     * @param value value to associate to the property.
     * @return the previous value associated with the property
     *          otherwise null
     */
    String addProperty(String property, String value);

    /**
     * Returns true if this Record contains a property for the given
     * property name.
     *
     * @param property property to be searched in Record
     * @return true if this Record contains the given Property
     */
    boolean hasProperty(String property);

    /**
     * Returns true if this Record contains a property with a given
     * value.
     *
     * @param value value to be searched in Record
     * @return true if this Record contains the given property value
     */
    boolean hasValue(String value);

    String getPropertyValue(String property);

    /**
     * Removes the given property from this Record if it exist.
     * Returns any value that was associated with the property or
     * null if the property does not exist.
     *
     * @param property property to be removed from Record
     * @return the previous value associated with the property
     *          otherwise null
     */
    String removeProperty(String property);

    /**
     * Compares the given object with this Record for equality.
     * Returns true if the given object is also a Record that
     * has the same properties and values associated with them.
     *
     * @param o object to be compared for equality with this Record.
     * @return true if given object is equal to this Record.
     */
    boolean equals(Object o);

    /**
     * Returns the hash code value for this Record. The hash code
     * of this Record is defined to be the sum of the properties
     * and values of each elements in this Record.
     *
     * @return the hash code value for this Record
     * @see Object#equals(Object)
     */
    int hashCode();

    /**
     * Returns a Set of properties contained in this Record.
     *
     * @return the Set of the properties contained in Record.
     * @see Map#keySet()
     */
    Set<String> properties();
}
