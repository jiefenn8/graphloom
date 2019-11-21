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

package com.github.jiefenn8.graphloom.integrationtest;

import com.github.jiefenn8.graphloom.api.InputSource;
import com.github.jiefenn8.graphloom.api.SourceConfig;
import com.github.jiefenn8.graphloom.api.MutableRecord;
import com.github.jiefenn8.graphloom.api.MutableEntityRecord;
import com.google.common.collect.ImmutableMap;

public class FakeInputDatabase implements InputSource {

    @Override
    public MutableEntityRecord getEntityRecord(SourceConfig config, int batchId) {
        MutableEntityRecord entityRecord = new MutableEntityRecord();
        entityRecord.addRecord(new MutableRecord(ImmutableMap.of(
                "EMPNO", "7369",
                "ENAME", "SMITH",
                "JOB", "CLERK",
                "DEPTNO", "10")));

        return entityRecord;
    }

    @Override
    public int calculateNumOfBatches(SourceConfig config) {
        return 1;
    }
}
