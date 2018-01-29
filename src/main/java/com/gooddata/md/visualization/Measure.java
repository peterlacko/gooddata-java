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

import java.util.Objects;


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

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonIgnore
    public boolean isPop() {
        return getDefinition() instanceof PopMeasureDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measure measure = (Measure) o;
        return super.equals(measure) && Objects.equals(title, measure.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title);
    }

    @JsonIgnore
    public boolean hasComputeRatio() {

        return getDefinition() instanceof VOSimpleMeasureDefinition && ((VOSimpleMeasureDefinition) getDefinition()).hasComputeRatio();
    }
}
