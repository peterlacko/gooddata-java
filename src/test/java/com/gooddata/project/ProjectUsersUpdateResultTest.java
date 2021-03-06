/*
 * Copyright (C) 2004-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.project;

import static com.gooddata.util.ResourceUtils.readObjectFromResource;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

import java.util.Arrays;

public class ProjectUsersUpdateResultTest {

    @Test
    public void testDeserialize() {
        final ProjectUsersUpdateResult project = readObjectFromResource("/project/projectUsersUpdateResult.json", ProjectUsersUpdateResult.class);

        assertThat(project.getFailed(), is(emptyList()));
        assertThat(project.getSuccessful(), is(Arrays.asList("/gdc/account/profile/1")));
    }

}
