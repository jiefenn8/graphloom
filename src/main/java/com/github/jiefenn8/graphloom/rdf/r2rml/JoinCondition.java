/*
 *    Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 *    This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import java.util.Objects;

public class JoinCondition {

    private final String parent;
    private final String child;

    protected JoinCondition(String parent, String child) {
        this.parent = parent;
        this.child = child;
    }

    public String getChild() {
        return child;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinCondition that = (JoinCondition) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child);
    }
}
