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
 * This class stores properties for a given section (i.e. java, tomcat7, glassfish3, etc...)
 */

public class RuntimeProperty {

    private Map<String, String> parameters;
    private String section;

    /**
     * Create a new RuntimeProperty from the section name and a map of key-value pairs.
     * @param section The parent section of the parameters.
     * @param parameters A key-value map of the parameters.
     */

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
