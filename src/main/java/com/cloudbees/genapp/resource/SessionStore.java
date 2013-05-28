package com.cloudbees.genapp.resource;

import java.util.*;

public class SessionStore extends Resource {

    public static final String NODES_PROPERTY = "servers";
    public static final String USERNAME_PROPERTY = "username";
    public static final String PASSWORD_PROPERTY = "password";
    public static final List<String> TYPES = Arrays.asList("session-store");

    static boolean checkResource(Resource resource) {
        boolean isValid;
        if (isValid = resource != null) {
            isValid = TYPES.contains(resource.getType());
            isValid = isValid && resource.getProperty(NODES_PROPERTY) != null;
            isValid = isValid && resource.getProperty(USERNAME_PROPERTY) != null;
            isValid = isValid && resource.getProperty(PASSWORD_PROPERTY) != null;
        }
        return isValid;
    }

    SessionStore (Resource resource) {
        super(resource.getProperties(), resource.getDescriptors());
        if (!checkResource(resource))
            throw new IllegalArgumentException("Incorrect session store resource definition.");
    }

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
