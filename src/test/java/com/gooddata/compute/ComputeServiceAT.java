/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.compute;

import com.gooddata.AbstractGoodDataAT;
import com.gooddata.compute.afm.AttributeItem;
import com.gooddata.compute.afm.ObjectAfm;
import org.testng.annotations.Test;

public class ComputeServiceAT extends AbstractGoodDataAT {

    @Test(groups = "compute", dependsOnGroups = "dataset", enabled = false)
    public void testExecute() throws Exception {
        final Computation computation = new Computation(new ObjectAfm()
                .addAttribute(new AttributeItem(new ObjIdentifierQualifier("attr.person.name")))
                .addAttribute(new AttributeItem(new ObjIdentifierQualifier("attr.person.role"))));
        gd.getComputeService().compute(project, computation).get();
    }
}