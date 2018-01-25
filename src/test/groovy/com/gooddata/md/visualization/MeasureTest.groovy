/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.md.visualization

import org.apache.commons.lang3.SerializationUtils
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static spock.util.matcher.HamcrestSupport.that

class MeasureTest extends Specification {
    private static final String MULTIPLE_MEASURES_BUCKET = "md/visualization/multipleMeasuresBucket.json"

    Bucket bucket = readObjectFromResource("/$MULTIPLE_MEASURES_BUCKET", Bucket)
    Measure measureWithCr = bucket.getItems().get(0)
    Measure measureWithoutCr = bucket.getItems().get(1)
    Measure popMeasure = bucket.getItems().get(2)

    def "should check if is pop measure"() {
        expect:
        popMeasure.isPop()

        and:
        !measureWithCr.isPop()
        !measureWithoutCr.isPop()

    }

    def "should check if has compute ratio"() {
        expect:
        measureWithCr.hasComputeRatio()

        and:
        !measureWithoutCr.hasComputeRatio()
        !popMeasure.hasComputeRatio()

    }

    def "test serializable"() {
        Measure deserialized = SerializationUtils.roundtrip(measureWithCr)

        expect:
        that deserialized, jsonEquals(measureWithCr)
    }
}
