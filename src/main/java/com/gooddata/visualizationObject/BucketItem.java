/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */


package com.gooddata.visualizationObject;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Measure.class, name = Measure.NAME),
        @JsonSubTypes.Type(value = VisualizationAttribute.class, name = VisualizationAttribute.NAME)
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface BucketItem {}

