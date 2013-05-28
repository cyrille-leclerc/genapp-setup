package com.cloudbees.genapp.metadata;

import com.cloudbees.genapp.resource.Resource;
import com.cloudbees.genapp.resource.RuntimeProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

/**
 * The Metadata class stores Resources entries by name in a map.
 */

public class Metadata {
    private Map<String, Resource> resources;
    private Map<String, String> environment;
    private Map<String, RuntimeProperty> runtimeProperties;
    
    public Metadata(Map<String, Resource> resources, Map<String, String> environment,
                    Map<String, RuntimeProperty> runtimeProperties) {
        this.resources = resources;
        this.environment = environment;
        this.runtimeProperties = runtimeProperties;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public Map<String, String> getRuntimeParameters(String parent) {
        return runtimeProperties.get(parent).getParameters();
    }

    public String getRuntimeParameter(String parent, String propertyName) {
        return runtimeProperties.get(parent).getParameter(propertyName);
    }

    public static class Builder {

        /**
         * @param metadataFile The metadata.json file
         * @return A new Metadata instance, containing all resources parsed
         * from the JSON metadata given as input.
         * @throws java.io.IOException
         */
        public static Metadata fromFile(File metadataFile) throws IOException {
            FileInputStream metadataInputStream = new FileInputStream(metadataFile);
            try {
                return fromStream(metadataInputStream);
            } finally {
                metadataInputStream.close();
            }
        }

        /**
         * @param metadataInputStream An InputStream to read the JSON metadata from.
         * @return A new Metadata instance, containing all resources parsed
         * from the JSON metadata given as input.
         * @throws IOException
         */
        public static Metadata fromStream(InputStream metadataInputStream) throws IOException {
            ObjectMapper metadataObjectMapper = new ObjectMapper();

            JsonNode metadataRootNode = metadataObjectMapper.readTree(metadataInputStream);
            Builder metadataBuilder = new Builder();

            return metadataBuilder.buildResources(metadataRootNode);
        }

        /**
         * Parses resources and returns them in a new MetadataBuilder instance.
         * @param metadataRootNode The root node of the Json metadata to be parsed.
         * @return A new MetadataBuilder instance containing all parsed resources.
         **/
        private Metadata buildResources(JsonNode metadataRootNode) {
            Map<String, Resource> resources = new TreeMap<String, Resource>();
            Map<String, String> environment = new TreeMap<String,String>();
            Map<String, RuntimeProperty> runtimeProperties = new TreeMap<String, RuntimeProperty>();

            // We iterate over all the metadata fields, resources or not.
            for (Iterator<Map.Entry<String, JsonNode>> fields = metadataRootNode.fields();
                 fields.hasNext(); ) {

                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode content = entry.getValue();
                String id = entry.getKey();
                Map<String, String> entryMetadata = new HashMap<String, String>();

                // We then iterate over all the key-value pairs present
                for (Iterator<Map.Entry<String, JsonNode>> properties = content.fields();
                     properties.hasNext(); ) {
                    Map.Entry<String, JsonNode> property = properties.next();
                    String entryName = property.getKey();
                    JsonNode entryValueNode = property.getValue();

                    // We check if the entry is well-formed.
                    if (entryValueNode.isTextual() || entryValueNode.isInt()) {
                        String entryValue = entryValueNode.asText();
                        entryMetadata.put(entryName, entryValue);
                    }

                    // We get environment variables from the metadata when we iterate over "app"
                    if (id.equals("app") && entryName.equals("env")) {
                        for (Iterator<Map.Entry<String, JsonNode>> envVariables = entryValueNode.fields();
                             envVariables.hasNext();) {
                            Map.Entry<String, JsonNode> envVariable = envVariables.next();
                            String envName = envVariable.getKey();
                            JsonNode envValue = envVariable.getValue();
                            if (envValue.isTextual()) {
                                environment.put(envName, envValue.asText());
                            }
                        }
                    }
                }

                Resource resource = Resource.Builder.buildResource(entryMetadata);
                // Check if the resource was valid.
                if (resource != null) {
                    resources.put(resource.getName(), resource);
                // Otherwise, if it wasn't a resource nor the "app" field, it is composed of runtime parameters
                } else if (!id.equals("app")) {
                    runtimeProperties.put(id, new RuntimeProperty(id, entryMetadata));
                }
            }
            return new Metadata(resources, environment, runtimeProperties);
        }
    }
}