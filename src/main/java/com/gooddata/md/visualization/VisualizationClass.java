/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */

package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.*;
import com.gooddata.md.AbstractObj;
import com.gooddata.md.Meta;
import com.gooddata.md.Queryable;
import com.gooddata.md.Updatable;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.WRAPPER_OBJECT;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static org.apache.commons.lang3.Validate.notNull;

@JsonTypeName(VisualizationClass.NAME)
@JsonTypeInfo(include = WRAPPER_OBJECT, use = NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisualizationClass extends AbstractObj implements Queryable, Updatable {

    private static final long serialVersionUID = -72785788784079208L;
    static final String NAME = "visualizationClass";

    private Content content;

    public VisualizationClass(@JsonProperty("content") final Content content, @JsonProperty("meta") final Meta meta ) {
        super(meta);
        this.content = notNull(content);
    }

    public Content getContent()  {
        return content;
    }

    @JsonIgnore
    public VisualizationType getVisualizationType() {
        String uriParts[] = getContent().getUrl().split(":");
        String derivedType = uriParts[uriParts.length-1];

        VisualizationType visualizationType = VisualizationType.TABLE;

        if (isLocal()) {
            visualizationType = VisualizationType.get(derivedType);
        }

        return visualizationType;
    }

    @JsonIgnore
    private boolean isLocal() {
        return getContent().getUrl().startsWith("local");
    }


    private static class Content implements Serializable {
        private String url;
        private String icon;
        private String iconSelected;
        private String checksum;
        private Float orderIndex;


        @JsonCreator
        Content(@JsonProperty("url") String url,
                @JsonProperty("icon") String icon,
                @JsonProperty("iconSelected") String iconSelected,
                @JsonProperty("checksum") String checksum,
                @JsonProperty("orderIndex") Float orderIndex) {
            this.url = notNull(url);
            this.icon = notNull(icon);
            this.iconSelected = notNull(iconSelected);
            this.checksum = notNull(checksum);
            this.orderIndex = orderIndex;
        }

        public String getUrl() {
            return url;
        }

        public String getIcon() {
            return icon;
        }

        public String getIconSelected() {
            return iconSelected;
        }

        public String getChecksum() {
            return checksum;
        }

        public Float getOrderIndex() {
            return orderIndex;
        }
    }
}