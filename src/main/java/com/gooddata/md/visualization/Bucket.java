/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bucket implements Serializable {

    private static final long serialVersionUID = -7718720886547680021L;
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

    public void setItems(List<BucketItem> items) {
        this.items = items;
    }

    public void setLocalIdentifier(String localIdentifier) {

        this.localIdentifier = localIdentifier;
    }

    @JsonIgnore
    private VisualizationAttribute getFirstAttribute() {
        return getItems().stream()
                .filter(VisualizationAttribute.class::isInstance)
                .map(VisualizationAttribute.class::cast)
                .findFirst()
                .orElse(null);
    }

    @JsonIgnore
    public VisualizationAttribute getOnlyAttribute() {
        VisualizationAttribute firstAttribute = getFirstAttribute();

        if (getItems().size() != 1 || firstAttribute == null) {
            return null;
        }

        return firstAttribute;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bucket bucket = (Bucket) o;
        return Objects.equals(localIdentifier, bucket.localIdentifier) &&
                Objects.equals(items, bucket.items);
    }

    @Override
    public int hashCode() {

        return Objects.hash(localIdentifier, items);
    }
}
