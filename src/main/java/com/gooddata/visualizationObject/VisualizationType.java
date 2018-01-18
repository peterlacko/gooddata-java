/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.visualizationObject;

public enum VisualizationType {
    TABLE,
    LINE,
    COLUMN,
    BAR,
    PIE,

    // fallback to prevent 500
    UNKNOWN;

    public String toString() {
        return name().toLowerCase();
    }

    public static VisualizationType get(final String type) {
        for (VisualizationType vizType : VisualizationType.values()) {
            if (vizType.toString().equalsIgnoreCase(type)) {
                return vizType;
            }
        }
        return UNKNOWN;
    }
}
