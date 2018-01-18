/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */


package com.gooddata.visualizationObject;

import com.fasterxml.jackson.annotation.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.gooddata.visualizationObject.CollectionType.*;
import static java.util.stream.Collectors.toList;

import com.gooddata.executeafm.UriObjQualifier;
import com.gooddata.executeafm.afm.*;
import com.gooddata.md.AbstractObj;
import com.gooddata.md.Meta;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName(VisualizationObject.NAME)
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisualizationObject extends AbstractObj {
    static final String NAME = "visualizationObject";
    private Content content;

    @JsonCreator
    public VisualizationObject(@JsonProperty("content") final Content content, @JsonProperty("meta") final Meta meta) {
        super(meta);
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    @JsonIgnore
    public List<Bucket> getBuckets() {
        return getContent().getBuckets();
    }

    @JsonIgnore
    public List<Measure> getMeasures() {
        return getContent().getMeasures();
    }

    @JsonIgnore
    public List<Measure> getSimpleMeasures() {
        return getMeasures().stream()
                .filter(measure -> measure.getDefinition() instanceof SimpleMeasureDefinition)
                .collect(toList());
    }

    @JsonIgnore
    public List<VisualizationAttribute> getAttributes() {
        return getContent().getAttributes();
    }

    @JsonIgnore
    public VisualizationAttribute getAttributeFromCollection(final CollectionType type) {
        Bucket collectionBucket = getContent().getBuckets().stream()
                .filter(bucket -> bucket.getLocalIdentifier().equals(type.toString()))
                .findFirst()
                .orElse(null);
        if (collectionBucket == null) {
            return null;
        }

        VisualizationAttribute attribute = collectionBucket.getFirstAttribute();

        if (collectionBucket.getItems().size() != 1 || attribute == null) {
           return null;
        }

        return attribute;
    }

    @JsonIgnore
    public VisualizationAttribute getStack() {
        return getAttributeFromCollection(STACK);
    }

    @JsonIgnore
    public VisualizationAttribute getView() {
        return getAttributeFromCollection(VIEW);
    }

    @JsonIgnore
    public VisualizationAttribute getSegment() {
        return getAttributeFromCollection(SEGMENT);
    }

    @JsonIgnore
    public VisualizationAttribute getTrend() {
        return getAttributeFromCollection(TREND);
    }

    @JsonIgnore
    public String getVisualizationClassUri() {
        return getContent().getVisualizationClassUri();
    }

    @JsonIgnore
    public List<FilterItem> getFilters() {
        return getContent().getFilters();
    }

    @JsonIgnore
    public String getProperties() {
        return getContent().getProperties();
    }

    @JsonIgnore
    public boolean hasDerivedMeasure() {
        List<Measure> measures = getMeasures();
        return measures.stream().anyMatch(measure -> measure.isPop() || measure.hasComputeRatio());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Content {
        private UriObjQualifier visualizationClass;
        private List<Bucket> buckets;
        private List<FilterItem> filters;
        private String properties;

        @JsonCreator
        public Content(@JsonProperty("visualizationClass") final UriObjQualifier visualizationClass,
                       @JsonProperty("buckets") final List<Bucket> buckets,
                       @JsonProperty("filters") final List<FilterItem> filters,
                       @JsonProperty("properties") final String properties) {

            this.visualizationClass = visualizationClass;
            this.buckets = buckets;
            this.filters = filters;
            this.properties = properties;
        }

        public List<Bucket> getBuckets() {
            return buckets;
        }

        @JsonIgnore
        public List<VisualizationAttribute> getAttributes() {
            List<Bucket> buckets = getBuckets();

            final List<VisualizationAttribute> attributes = new ArrayList<>();
            for(Bucket bucket: buckets) {
                List<BucketItem> items = bucket.getItems();
                for(BucketItem item: items) {
                    if (item instanceof VisualizationAttribute) {
                        attributes.add((VisualizationAttribute) item);
                    }
                }
            }

            return attributes;
        }

        @JsonIgnore
        public List<Measure> getMeasures() {
            final List<Measure> measures = new ArrayList<>();
            for(Bucket bucket: buckets) {
                List<BucketItem> items = bucket.getItems();
                for(BucketItem item: items) {
                    if (item instanceof Measure) {
                        measures.add((Measure) item);
                    }
                }
            }

            return measures;
        }

        @JsonIgnore
        public String getVisualizationClassUri() {
            return visualizationClass.getUri();
        }

        public List<FilterItem> getFilters() {
            return filters;
        }

        public String getProperties() {
            return properties;
        }

    }
}
