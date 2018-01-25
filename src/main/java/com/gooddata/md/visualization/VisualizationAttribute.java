/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gooddata.executeafm.ObjQualifier;
import com.gooddata.executeafm.afm.AttributeItem;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisualizationAttribute extends AttributeItem implements BucketItem {

    private static final long serialVersionUID = -5144496152695494774L;
    static final String NAME = "visualizationAttribute";

    @JsonCreator
    public VisualizationAttribute(@JsonProperty("displayForm") final ObjQualifier displayForm,
                                  @JsonProperty("localIdentifier") final String localIdentifier,
                                  @JsonProperty("alias") final String alias) {
        super(displayForm, localIdentifier, alias);
    }
}
