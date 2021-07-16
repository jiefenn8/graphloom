/*
 * Copyright (c) 2019 - GraphLoom contributors (github.com/jiefenn8/graphloom)
 * This software is made available under the terms of Apache License, Version 2.0.
 */

package io.github.jiefenn8.graphloom.util;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import io.github.jiefenn8.graphloom.api.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class defines the base methods in handling the serialisation of common
 * types using Gson for GraphLoom.
 */
public class GsonHelper {

    /**
     * Returns given {@link GsonBuilder} with custom type adapters registered to
     * the builder.
     *
     * @param builder to register custom type adapters to
     * @return gson builder with custom adapters
     */
    public static GsonBuilder loadTypeAdapters(GsonBuilder builder) {
        builder.registerTypeAdapter(EntityMap.class, uniqueIdJsonSerializer());
        builder.registerTypeAdapter(PropertyMap.class, uniqueIdJsonSerializer());
        builder.registerTypeAdapter(RelationMap.class, uniqueIdJsonSerializer());
        builder.registerTypeAdapter(NodeMap.class, uniqueIdJsonSerializer());
        Type pomType = new TypeToken<Map<RelationMap, NodeMap>>() {
        }.getType();
        builder.registerTypeAdapter(pomType, mapKeyObjectSerializer());
        builder.registerTypeAdapter(SourceMap.class, uniqueIdJsonSerializer());
        builder.registerTypeAdapter(RDFNode.class, rdfNodeJsonSerializer());
        builder.registerTypeAdapter(Resource.class, resourceJsonSerializer());
        builder.registerTypeAdapter(PropertyMap.class, propertyJsonSerializer());
        builder.registerTypeAdapter(Literal.class, literalJsonSerializer());
        return builder;
    }

    private static JsonSerializer<Literal> literalJsonSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(src.getString());
    }

    private static JsonSerializer<Property> propertyJsonSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(src.getURI());
    }

    private static JsonSerializer<Resource> resourceJsonSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(src.getURI());
    }

    private static JsonSerializer<RDFNode> rdfNodeJsonSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(src.toString());
    }

    private static JsonSerializer<UniqueId> uniqueIdJsonSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(src.getUniqueId());
    }

    private static JsonSerializer<Map<RelationMap, NodeMap>> mapKeyObjectSerializer() {
        return (src, typeOfSrc, context) -> {
            JsonObject object = new JsonObject();
            for (Entry<RelationMap, NodeMap> entry : src.entrySet()) {
                String relationMapId = entry.getKey().getUniqueId();
                String nodeMapId = entry.getValue().getUniqueId();
                object.addProperty(relationMapId, nodeMapId);
            }
            return object;
        };
    }
}
