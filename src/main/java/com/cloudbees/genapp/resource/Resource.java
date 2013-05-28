package com.cloudbees.genapp.resource;

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

    static boolean checkResource(Map<String, String> properties, Map<String, String> descriptors) {
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

    Resource(Map<String, String> properties, Map<String, String> descriptors) {
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
        return properties.get(property);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public Map<String, String> filterProperties(List<String> filter) {
        Map<String, String> filteredProperties = new HashMap<String, String>();
        for (String propertyName : filter) {
            if (properties.containsKey(propertyName)){
                filteredProperties.put(propertyName, properties.get(propertyName));
            }
        }
        return filteredProperties;
    }

    public static class Builder {

        public static Resource buildResource(Map<String, String> metadata) {
            Map<String, String> properties = new HashMap<String, String>();
            Map<String, String> descriptors = new HashMap<String, String>();
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

    /**
     * This method compounds the hashes for all of the object's variables, creating a hash for the Resource.
     * @return A simple, non-cryptographic hash for the Resource.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        // The hash for a variable set to null is 0, and we skip the .hashCode() method call.
        result = prime * result + ((getDescriptors() == null) ? 0 : getDescriptors().hashCode());
        result = prime * result + ((getProperties() == null) ? 0 : getProperties().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object comparedObject) {

        // We check if this instance is the same as the compared Object and return true if it is.
        if (this == comparedObject)
            return true;

        // Then, if that failed, we make sure we are dealing with a Resource and return false if it's not.
        if (comparedObject == null)
            return false;
        if (getClass() != comparedObject.getClass())
            return false;

        // If it's a Resource, we check for the class' variables to match and return false if they don't.
        Resource comparedResource = (Resource) comparedObject;

        if (getDescriptors() == null) {
            if (comparedResource.getDescriptors() != null)
                return false;
        } else if (!getDescriptors().equals(comparedResource.getDescriptors())) {
            return false;
        }

        if (getProperties() == null) {
            if (comparedResource.getProperties() != null)
                return false;
        } else if (!getProperties().equals(comparedResource.getProperties())) {
            return false;
        }

        // If no difference is found then both Resources are equal and we return true.
        return true;
    }


}