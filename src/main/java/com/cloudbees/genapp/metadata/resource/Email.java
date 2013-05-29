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
 * Stores a SMTP server as a Resource. Typically a SendGrid endpoint.
 */

public class Email extends Resource {

    public static final String HOST_PROPERTY = "SENDGRID_SMTP_HOST";
    public static final String USERNAME_PROPERTY = "SENDGRID_USERNAME";
    public static final String PASSWORD_PROPERTY = "SENDGRID_PASSWORD";
    public static final List<String> TYPES = Arrays.asList("email");

    /**
     * Checks if a given Resource is a mail endpoint.
     * @param resource The Resource to be tested.
     * @return A boolean, true if the Resource given is a mail endpoint.
     */

    protected static boolean checkResource(Resource resource) {
        boolean isValid;
        if (isValid = resource != null) {
            isValid = TYPES.contains(resource.getType());
            isValid = isValid && resource.getProperty(HOST_PROPERTY) != null;
            isValid = isValid && resource.getProperty(USERNAME_PROPERTY) != null;
            isValid = isValid && resource.getProperty(PASSWORD_PROPERTY) != null;
        }
        return isValid;
    }

    protected Email (Resource resource) {
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
