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
import com.gooddata.executeafm.afm.FilterItem;

import java.util.List;

import static com.gooddata.md.visualization.VOSimpleMeasureDefinition.NAME;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(NAME)
public class VOSimpleMeasureDefinition extends com.gooddata.executeafm.afm.SimpleMeasureDefinition {

    private static final long serialVersionUID = 8467311354259963694L;
    public static final String NAME = "measureDefinition";

    @JsonCreator
    public VOSimpleMeasureDefinition(@JsonProperty("item") final ObjQualifier item,
                                     @JsonProperty("aggregation") final String aggregation,
                                     @JsonProperty("computeRatio") final Boolean computeRatio,
                                     @JsonProperty("filters") final List<FilterItem> filters) {
        super(item, aggregation, computeRatio, filters);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        return super.equals(obj);
    }
}
