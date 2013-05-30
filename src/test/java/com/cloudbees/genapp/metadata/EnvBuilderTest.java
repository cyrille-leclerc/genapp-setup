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

import com.cloudbees.genapp.metadata.resource.Resource;
import com.cloudbees.genapp.metadata.resource.RuntimeProperty;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class EnvBuilderTest {

    @Test
    public void testIsDeprecated() throws Exception {

        Metadata metadata = new Metadata(new HashMap<String, Resource>(), new HashMap<String, String>(), new HashMap<String, RuntimeProperty>());

        EnvBuilder envBuilder = new EnvBuilder(false, true, metadata);

        Assert.assertTrue(envBuilder.isDeprecated("MYSQL_DATABASE_NAME_MYDB"));
        Assert.assertFalse(envBuilder.isDeprecated("DATABASE_NAME_MYDB"));

    }

}
