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
 * An Entity Record.
 * <p>
 * This interface defines a set of methods to manage a collection
 * that contains multiple Record with the same properties.
 */
public interface EntityRecord extends Iterable<Record> {

    /**
     * Returns the number of records in this collection. If this
     * set contains more than the {@link Integer#MAX_VALUE} elements,
     * returns the max value possible of Integer.
     *
     * @return the number of elements in this collection
     */
    int size();

    /**
     * Returns true if this collection contains the given Record.
     *
     * @param r record to be searched in collection
     * @return true if this collection contains the given Record
     * @throws NullPointerException if the given Record is null and this
     *          collection does not allow null elements
     */
    boolean containsRecord(Record r);

    /**
     * Removes all the Records from this collection. The collection
     * will be empty after this call returns.
     */
    void clear();

    /**
     * Removes the given Record from this collection if it does exist.
     * Returns true if this collection contains given Record and is removed.
     *
     * @param r record to be removed from collection
     * @return true if the given Record exists and is removed.
     */
    boolean removeRecord(Record r);

    /**
     * Adds the given Record to this collection if it does not exist.
     * If this collection already contains previous Record with different
     * properties, the method will leave the collection unchanged and
     * returns false.
     *
     * @param r record to be added to the collection
     * @return true if the given Record element did not exist in the
     *          collection or its properties matches with existing elements
     */
    boolean addRecord(Record r);

    /**
     * Returns true if this collection contains no Records.
     *
     * @return true if this collection contains no Records
     */
    boolean isEmpty();

    /**
     * Compares the given object with this collection for equality. Returns
     * true if the given object is also a collection that has the same
     * amount of Records and their values associated with them.
     *
     * @param o object to be compared for equality with this collection
     * @return true if given object is equal to this collection
     */
    boolean equals(Object o);

    /**
     * Returns the hash code value for this collection. The hash code of
     * this collection is defined to be the sum of the Record elements.
     *
     * @return the hash code value for this collection
     * @see Object#equals(Object)
     */
    int hashCode();
}
