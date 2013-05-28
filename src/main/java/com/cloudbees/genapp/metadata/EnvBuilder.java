package com.cloudbees.genapp.metadata;

import java.io.*;
import java.util.*;

public class EnvBuilder {

    private boolean safe;
    private boolean deprecated;
    private Metadata metadata;
    private static final List<String> deprecatedKeys = Arrays.asList("^MYSQL_.*$");

    public EnvBuilder(boolean safe, boolean deprecated, Metadata metadata) {
        this.safe = safe;
        this.deprecated = deprecated;
        this.metadata = metadata;
    }

    private List<String> getProperties() {
        Vector<String> properties = new Vector<String>();
        for (Iterator<Map.Entry<String, String>> fields = metadata.getEnvironment().entrySet().iterator();
             fields.hasNext(); ) {
            Map.Entry<String, String> field = fields.next();
            String fieldKey = field.getKey();
            String fieldValue = field.getValue();
            if (!deprecated) {
                boolean isDeprecated = false;
                for (String deprecatedKey : deprecatedKeys) {
                    if (fieldKey.matches(deprecatedKey)) {
                        isDeprecated = true;
                    }
                }
                if (!isDeprecated) {
                    properties.add(formatProperty(fieldKey, fieldValue));
                }
            } else {
                properties.add(formatProperty(fieldKey, fieldValue));
            }
        }
        return properties;
    }

    private String formatProperty(String fieldKey, String fieldValue) {
        if (!safe) {
            // Remove whitespace from name and quotes the value.
            // This typically still returns bash-unsafe key-value pairs.
            return fieldKey + "=" + fieldValue;
        } else {
            // This returns bash-safe variables.
            // First, we remove any illegal characters
            fieldKey = fieldKey.replaceAll("[^a-zA-Z0-9_]+", "_");
            // In case the variable name is starting with a digit, we add in an underscore instead.
            if (fieldKey.matches("^[0-9].*$")) {
                fieldKey = "_" + fieldKey;
            }
            // All return characters should be escaped for bash
            fieldValue = fieldValue.replaceAll("\\n", "\\ \n");
            return fieldKey + "='" + fieldValue + "'";
        }
    }

    public void writeControlFile(String controlPath) throws IOException {
        Map<String, String> env = System.getenv();
        String controlAbsolutePath = env.get("control_dir") + controlPath;
        File controlFile = new File(controlAbsolutePath);

        if (!controlFile.exists())
            controlFile.createNewFile();

        List<String> envProperties = getProperties();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(controlFile));
        for (String line : envProperties) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }
}
