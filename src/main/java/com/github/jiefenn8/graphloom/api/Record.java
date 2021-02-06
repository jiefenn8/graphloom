/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.api;

import java.util.Map;
import java.util.Set;

/**
 * This interface defines the base method to manage a collection of
 * properties with their values as Strings.
 */
@Deprecated
public interface Record {

    /**
     * Adds the given property and its value to this Record if it does
     * not exist. If this Record already the property, the old value
     * of that property will be replaced with the given value. Returns
     * any previously associated value of given property or null if the
     * property does not exist.
     *
     * @param property name of the property to add to Record.
     * @param value    value to associate to the property.
     * @return the previous value associated with the property
     * otherwise null
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

    /**
     * Returns the value of the given property in this Record if
     * does exist. If the property does not exist in the Record, null
     * will be returned. A null value returned does not fully indicate
     * there is no property but can also mean that the property exist
     * but the value is mapped to null.
     *
     * @param property property to retrieve its value in this Record
     * @return the value mapped to given property otherwise null
     */
    String getPropertyValue(String property);

    /**
     * Removes the given property from this Record if it exist.
     * Returns any value that was associated with the property or
     * null if the property does not exist.
     *
     * @param property property to be removed from Record
     * @return the previous value associated with the property
     * otherwise null
     */
    String removeProperty(String property);

    /**
     * Compares the given object with this Record for equality.
     * Returns true if the given object is also a Record that
     * has the same properties and values associated with them.
     *
     * @param obj the object to be compared for equality with this record
     * @return true if given object is equal to this record
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code value for this Record. The hash code
     * of this Record is defined to be the sum of the properties
     * and values of each elements in this Record.
     *
     * @return the hash code value for this record
     * @see Object#equals(Object)
     */
    @Override
    int hashCode();

    /**
     * Returns a Set of properties contained in this Record.
     *
     * @return the Set of the properties contained in record
     * @see Map#keySet()
     */
    Set<String> properties();

    /**
     * Returns the number of columns this records has.
     *
     * @return the number of columns
     */
    int columnSize();
}
