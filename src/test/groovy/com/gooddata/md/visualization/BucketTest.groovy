/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.md.visualization

import com.gooddata.executeafm.UriObjQualifier
import org.apache.commons.lang3.SerializationUtils
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class BucketTest extends Specification {
    private static final String NO_ITEMS_BUCKET = "md/visualization/noItemsBucket.json"
    private static final String MIXED_BUCKET = "md/visualization/mixedBucket.json"
    private static final String ATTRIBUTE_BUCKET = "md/visualization/attributeBucket.json"
    private static final String MEASURE_BUCKET = "md/visualization/measureBucket.json"
    private static final String MULTIPLE_ATTRIBUTES_BUCKET = "md/visualization/multipleAttributesBucket.json"

    def "should serialize empty"() {
        Bucket noItemsBucket = readObjectFromResource("/$NO_ITEMS_BUCKET", Bucket)

        expect:
        that new Bucket("noItems", new ArrayList<BucketItem>()), jsonEquals(noItemsBucket)
    }

    def "should serialize full"() {
        expect:
        that new Bucket("attributeBucket", new ArrayList<BucketItem>(Arrays.asList(
                new VisualizationAttribute(new UriObjQualifier("/uri/to/displayForm"), "attribute", "Attribute Alias"),
                new Measure(
                        new VOSimpleMeasureDefinition( new UriObjQualifier("/uri/to/measure"), "sum", false, []),
                        "measure",
                        "Measure Alias",
                        "Measure",
                        null
                )
        ))), jsonEquals(resource(MIXED_BUCKET))
    }

    def "should return only attribute from bucket"() {
        Bucket bucket = readObjectFromResource("/$resource", Bucket)
        BucketItem bucketItem = index == null ? null : bucket.getItems().get(index)

        expect:
        that bucket.getOnlyAttribute(), jsonEquals(bucketItem)

        where:
        // exactly one attributeItem in bucket is required
        resource << [NO_ITEMS_BUCKET, ATTRIBUTE_BUCKET, MEASURE_BUCKET, MULTIPLE_ATTRIBUTES_BUCKET]
        index << [null, 0, null, null]
    }

    def "test serializable"() {
        Bucket bucket = readObjectFromResource("/$MIXED_BUCKET", Bucket.class)
        Bucket deserialized = SerializationUtils.roundtrip(bucket)

        expect:
        that deserialized, jsonEquals(bucket)
    }
}
