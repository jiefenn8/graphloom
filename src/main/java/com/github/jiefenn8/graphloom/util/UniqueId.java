/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package com.github.jiefenn8.graphloom.util;

import java.util.UUID;

/**
 * This interface defines the base methods in uniquely identify an
 * instance of an implementation.
 */
public interface UniqueId {

    /**
     * Returns the {@link UUID} generated in this instance.
     *
     * @return UUID of this instance
     */
    String getUniqueId();
}
