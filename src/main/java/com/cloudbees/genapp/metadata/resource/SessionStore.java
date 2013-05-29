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
 * This class stores Membase distributed session stores.
 */

public class SessionStore extends Resource {

    public static final String NODES_PROPERTY = "servers";
    public static final String USERNAME_PROPERTY = "username";
    public static final String PASSWORD_PROPERTY = "password";
    public static final List<String> TYPES = Arrays.asList("session-store");

    /**
     * Checks if a Resource is a session store.
     * @param resource The Resource to be checked.
     * @return A boolean indicating if the Resource is a session store.
     */
    protected static boolean checkResource(Resource resource) {
        boolean isValid;
        if (isValid = resource != null) {
            isValid = TYPES.contains(resource.getType());
            isValid = isValid && resource.getProperty(NODES_PROPERTY) != null;
            isValid = isValid && resource.getProperty(USERNAME_PROPERTY) != null;
            isValid = isValid && resource.getProperty(PASSWORD_PROPERTY) != null;
        }
        return isValid;
    }

    /**
     * Creates a new SessionStore from an existing Resource.
     * @param resource
     */

    protected SessionStore (Resource resource) {
        super(resource.getProperties(), resource.getDescriptors());
        if (!checkResource(resource))
            throw new IllegalArgumentException("Incorrect session store resource definition.");
    }

    /**
     * Returns a list of the membase nodes for this session store.
     * @return A comma-delimited list of membase nodes' pools.
     */

    public String getNodes() {
        String nodes = "";
        String[] nodeArray = getProperty(NODES_PROPERTY).split(",");
        for (int i = 0; i < nodeArray.length; i++) {
            if (i != 0)
                nodes += ",";
            nodes += "http://" + nodeArray[i] + ":8091/pools";
        }
        return nodes;
    }

    public String getUsername() {
        return getProperty(USERNAME_PROPERTY);
    }

    public String getPassword() {
        return getProperty(PASSWORD_PROPERTY);
    }
}
