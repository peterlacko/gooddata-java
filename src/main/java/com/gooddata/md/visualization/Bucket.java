/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.*;
import com.gooddata.executeafm.afm.LocallyIdentifiable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents bucket within {@link VisualizationObject}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bucket implements Serializable, LocallyIdentifiable {

    private static final long serialVersionUID = -7718720886547680021L;
    private String localIdentifier;
    private List<BucketItem> items;

    /**
     * Creates new instance of bucket
     * @param localIdentifier local identifier of bucket
     * @param items list of {@link BucketItem}s for this bucket
     */
    @JsonCreator
    public Bucket(@JsonProperty("localIdentifier") final String localIdentifier,
                  @JsonProperty("items") final List<BucketItem> items) {
        this.localIdentifier = localIdentifier;
        this.items = items;
    }

    /**
     * @return local identifier
     */
    public String getLocalIdentifier() {
        return localIdentifier;
    }

    /**
     * @param localIdentifier local identifier of bucket
     */
    public void setLocalIdentifier(String localIdentifier) {
        this.localIdentifier = localIdentifier;
    }

    /**
     * @return list of {@link BucketItem}s
     */
    public List<BucketItem> getItems() {
        return items;
    }

    /**
     * @param items to be set to bucket
     */
    public void setItems(List<BucketItem> items) {
        this.items = items;
    }

    @JsonIgnore
    VisualizationAttribute getOnlyAttribute() {
        VisualizationAttribute firstAttribute = getFirstAttribute();

        if (getItems().size() != 1 || firstAttribute == null) {
            return null;
        }

        return firstAttribute;
    }

    @JsonIgnore
    private VisualizationAttribute getFirstAttribute() {
        return getItems().stream()
                .filter(VisualizationAttribute.class::isInstance)
                .map(VisualizationAttribute.class::cast)
                .findFirst()
                .orElse(null);
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
