package com.cloudbees.genapp.resource;

import java.util.*;

public class RuntimeProperty {

    private Map<String, String> parameters;
    private String section;

    public RuntimeProperty (String section, Map<String, String> parameters) {
        this.parameters = parameters;
        this.section = section;
    }

    public String getSectionName() {
        return section;
    }

    public String getParameter(String parameterName) {
        return parameters.get(parameterName);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

}
