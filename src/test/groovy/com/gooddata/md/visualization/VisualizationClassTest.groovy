/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.md.visualization

import com.gooddata.md.Meta
import org.apache.commons.lang3.SerializationUtils
import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import static spock.util.matcher.HamcrestSupport.that

class VisualizationClassTest extends Specification {
    private static final String TABLE_VISUALIZATION_CLASS = '/md/visualization/tableVisualizationClass.json'
    private static final String BAR_VISUALIZATION_CLASS = '/md/visualization/barVisualizationClass.json'
    private static final String EXTERNAL_VISUALIZATION_CLASS = '/md/visualization/externalVisualizationClass.json'

    def "should serialize full"() {
        VisualizationClass table = readObjectFromResource(TABLE_VISUALIZATION_CLASS, VisualizationClass)
        VisualizationClass external = readObjectFromResource(EXTERNAL_VISUALIZATION_CLASS, VisualizationClass)

        expect:
        that new VisualizationClass(
                new VisualizationClass.Content("local:table", "icon", "iconSelected", "checksum", 0),
                new Meta("visClass")
        ),
                jsonEquals(table)

        and:
        that new VisualizationClass(
                new VisualizationClass.Content("https://some.vis", "icon", "iconSelected", "checksum", 0),
                new Meta("external")
        ),
                jsonEquals(external)
    }

    def "should check if visualization is local"() {
        VisualizationClass visualizationClass = readObjectFromResource(resource, VisualizationClass)

        expect:
        visualizationClass.isLocal() == expected

        where:
        resource << [EXTERNAL_VISUALIZATION_CLASS, TABLE_VISUALIZATION_CLASS]
        expected << [false, true]
    }

    def "should return correct visualization type"() {
        VisualizationClass visualizationClass = readObjectFromResource(resource, VisualizationClass)

        expect:
        visualizationClass.getVisualizationType() == expected

        where:
        resource << [EXTERNAL_VISUALIZATION_CLASS, TABLE_VISUALIZATION_CLASS, BAR_VISUALIZATION_CLASS]
        expected << [VisualizationType.TABLE, VisualizationType.TABLE, VisualizationType.BAR]
    }

    def "test serializable"() {
        VisualizationClass visualizationClass = readObjectFromResource(TABLE_VISUALIZATION_CLASS, VisualizationClass.class)
        VisualizationClass deserialized = SerializationUtils.roundtrip(visualizationClass)

        expect:
        that deserialized, jsonEquals(visualizationClass)
    }
}
