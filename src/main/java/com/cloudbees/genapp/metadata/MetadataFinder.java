package com.cloudbees.genapp.metadata;

import java.io.File;
import java.util.Map;

public class MetadataFinder {

    private Metadata metadata;

    public MetadataFinder() throws Exception {
        this(null);
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public MetadataFinder(String defaultMetadataPath) throws Exception {
        Map<String, String> env = System.getenv();
        String metadataPath;

        if (defaultMetadataPath != null)
            metadataPath = defaultMetadataPath;
        else
            metadataPath = env.get("genapp_dir") + "/metadata.json";

        // Locate genapp's metadata.json
        File metadataJson = new File(metadataPath);
        if (!metadataJson.exists())
            throw new Exception("Missing metadata file: " + metadataJson.getAbsolutePath());

        metadata = Metadata.Builder.fromFile(metadataJson);
    }
}
