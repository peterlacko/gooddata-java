/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.gooddata.executeafm.UriObjQualifier
import com.gooddata.executeafm.afm.AbsoluteDateFilter
import com.gooddata.executeafm.afm.NegativeAttributeFilter
import com.gooddata.executeafm.afm.PositiveAttributeFilter
import com.gooddata.executeafm.afm.RelativeDateFilter
import com.gooddata.md.Meta
import org.apache.commons.lang3.SerializationUtils
import org.joda.time.LocalDate
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class VisualizationObjectTest extends Specification {
    private static final String COMPLEX_VISUALIZATION = "md/visualization/complexVisualizationObject.json"
    private static final String SIMPLE_VISUALIZATION = "md/visualization/simpleVisualizationObject.json"
    private static final String STACKED_COLUMN_CHART = "md/visualization/stackedColumnChart.json"
    private static final String SEGMENTED_LINE_CHART = "md/visualization/segmentedLineChart.json"
    private static final String CUSTOM_CHART = "md/visualization/customChart.json"
    private static final String EMPTY_BUCKETS = "md/visualization/emptyBucketsVisualization.json"
    private static final String MULTIPLE_MEASURE_BUCKETS = "md/visualization/multipleMeasureBucketsVisualization.json"

    ObjectMapper mapper = new ObjectMapper()

    def "should serialize full"() {
        VisualizationAttribute attribute1 = new VisualizationAttribute(new UriObjQualifier("/uri/to/displayForm/1"), "attribute1", "attributeAlias")
        PositiveAttributeFilter positiveAttributeFilter = new PositiveAttributeFilter( new UriObjQualifier("/uri/to/displayForm/3"), ["ab", "cd"])
        NegativeAttributeFilter negativeAttributeFilter = new NegativeAttributeFilter( new UriObjQualifier("/uri/to/displayForm/3"), ["ab", "cd"])
        AbsoluteDateFilter absoluteDateFilter = new AbsoluteDateFilter( new UriObjQualifier("/uri/to/dataSet/1"), new LocalDate("2000-08-30"), new LocalDate("2017-08-07"))
        RelativeDateFilter relativeDateFilter = new RelativeDateFilter( new UriObjQualifier("/uri/to/dataSet/2"), "month", 0, -11)
        VOSimpleMeasureDefinition measureDefinition = new VOSimpleMeasureDefinition(new UriObjQualifier("/uri/to/measure/1"), "sum", false, [positiveAttributeFilter, negativeAttributeFilter, absoluteDateFilter, relativeDateFilter ])
        JsonNode objectNode = mapper.createObjectNode()
        objectNode.put("key", "value")
        objectNode.put("foo", "bar")

        expect:
        that new VisualizationObject(
                new VisualizationObject.Content(
                        new UriObjQualifier("visClass"),
                        [
                                new Bucket("bucket1", [attribute1]),
                                new Bucket("bucket2", [
                                        new Measure(
                                                measureDefinition,
                                                "measure1",
                                                "Measure 1 alias",
                                                "Measure 1",
                                                null
                                        )
                                ])
                        ],
                        [
                                positiveAttributeFilter,
                                negativeAttributeFilter,
                                absoluteDateFilter,
                                relativeDateFilter
                        ],
                        "{\"key\":\"value\"}",
                        objectNode
                ),
                new Meta("complex")
        ),
                jsonEquals(resource(COMPLEX_VISUALIZATION))
    }

    def "should serialize"() {
        expect:
        that new VisualizationObject(
                new VisualizationObject.Content(
                        new UriObjQualifier("visClass"),
                        [],
                        null,
                        null,
                        null
                ),
                new Meta("simple")
        ),
                jsonEquals(resource(SIMPLE_VISUALIZATION))

    }

    def "should return null when invalid collection requested"() {
        VisualizationObject stackedChart = readObjectFromResource("/$STACKED_COLUMN_CHART", VisualizationObject)
        VisualizationObject customChart = readObjectFromResource("/$CUSTOM_CHART", VisualizationObject)

        VisualizationAttribute fromMissingCollection = stackedChart.getAttributeFromCollection(CollectionType.TREND)
        VisualizationAttribute missingAttribute = customChart.getAttributeFromCollection(CollectionType.VIEW)

        expect:
        fromMissingCollection == null
        missingAttribute == null
    }

    def "return item in view collection"() {
        VisualizationObject visualizationObject = readObjectFromResource("/$STACKED_COLUMN_CHART", VisualizationObject)
        VisualizationAttribute view = visualizationObject.getView()

        expect:
        that view, jsonEquals(visualizationObject.getBuckets().get(0).getItems().get(0))
    }

    def "return item in stack collection"() {
        VisualizationObject visualizationObject = readObjectFromResource("/$STACKED_COLUMN_CHART", VisualizationObject)
        VisualizationAttribute stack = visualizationObject.getStack()

        expect:
        that stack, jsonEquals(visualizationObject.getBuckets().get(1).getItems().get(0))
    }

    def "return item in trend collection"() {
        VisualizationObject visualizationObject = readObjectFromResource("/$SEGMENTED_LINE_CHART", VisualizationObject)
        VisualizationAttribute stack = visualizationObject.getTrend()

        expect:
        that stack, jsonEquals(visualizationObject.getBuckets().get(0).getItems().get(0))
    }

    def "return item in segment collection"() {
        VisualizationObject visualizationObject = readObjectFromResource("/$SEGMENTED_LINE_CHART", VisualizationObject)
        VisualizationAttribute stack = visualizationObject.getSegment()

        expect:
        that stack, jsonEquals(visualizationObject.getBuckets().get(1).getItems().get(0))
    }

    def "should return null when non-existing key rerquested"() {
        VisualizationObject complexVisualization = readObjectFromResource("/$COMPLEX_VISUALIZATION", VisualizationObject)

        expect:
        complexVisualization.getItemById("foo") == "bar"

        and:
        complexVisualization.getItemById("invalid") == null
    }

    def "should return measures"() {
        VisualizationObject multipleMeasuresVisualization = readObjectFromResource("/$MULTIPLE_MEASURE_BUCKETS", VisualizationObject)
        VisualizationObject noMeasuresVisualization = readObjectFromResource("/$EMPTY_BUCKETS", VisualizationObject)

        List<Measure> multipleMeasures = multipleMeasuresVisualization.getMeasures()
        List<Measure> noMeasures = noMeasuresVisualization.getMeasures()

        expect:
        that multipleMeasures, jsonEquals(new ArrayList<>(Arrays.asList(
                multipleMeasuresVisualization.content.buckets.get(0).getItems().get(0),
                multipleMeasuresVisualization.content.buckets.get(1).getItems().get(0),
                multipleMeasuresVisualization.content.buckets.get(1).getItems().get(1),
        )))

        and:
        noMeasures.isEmpty()
    }

    def "return only simple measures"() {
        VisualizationObject multipleMeasuresVisualization = readObjectFromResource("/$MULTIPLE_MEASURE_BUCKETS", VisualizationObject)
        VisualizationObject noMeasuresVisualization = readObjectFromResource("/$EMPTY_BUCKETS", VisualizationObject)

        List<Measure> simpleMeasures = multipleMeasuresVisualization.getSimpleMeasures()
        List<Measure> noMeasures = noMeasuresVisualization.getSimpleMeasures()

        expect:
        that simpleMeasures, jsonEquals(new ArrayList<>(Arrays.asList(
                multipleMeasuresVisualization.content.buckets.get(0).getItems().get(0),
                multipleMeasuresVisualization.content.buckets.get(1).getItems().get(0)
        )))

        and:
        noMeasures.isEmpty()
    }

    def "should return attributes"() {
        VisualizationObject multipleAttributesVisualization = readObjectFromResource("/$SEGMENTED_LINE_CHART", VisualizationObject)
        VisualizationObject noAttributesVisualization = readObjectFromResource("/$EMPTY_BUCKETS", VisualizationObject)

        List<VisualizationAttribute> multipleAttributes = multipleAttributesVisualization.getAttributes()
        List<VisualizationAttribute> noAttributes = noAttributesVisualization.getAttributes()

        expect:
        that multipleAttributes, jsonEquals(new ArrayList<>(Arrays.asList(
                multipleAttributesVisualization.content.buckets.get(0).getItems().get(0),
                multipleAttributesVisualization.content.buckets.get(1).getItems().get(0)
        )))

        and:
        noAttributes.isEmpty()
    }

    def "should check if visualization object has derived measures"() {
        VisualizationObject visualizationWithPop = readObjectFromResource("/$MULTIPLE_MEASURE_BUCKETS", VisualizationObject)
        VisualizationObject visualizationWithoutPop = readObjectFromResource("/$SEGMENTED_LINE_CHART", VisualizationObject)
        VisualizationObject noMeasuresVisualization = readObjectFromResource("/$EMPTY_BUCKETS", VisualizationObject)

        expect:
        visualizationWithPop.hasDerivedMeasure()

        and:
        !visualizationWithoutPop.hasDerivedMeasure()
        !noMeasuresVisualization.hasDerivedMeasure()
    }

    def "test serializable"() {
        VisualizationObject visualizationObject = readObjectFromResource("/$COMPLEX_VISUALIZATION", VisualizationObject.class)
        VisualizationObject deserialized = SerializationUtils.roundtrip(visualizationObject)

        expect:
        that deserialized, jsonEquals(visualizationObject)
    }
}