/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.md.visualization

import spock.lang.Specification

import static com.gooddata.util.ResourceUtils.readObjectFromResource

class VisualizationClassTest extends Specification {
    private static final String TABLE_VISUALIZATION_CLASS = '/md/visualization/tableVisualizationClass.json'
    private static final String BAR_VISUALIZATION_CLASS = '/md/visualization/barVisualizationClass.json'
    private static final String EXTERNAL_VISUALIZATION_CLASS = '/md/visualization/externalVisualizationClass.json'


    private VisualizationClass visualizationClass


    def "should serialize full"() {
        when:
        visualizationClass = readObjectFromResource(TABLE_VISUALIZATION_CLASS, VisualizationClass)

        then:
        visualizationClass != null
    }


    def "getVisualizationType should return type TABLE on external md.visualization"() {
        when:
        visualizationClass = readObjectFromResource(EXTERNAL_VISUALIZATION_CLASS, VisualizationClass)
        VisualizationType type = visualizationClass.getVisualizationType()

        then:
        type == VisualizationType.TABLE
    }

    def "getVisualizationType should return type TABLE"() {
        when:
        visualizationClass = readObjectFromResource(TABLE_VISUALIZATION_CLASS, VisualizationClass)
        VisualizationType type = visualizationClass.getVisualizationType()

        then:
        type == VisualizationType.TABLE
    }

    def "getVisualizationType should return type BAR"() {
        when:
        visualizationClass = readObjectFromResource(BAR_VISUALIZATION_CLASS, VisualizationClass)
        VisualizationType type = visualizationClass.getVisualizationType()

        then:
        type == VisualizationType.BAR
    }
}
