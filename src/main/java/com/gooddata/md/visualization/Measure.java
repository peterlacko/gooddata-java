/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gooddata.executeafm.afm.MeasureDefinition;
import com.gooddata.executeafm.afm.MeasureItem;
import com.gooddata.executeafm.afm.PopMeasureDefinition;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Measure extends MeasureItem implements BucketItem {

    private static final long serialVersionUID = -6311373783004640731L;
    static final String NAME = "measure";

    private String title;

    @JsonCreator
    public Measure(@JsonProperty("definition") final MeasureDefinition definition,
                   @JsonProperty("localIdentifier") final String localIdentifier,
                   @JsonProperty("alias") final String alias,
                   @JsonProperty("title") final String title,
                   @JsonProperty("format") final String format) {
        super(definition, localIdentifier, alias, format);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @JsonIgnore
    public boolean isPop() {
        return getDefinition() instanceof PopMeasureDefinition;
    }

    @JsonIgnore
    public boolean hasComputeRatio() {

        return getDefinition() instanceof VOSimpleMeasureDefinition && ((VOSimpleMeasureDefinition) getDefinition()).hasComputeRatio();
    }
}
