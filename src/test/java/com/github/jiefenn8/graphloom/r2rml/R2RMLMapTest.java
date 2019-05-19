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

package com.github.jiefenn8.graphloom.r2rml;

import com.github.jiefenn8.graphloom.configmap.AbstractConfigMapTest;
import com.github.jiefenn8.graphloom.configmap.ConfigMap;

/**
 * Tests for R2RMLMap code using {@link AbstractConfigMapTest} test cases.
 * Currently skeletal till further iteration of the project or feature.
 */
public class R2RMLMapTest extends AbstractConfigMapTest {
    @Override
    public ConfigMap createInstance() {
        return new R2RMLMap();
    }
}
