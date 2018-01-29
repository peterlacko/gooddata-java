/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.md.visualization;

public enum CollectionType {
    SEGMENT,
    STACK,
    TREND,
    VIEW;

    public String getName() {
        return name().toLowerCase();
    }

    boolean isValueOf(final String type) {
        return getName().equals(type.toLowerCase());
    }
}
