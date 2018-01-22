/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.visualizationObject;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bucket {
    private String localIdentifier;
    private List<BucketItem> items;

    @JsonCreator
    public Bucket(@JsonProperty("localIdentifier") final String localIdentifier,
                  @JsonProperty("items") final List<BucketItem> items) {
        this.localIdentifier = localIdentifier;
        this.items = items;
    }

    public String getLocalIdentifier() {
        return localIdentifier;
    }

    public List<BucketItem> getItems() {
        return items;
    }

    @JsonIgnore
    public VisualizationAttribute getFirstAttribute() {
        return getItems().stream()
                .filter(item -> item instanceof VisualizationAttribute)
                .map(VisualizationAttribute.class::cast)
                .findFirst()
                .orElse(null);
    }
}
