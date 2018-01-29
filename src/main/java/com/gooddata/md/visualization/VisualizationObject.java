/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.*;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.gooddata.md.visualization.CollectionType.*;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.Validate.notNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.gooddata.executeafm.UriObjQualifier;
import com.gooddata.executeafm.afm.*;
import com.gooddata.md.AbstractObj;
import com.gooddata.md.Meta;
import com.gooddata.md.Queryable;
import com.gooddata.md.Updatable;

import java.io.Serializable;
import java.util.*;

@JsonTypeName(VisualizationObject.NAME)
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisualizationObject extends AbstractObj implements Queryable, Updatable {
    static final String NAME = "visualizationObject";
    private Content content;

    @JsonCreator
    public VisualizationObject(@JsonProperty("content") final Content content, @JsonProperty("meta") final Meta meta) {
        super(meta);
        this.content = notNull(content);
    }

    @JsonIgnore
    public List<Measure> getMeasures() {
        return getContent().getMeasures();
    }

    @JsonIgnore
    public List<Measure> getSimpleMeasures() {
        return getMeasures().stream()
                .filter(measure -> measure.getDefinition() instanceof VOSimpleMeasureDefinition)
                .collect(toList());
    }

    @JsonIgnore
    public List<VisualizationAttribute> getAttributes() {
        return getContent().getAttributes();
    }

    @JsonIgnore
    public VisualizationAttribute getAttributeFromCollection(final CollectionType type) {
        return getContent().getBuckets().stream()
                .filter(bucket -> type.isValueOf(bucket.getLocalIdentifier()))
                .findFirst()
                .map(Bucket::getOnlyAttribute)
                .orElse(null);
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
    public List<Bucket> getBuckets() {
        return getContent().getBuckets();
    }

    @JsonIgnore
    public void setBuckets(List<Bucket> buckets) {
        content.setBuckets(buckets);
    }

    @JsonIgnore
    public List<FilterItem> getFilters() {
        return getContent().getFilters();
    }

    @JsonIgnore
    public void setFilters(List<FilterItem> filters) {
        content.setFilters(filters);
    }

    @JsonIgnore
    public String getProperties() {
        return getContent().getProperties();
    }

    @JsonIgnore
    public void setProperties(String properties) {
        content.setProperties(properties);
    }

    @JsonIgnore
    public Map<String, String> getReferenceItems() {
        return getContent().getReferenceItems();
    }

    @JsonIgnore
    public void setReferenceItems(Map<String, String> referenceItems) {
        content.setReferenceItems(referenceItems);
    }

    public UriObjQualifier getVisualizationClass() {
        return content.getVisualizationClass();
    }

    public void setVisualizationClass(UriObjQualifier uri) {
        content.setVisualizationClass(uri);
    }

    @JsonIgnore
    public boolean hasDerivedMeasure() {
        return getMeasures().stream().anyMatch(measure -> measure.isPop() || measure.hasComputeRatio());
    }

    @JsonIgnore
    public String getItemById(String id) {
        Map<String, String> referenceItems = getReferenceItems();
        if (referenceItems != null) {
            return referenceItems.get(id);
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisualizationObject that = (VisualizationObject) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(content);
    }

    private Content getContent() {
        return content;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Content implements Serializable {

        private static final long serialVersionUID = 2895359822118041504L;
        private UriObjQualifier visualizationClass;
        private List<Bucket> buckets;
        private List<FilterItem> filters;
        private String properties;
        private Map<String, String> referenceItems;

        @JsonCreator
        public Content(@JsonProperty("visualizationClass") final UriObjQualifier visualizationClass,
                       @JsonProperty("buckets") final List<Bucket> buckets,
                       @JsonProperty("filters") final List<FilterItem> filters,
                       @JsonProperty("properties") final String properties,
                       @JsonProperty("references") final Map<String, String> referenceItems) {

            this.visualizationClass = notNull(visualizationClass);
            this.buckets = notNull(buckets);
            this.filters = filters;
            this.properties = properties;
            this.referenceItems = referenceItems;
        }

        public List<Bucket> getBuckets() {
            return buckets;
        }

        @JsonIgnore
        public List<VisualizationAttribute> getAttributes() {
            return buckets.stream()
                    .flatMap(bucket -> bucket.getItems().stream())
                    .filter(VisualizationAttribute.class::isInstance)
                    .map(VisualizationAttribute.class::cast)
                    .collect(toList());
        }

        public void setVisualizationClass(UriObjQualifier visualizationClass) {
            this.visualizationClass = visualizationClass;
        }

        public void setBuckets(List<Bucket> buckets) {
            this.buckets = buckets;
        }

        public void setFilters(List<FilterItem> filters) {
            this.filters = filters;
        }

        public void setProperties(String properties) {
            this.properties = properties;
        }

        public void setReferenceItems(Map<String, String> referenceItems) {
            this.referenceItems = referenceItems;
        }

        @JsonIgnore
        public List<Measure> getMeasures() {
            return buckets.stream()
                    .flatMap(bucket -> bucket.getItems().stream())
                    .filter(Measure.class::isInstance)
                    .map(Measure.class::cast)
                    .collect(toList());
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

        public UriObjQualifier getVisualizationClass() {
            return visualizationClass;
        }

        public Map<String, String> getReferenceItems() {
            return referenceItems;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Content content = (Content) o;
            return Objects.equals(visualizationClass, content.visualizationClass) &&
                    Objects.equals(buckets, content.buckets) &&
                    Objects.equals(filters, content.filters) &&
                    Objects.equals(properties, content.properties) &&
                    Objects.equals(referenceItems, content.referenceItems);
        }

        @Override
        public int hashCode() {

            return Objects.hash(visualizationClass, buckets, filters, properties, referenceItems);
        }

    }
}
