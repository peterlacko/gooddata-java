/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.visualizationObject;

public enum CollectionType {
    SEGMENT,
    STACK,
    TREND,
    VIEW;

    public String toString() {
        return name().toLowerCase();
    }
}
