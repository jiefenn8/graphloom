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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a single record of an Entity using HashMap implementation with the
 * property as String key its value as String value.
 */
public class HashRecord implements Record {

    private Map<String, String> recordValue;

    //Default constructor
    public HashRecord(String column, String value) {
        recordValue = new HashMap<>();
        recordValue.put(column, value);
    }

    public HashRecord(Map<String, String> m) {
        recordValue = new HashMap<>(m);
    }

    @Override
    public String addProperty(String property, String value) {
        return recordValue.put(property, value);
    }

    @Override
    public boolean hasProperty(String property) {
        return recordValue.containsKey(property);
    }

    @Override
    public boolean hasValue(String value) {
        return recordValue.containsValue(value);
    }

    @Override
    public String getPropertyValue(String property) {
        return recordValue.get(property);
    }

    @Override
    public String removeProperty(String property) {
        return recordValue.remove(property);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashRecord that = (HashRecord) o;
        return Objects.equals(recordValue, that.recordValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordValue);
    }

    @Override
    public Set<String> properties() {
        return recordValue.keySet();
    }
}
