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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gooddata.executeafm.UriObjQualifier;
import com.gooddata.executeafm.afm.*;
import com.gooddata.executeafm.resultspec.SortItem;
import com.gooddata.md.AbstractObj;
import com.gooddata.md.Meta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonTypeName(VisualizationObject.NAME)
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisualizationObject extends AbstractObj {
    static final String NAME = "visualizationObject";
    private Content content;

    @JsonCreator
    public VisualizationObject(@JsonProperty("content") Content content, @JsonProperty("meta") Meta meta) {
        super(meta);
        this.content = content;
    }

    public Content getContent() {
        return content;
    }

    public List<Bucket> getBuckets() {
        return getContent().getBuckets();
    }

    public List<Measure> getMeasures() {
        return getContent().getMeasures();
    }

    public List<Measure> getSimpleMeasures() {
        return getMeasures().stream()
                .filter(measure -> measure.getDefinition() instanceof SimpleMeasureDefinition)
                .collect(toList());
    }


    public List<MeasureItem> getMeasuresForAfm() {
        List<Measure> measures = getMeasures();

        List<MeasureItem> measuresForAfm = measures.stream()
                .map(measure -> measure.getMeasureForAfm())
                .collect(toList());
        return measuresForAfm;
    }

    public List<VisualizationAttribute> getAttributes() {
        return getContent().getAttributes();
    }

    public List<AttributeItem> getAttributesForAfm() {
        return getAttributes().stream()
                .map(attribute -> (AttributeItem) attribute)
                .collect(toList());
    }

    public VisualizationAttribute getAttributeFromCollection(CollectionType type) {
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

    public VisualizationAttribute getStack() {
        return getAttributeFromCollection(STACK);
    }

    public VisualizationAttribute getView() {
        return getAttributeFromCollection(VIEW);
    }
    public VisualizationAttribute getSegment() {
        return getAttributeFromCollection(SEGMENT);
    }
    public VisualizationAttribute getTrend() {
        return getAttributeFromCollection(TREND);
    }

    public String getVisualizationClassUri() {
        return getContent().getVisualizationClassUri();
    }

    public List<FilterItem> getFilters() {
        return getContent().getFilters();
    }

    public List<CompatibilityFilter> getCompatibilityFilters() {
        return (List<CompatibilityFilter>)(List<?>) getFilters();
    }

    public String getProperties() {
        return getContent().getProperties();
    }

    public List<SortItem> getSorts() {
        String properties = getContent().getProperties();
        try {
            JsonNode jsonProperties = getParsedProperties(properties);
            if (jsonProperties.has("sortItems")) {
                List<SortItem> sortItems = new ArrayList<>();
                Iterator<JsonNode> sortItemsIterator = jsonProperties.get("sortItems").iterator();
                ObjectMapper mapper = new ObjectMapper();
                while(sortItemsIterator.hasNext()) {
                    sortItems.add(mapper.convertValue(sortItemsIterator.next(), SortItem.class));
                }
                return sortItems;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private JsonNode getParsedProperties(String properties) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(properties, JsonNode.class);
    }

    public boolean hasDerivedMeasure() {
        List<Measure> measures = getMeasures();
        return measures.stream().anyMatch(measure -> measure.isPop() || measure.hasComputeRatio());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {
        private UriObjQualifier visualizationClass;
        private List<Bucket> buckets;
        private List<FilterItem> filters;
        private String properties;

        @JsonCreator
        public Content(@JsonProperty("visualizationClass") UriObjQualifier visualizationClass,
                       @JsonProperty("buckets") List<Bucket> buckets,
                       @JsonProperty("filters") List<FilterItem> filters,
                       @JsonProperty("properties") String properties) {

            this.visualizationClass = visualizationClass;
            this.buckets = buckets;
            this.filters = filters;
            this.properties = properties;
        }

        public List<Bucket> getBuckets() {
            return buckets;
        }

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
