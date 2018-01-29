/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.gooddata.executeafm.ObjQualifier;

import static com.gooddata.md.visualization.VOPopMeasureDefinition.NAME;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(NAME)
public class VOPopMeasureDefinition extends com.gooddata.executeafm.afm.PopMeasureDefinition {

    private static final long serialVersionUID = -2727004914980057124L;
    public static final String NAME = "popMeasureDefinition";

    @JsonCreator
    public VOPopMeasureDefinition(@JsonProperty("measureIdentifier") final String measureIdentifier,
                                  @JsonProperty("popAttribute") final ObjQualifier popAttribute) {
        super(measureIdentifier, popAttribute);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        return super.equals(obj);
    }
}
