/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import org.springframework.util.StringUtils;

import static com.gooddata.util.Validate.notNull;
import static java.lang.String.format;

public enum VisualizationType {
    TABLE,
    LINE,
    COLUMN,
    BAR,
    PIE;


    public String getName() {
        return name().toLowerCase();
    }

    public static VisualizationType get(final String type) {
        notNull(type, "type");
        try {
            return VisualizationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(
                    format("Unknown visualization type: \"%s\", supported types are: [%s]",
                            type, StringUtils.arrayToCommaDelimitedString(VisualizationType.values())),
                    e);
        }
    }
}
