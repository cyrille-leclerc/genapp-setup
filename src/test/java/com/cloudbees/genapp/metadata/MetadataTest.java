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
package com.cloudbees.genapp.metadata;

import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class MetadataTest {

    @Test
    public void testParser() throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("metadata-1.json");
        Metadata metadata = Metadata.Builder.fromStream(in);

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("DATABASE_MAXACTIVE_MYDB", "10");
        expected.put("DATABASE_PASSWORD_MYDB", "test");
        expected.put("DATABASE_URL_MYDB", "mysql://localhost:3306/test");
        expected.put("DATABASE_USERNAME_MYDB", "test");
        expected.put("JENKINS_URL", "https://xyz.ci.cloudbees.com");
        expected.put("MYSQL_MAX_POOL_SIZE_MYDB", "10");
        expected.put("MYSQL_PASSWORD_MYDB", "pass");
        expected.put("MYSQL_URL_MYDB", "mysql://localhost:3306/test");
        expected.put("MYSQL_USERNAME_MYDB", "test");
        expected.put("SENDGRID_PASSWORD", "sendgridpassword");
        expected.put("SENDGRID_SMTP_HOST", "smtp.sendgrid.net");
        expected.put("SENDGRID_USERNAME", "cloudbees_xyz");
        expected.put("config", "production.properties");
        expected.put("max-pool-size", "10");
        expected.put("auth-realm.database", "mydb");

        Map<String, String> environment = metadata.getEnvironment();
        Assert.assertThat(environment, equalTo(expected));
    }
}
