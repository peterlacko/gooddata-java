/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.visualizationObject

import com.gooddata.executeafm.UriObjQualifier
import com.gooddata.executeafm.afm.AbsoluteDateFilter
import com.gooddata.executeafm.afm.NegativeAttributeFilter
import com.gooddata.executeafm.afm.PositiveAttributeFilter
import com.gooddata.executeafm.afm.RelativeDateFilter
import com.gooddata.md.Meta
import org.joda.time.LocalDate
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource
import static spock.util.matcher.HamcrestSupport.that

class VisualizationObjectTest extends Specification {
    private static final String COMPLEX_VISUALIZATION_OBJECT = "visualizationObject/complexVisualizationObject.json"

    def "should serialize full"() {
        VisualizationAttribute attribute1 = new VisualizationAttribute(new UriObjQualifier("/uri/to/displayForm/1"), "attribute1", "attributeAlias")
        PositiveAttributeFilter positiveAttributeFilter = new PositiveAttributeFilter( new UriObjQualifier("/uri/to/displayForm/3"), ["ab", "cd"])
        NegativeAttributeFilter negativeAttributeFilter = new NegativeAttributeFilter( new UriObjQualifier("/uri/to/displayForm/3"), ["ab", "cd"])
        AbsoluteDateFilter absoluteDateFilter = new AbsoluteDateFilter( new UriObjQualifier("/uri/to/dataSet/1"), new LocalDate("2000-08-30"), new LocalDate("2017-08-07"))
        RelativeDateFilter relativeDateFilter = new RelativeDateFilter( new UriObjQualifier("/uri/to/dataSet/2"), "month", 0, -11)
        SimpleMeasureDefinition measureDefinition = new SimpleMeasureDefinition(new UriObjQualifier("/uri/to/measure/1"), "sum", false, [ positiveAttributeFilter, negativeAttributeFilter, absoluteDateFilter, relativeDateFilter ])

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
                        "{\"key\":\"value\"}"
                ),
                new Meta("New insight")
        ),
                jsonEquals(resource(COMPLEX_VISUALIZATION_OBJECT))
    }
}