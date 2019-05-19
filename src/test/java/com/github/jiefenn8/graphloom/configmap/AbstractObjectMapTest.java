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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for ObjectMap code. Currently skeletal till further
 * iteration of the project or feature.
 */
public abstract class AbstractObjectMapTest {

    private final String objectSource = "ENAME";
    private ObjectMap objectMap;

    public abstract ObjectMap createInstance();

    @Before
    public void setUp() throws Exception {
        objectMap = createInstance();
    }

    /**
     * Tests that the ObjectMap instance returns a valid object source
     * String set by {@code ObjectMap}.
     */
    @Test
    public void WhenObjectSourceExists_ShouldReturnValue() {
        objectMap.setObjectSource(objectSource);
        String result = objectMap.getObjectSource();
        assertThat(result, is(objectSource));
    }
}
