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

package com.github.jiefenn8.graphloom.rdf.r2rml;

import com.github.jiefenn8.graphloom.api.Record;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.RDFNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.jena.ext.com.google.common.base.Preconditions.checkNotNull;

public class TmplTermMap implements TermMap {

    private Pattern pattern = Pattern.compile("\\{(.*?)}");
    private String templateStr;
    private TermType termType;

    protected TmplTermMap(String templateStr, TermType termType) {
        this.templateStr = checkNotNull(templateStr);
        this.termType = checkNotNull(termType);
    }

    @Override
    public RDFNode generateRDFTerm(Record entityProps) {
        Matcher matcher = pattern.matcher(templateStr);
        if (!matcher.find()) throw new MapperException("Invalid template string given.");
        String generatedTerm = templateStr.replace(matcher.group(0), entityProps.getPropertyValue(matcher.group(1)));
        return RDFTermHelper.asRDFTerm(generatedTerm, termType);
    }
}
