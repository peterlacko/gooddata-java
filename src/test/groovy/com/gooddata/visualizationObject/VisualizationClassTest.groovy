/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.visualizationObject;

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.FileUtils
import spock.lang.Specification

class VisualizationClassTest extends Specification {
    private static final String TABLE_VISUALIZATION_CLASS = "/visualizationObject/tableVisualizationClass.json"
    private static final String BAR_VISUALIZATION_CLASS = "/visualizationObject/barVisualizationClass.json"
    private static final String EXTERNAL_VISUALIZATION_CLASS = "/visualizationObject/externalVisualizationClass.json"
    ObjectMapper MAPPER = new ObjectMapper()


    private VisualizationClass visualizationClass


    def "should serialize full"() {
        when:
        visualizationClass = getVisualizationClass(TABLE_VISUALIZATION_CLASS)

        then:
        visualizationClass != null
    }


    def "getVisualizationType should return type TABLE on external visualization"() {
        when:
        visualizationClass = getVisualizationClass(EXTERNAL_VISUALIZATION_CLASS)
        VisualizationType type = visualizationClass.getVisualizationType()

        then:
        type == VisualizationType.TABLE
    }

    def "getVisualizationType should return type TABLE"() {
        when:
        visualizationClass = getVisualizationClass(TABLE_VISUALIZATION_CLASS)
        VisualizationType type = visualizationClass.getVisualizationType()

        then:
        type == VisualizationType.TABLE
    }

    def "getVisualizationType should return type BAR"() {
        when:
        visualizationClass = getVisualizationClass(BAR_VISUALIZATION_CLASS)
        VisualizationType type = visualizationClass.getVisualizationType()

        then:
        type == VisualizationType.BAR
    }

    VisualizationClass getVisualizationClass(String file) {
        URL fileUrl = VisualizationClass.class.getResource(file)
        final String VISUALIZATION_CLASS_STRING = FileUtils.readFileToString(
                new File(fileUrl.toURI()),
                "utf-8")
        JsonNode node = MAPPER.readTree(VISUALIZATION_CLASS_STRING)
        return MAPPER.convertValue(node, VisualizationClass.class)
    }
}
