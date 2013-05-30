package com.cloudbees.genapp.metadata.resource;

 /*
 * Copyright 2010-2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.*;

/**
 * The Resource class stores a Genapp resource characterized by descriptors and parameters.
 * There are only two mandatory descriptors: name and type.
 */

public class Resource {
    public static final String NAME_DESCRIPTOR = "resource_name";
    public static final String TYPE_DESCRIPTOR = "resource_type";

    private Map<String, String> descriptors = new HashMap<String, String>();
    private Map<String, String> properties = new HashMap<String, String>();

    /**
     * Checks if a given metadata entry is a resource.
     * @param properties A map of key-value pairs for all the properties.
     * @param descriptors A map of key-value pairs for all descriptors (keys ~ __*__ in the metadata)
     * @return A boolean, true if the metadata section defined a resource.
     */

    private static boolean checkResource(Map<String, String> properties, Map<String, String> descriptors) {
        boolean isValid;
        if (isValid = descriptors != null){
            isValid = descriptors.get(NAME_DESCRIPTOR) != null && descriptors.get(TYPE_DESCRIPTOR) != null;
            if (isValid = isValid && properties != null) {
                for (Map.Entry<String, String> property : properties.entrySet()) {
                    isValid = isValid && property.getKey() != null && property.getValue() != null;
                }
                for (Map.Entry<String, String> descriptor : descriptors.entrySet()) {
                    isValid = isValid && descriptor.getKey() != null && descriptor.getValue() != null;
                }
            }
        }
        return isValid;
    }

    /**
     * Constructs a new resource, given the properties and descriptors.
     * @param properties A map of key-value pairs for all the properties.
     * @param descriptors A map of key-value pairs for all descriptors (keys ~ __*__ in the metadata)
     */

    protected Resource(Map<String, String> properties, Map<String, String> descriptors) {
        if (!checkResource(properties, descriptors)) {
            throw new IllegalArgumentException("Incorrect resource definition.");
        }
        this.descriptors.putAll(descriptors);
        this.properties.putAll(properties);
    }

    public String getName() {
        return descriptors.get(NAME_DESCRIPTOR);
    }

    public String getType() {
        return descriptors.get(TYPE_DESCRIPTOR);
    }

    public String getDescriptor(String descriptor) {
        return descriptors.get(descriptor);
    }

    public Map<String, String> getDescriptors() {
        return descriptors;
    }

    public String getProperty(String property) {
        return  properties.get(property);
    }

    public String getProperty(String property, String defaultValue) {
        String value = properties.get(property);
        if(value == null) {
            value = defaultValue;
        }
        return value;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Gets all resources matching particular keys.
     * @param filter A list of keys to find in the properties.
     * @return The properties associated to the specified filters.
     */

    public Map<String, String> filterProperties(List<String> filter) {
        Map<String, String> filteredProperties = new HashMap<String, String>();
        for (String propertyName : filter) {
            if (properties.containsKey(propertyName)){
                filteredProperties.put(propertyName, properties.get(propertyName));
            }
        }
        return filteredProperties;
    }

    /**
     * The Builder class instanciates new Resources.
     */

    public static class Builder {

        /**
         * Creates a new resource from a given metadata section. Returns null if the input doesn't represent a Resource.
         * Otherwise returns a Resource, or a subclass thereof, depending on which kind of resource was detected.
         * @param metadata A metadata section, given as a Map of key-value pairs.
         * @return A new Resource, or Resource subclass if applicable.
         */

        public static Resource buildResource(Map<String, String> metadata) {
            Map<String, String> properties = new HashMap<String, String>();
            Map<String, String> descriptors = new HashMap<String, String>();

            // We separe the metadata into properties and descriptors.
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                String entryKey = entry.getKey();
                String entryValue = entry.getValue();
                if (entryKey != null && entryValue != null) {
                    if (entryKey.matches("^__.*__$")) {
                        entryKey = entryKey.replaceAll("^__", "").replaceAll("__$", "");
                        descriptors.put(entryKey, entryValue);
                    } else {
                        properties.put(entryKey, entryValue);
                    }
                }
            }

            /**
             * We then check if the resource fits one of the subclasses' requirements.
             * If it does, we then return a subclass of Resource appropriate to the detected type.
             */

            if(Resource.checkResource(properties, descriptors)) {
                Resource resource = new Resource(properties, descriptors);
                if (Database.TYPES.contains(resource.getType())) {
                    if (Database.checkResource(resource))
                        resource = new Database(resource);
                } else if (Email.TYPES.contains(resource.getType())) {
                    if (Email.checkResource(resource))
                        resource = new Email(resource);
                } else if (SessionStore.TYPES.contains(resource.getType())) {
                if (SessionStore.checkResource(resource))
                    resource = new SessionStore(resource);
                }
                return resource;
            }
            else
                return null;
        }
    }
}