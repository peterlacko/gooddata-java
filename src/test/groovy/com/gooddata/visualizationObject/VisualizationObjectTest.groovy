/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.visualizationObject

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.FileUtils
import spock.lang.Specification

class VisualizationObjectTest extends Specification {
    private static final String VISUALIZATION_OBJECT_FILENAME = "/visualizationObject/complexVisualizationObject.json"
//    private static final String VISUALIZATION_OBJECT_FILENAME = "/visualizationObject/simpleVisualizationObject.json"
    URL fileUrl = VisualizationObject.class.getResource(VISUALIZATION_OBJECT_FILENAME)
    private final String VISUALIZATION_OBJECT_STRING = FileUtils.readFileToString(
            new File(fileUrl.toURI()),
            "utf-8")

    private VisualizationObject visualizationObject

    def "should deserialize full"() {
        when:
        ObjectMapper MAPPER = new ObjectMapper()
        JsonNode node = MAPPER.readTree(VISUALIZATION_OBJECT_STRING)//.get("visualizationObject")
        visualizationObject = MAPPER.convertValue(node, VisualizationObject.class)

        then:
        visualizationObject != null
    }
}