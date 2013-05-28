package com.cloudbees.genapp.metadata;

import java.io.File;

public interface ConfigurationBuilder {
    public void writeConfiguration(Metadata metadata, File configurationFile) throws Exception;
    public ConfigurationBuilder create(Metadata metadata);
}
