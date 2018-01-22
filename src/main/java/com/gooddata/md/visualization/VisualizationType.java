/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import com.gooddata.md.report.Total;
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
        notNull(type, "total");
        try {
            return VisualizationType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(
                    format("Unknown value for Grid's total: \"%s\", supported values are: [%s]",
                            type, StringUtils.arrayToCommaDelimitedString(Total.values())),
                    e);
        }
    }
}
