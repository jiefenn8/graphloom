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

import com.github.jiefenn8.graphloom.api.PropertyMap;
import com.github.jiefenn8.graphloom.exceptions.MapperException;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of R2RML SubjectMap with{@link PropertyMap} interface
 * using {@code Resource} as return type.
 */
public class SubjectMap implements PropertyMap {

    private final Pattern pattern = Pattern.compile("\\{(.*?)}");
    private String template;
    private List<Resource> classes = new ArrayList<>();

    public SubjectMap(String template) {
        this.template = template;
    }

    /**
     * Adds a class type to subject map.
     *
     * @param clazz to add to list.
     */
    public void addClass(Resource clazz) {
        classes.add(clazz);
    }

    @Override
    public Resource generateEntityTerm(Map<String, String> entityRow) {
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) throw new MapperException("Failed to generate template.");
        return ResourceFactory.createResource(template.replace(matcher.group(0), entityRow.get(matcher.group(1))));
    }

    @Override
    public List<Resource> listEntityClasses() {
        return Collections.unmodifiableList(classes);
    }
}
