/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.rdf.r2rml;


import com.google.gson.Gson;

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
        this.parent = Objects.requireNonNull(parent);
        this.child = Objects.requireNonNull(child);
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * Returns the string version of a SQL Join for both child and parent.
     *
     * @return string of the joined child and parent
     */
    public String getJoinString() {
        return "child." + child + "=parent." + parent;
    }
}
