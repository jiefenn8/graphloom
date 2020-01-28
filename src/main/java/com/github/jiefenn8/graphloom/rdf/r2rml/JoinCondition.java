/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.rdf.r2rml;

import java.util.Objects;

/**
 * Implementation of R2RML JoinCondition. This class defines
 * the base methods that manages the joins between two
 * different queries.
 */
public class JoinCondition {

    private final String parent;
    private final String child;

    /**
     * Constructs a JoinCondition with the specified parent
     * and child columns.
     *
     * @param parent the parent column to join to child
     * @param child  the child column to join to parent
     */
    protected JoinCondition(String parent, String child) {
        this.parent = parent;
        this.child = child;
    }

    /**
     * Returns the child column of this join condition.
     *
     * @return the child column name
     */
    public String getChild() {
        return child;
    }

    /**
     * Returns the parent column of this join condition.
     *
     * @return the parent column name
     */
    public String getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        JoinCondition that = (JoinCondition) obj;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child);
    }
}
