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

/**
 * Stores complete information about new visualization object that can be stored as MD to md server
 */
@JsonTypeName(VisualizationObject.NAME)
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisualizationObject extends AbstractObj implements Queryable, Updatable {
    static final String NAME = "visualizationObject";
    private Content content;

    @JsonCreator
    private VisualizationObject(@JsonProperty("content") final Content content, @JsonProperty("meta") final Meta meta) {
        super(meta);
        this.content = notNull(content);
    }

    /**
     * @return all measures from all buckets in visualization object
     */
    @JsonIgnore
    public List<Measure> getMeasures() {
        return getContent().getMeasures();
    }

    /**
     * @return all measures from all buckets whose measure definition is instance of {@link VOSimpleMeasureDefinition}
     */
    @JsonIgnore
    public List<Measure> getSimpleMeasures() {
        return getMeasures().stream()
                .filter(measure -> measure.getDefinition() instanceof VOSimpleMeasureDefinition)
                .collect(toList());
    }

    /**
     * @return all attributes from all buckets in visualization object
     */
    @JsonIgnore
    public List<VisualizationAttribute> getAttributes() {
        return getContent().getAttributes();
    }

    /**
     * Returns attribute from collection bucket, if and only if bucket contains only one item of type
     * {@link VisualizationAttribute}, null otherwise
     * @param type of collection which we want to get, stored as local identifier in each bucket
     * @return attribute from collection bucket
     */
    @JsonIgnore
    public VisualizationAttribute getAttributeFromCollection(final CollectionType type) {
        return getContent().getBuckets().stream()
                .filter(bucket -> type.isValueOf(bucket.getLocalIdentifier()))
                .findFirst()
                .map(Bucket::getOnlyAttribute)
                .orElse(null);
    }

    /**
     * @return attribute from stack collection
     */
    @JsonIgnore
    public VisualizationAttribute getStack() {
        return getAttributeFromCollection(STACK);
    }

    /**
     * @return attribute from view collection
     */
    @JsonIgnore
    public VisualizationAttribute getView() {
        return getAttributeFromCollection(VIEW);
    }

    /**
     * @return attribute from segment collection
     */
    @JsonIgnore
    public VisualizationAttribute getSegment() {
        return getAttributeFromCollection(SEGMENT);
    }

    /**
     * @return attribute from trend collection
     */
    @JsonIgnore
    public VisualizationAttribute getTrend() {
        return getAttributeFromCollection(TREND);
    }

    /**
     * @return uri to visualization class
     */
    @JsonIgnore
    public String getVisualizationClassUri() {
        return getContent().getVisualizationClassUri();
    }

    /**
     * @return buckets from visualization object
     */
    @JsonIgnore
    public List<Bucket> getBuckets() {
        return getContent().getBuckets();
    }

    /**
     * @param buckets replacing previous visualization object's buckets
     */
    @JsonIgnore
    public void setBuckets(List<Bucket> buckets) {
        content.setBuckets(buckets);
    }

    /**
     * @return filters from visualization object
     */
    @JsonIgnore
    public List<FilterItem> getFilters() {
        return getContent().getFilters();
    }

    /**
     * @param filters replacing previsous visualization object's filters
     */
    @JsonIgnore
    public void setFilters(List<FilterItem> filters) {
        content.setFilters(filters);
    }

    /**
     * @return json properties of visualization object in form of string
     */
    @JsonIgnore
    public String getProperties() {
        return getContent().getProperties();
    }

    /**
     * @param properties to be set to visualization object in form of stringified json
     */
    @JsonIgnore
    public void setProperties(String properties) {
        content.setProperties(properties);
    }

    /**
     * @return hash map of references in form localIdentifier:uri
     */
    @JsonIgnore
    public Map<String, String> getReferenceItems() {
        return getContent().getReferenceItems();
    }

    /**
     * @param referenceItems is a hash map of references in form localIdentifier:uri to be set to visualization object
     */
    @JsonIgnore
    public void setReferenceItems(Map<String, String> referenceItems) {
        content.setReferenceItems(referenceItems);
    }

    /**
     * @return uri to visualizaton class wrapped as {@link UriObjQualifier}
     */
    public UriObjQualifier getVisualizationClass() {
        return content.getVisualizationClass();
    }

    /**
     * @param uri to replace previous visualization class's uri, wrapped as {@link UriObjQualifier}
     */
    public void setVisualizationClass(UriObjQualifier uri) {
        content.setVisualizationClass(uri);
    }

    /**
     * @return true if visualization object contains at leas one PoP measure or measure with compute ratio, false otherwise
     */
    @JsonIgnore
    public boolean hasDerivedMeasure() {
        return getMeasures().stream().anyMatch(measure -> measure.isPop() || measure.hasComputeRatio());
    }

    /**
     * Method to get uri to requested local identifier from reference items
     * @param id of item
     * @return uri of requested item
     */
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
