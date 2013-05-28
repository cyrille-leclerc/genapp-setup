package com.cloudbees.genapp.resource;

import java.util.*;

public class Email extends Resource {

    public static final String HOST_PROPERTY = "SENDGRID_SMTP_HOST";
    public static final String USERNAME_PROPERTY = "SENDGRID_USERNAME";
    public static final String PASSWORD_PROPERTY = "SENDGRID_PASSWORD";
    public static final List<String> TYPES = Arrays.asList("email");

    static boolean checkResource(Resource resource) {
        boolean isValid;
        if (isValid = resource != null) {
            isValid = TYPES.contains(resource.getType());
            isValid = isValid && resource.getProperty(HOST_PROPERTY) != null;
            isValid = isValid && resource.getProperty(USERNAME_PROPERTY) != null;
            isValid = isValid && resource.getProperty(PASSWORD_PROPERTY) != null;
        }
        return isValid;
    }

    Email (Resource resource) {
        super(resource.getProperties(), resource.getDescriptors());
        if (!checkResource(resource))
            throw new IllegalArgumentException("Incorrect email resource definition.");
    }

    public String getHost() {
        return getProperty(HOST_PROPERTY);
    }

    public String getUsername() {
        return getProperty(USERNAME_PROPERTY);
    }

    public String getPassword() {
        return getProperty(PASSWORD_PROPERTY);
    }

}
