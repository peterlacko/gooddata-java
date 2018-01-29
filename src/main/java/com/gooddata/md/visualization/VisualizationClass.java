/*
 * Copyright (C) 2018, GoodData(R) Corporation. All rights reserved.
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

    private VisualizationClass(@JsonProperty("content") final Content content, @JsonProperty("meta") final Meta meta ) {
        super(meta);
        this.content = notNull(content);
    }

    /**
     * @return default icon for visualization, for local visualizations in form of 'local:{@link VisualizationType}'
     */
    @JsonIgnore
    public String getIcon() {
        return getContent().getIcon();
    }

    /**
     * @param {set} default icon for visualization, for local visualizations in form of 'local:{@link VisualizationType}'
     */
    @JsonIgnore
    public void setIcon(String icon) {
        content.setIcon(icon);
    }

    /**
     * @return icon displayed on mouse over, for local visualizations in form of 'local:{@link VisualizationType}.selected'
     */
    @JsonIgnore
    public String getIconSelected() {
        return getContent().getIconSelected();
    }

    /**
     * @param iconSelected icon displayed on mouse over, for local visualizations in form of 'local:{@link VisualizationType}.selected'
     */
    @JsonIgnore
    public void setIconSelected(String iconSelected) {
        content.setIconSelected(iconSelected);
    }

    /**
     * @return checksum of visualization class, 'local' for local visualizations
     */
    @JsonIgnore
    public String getChecksum() {
        return getContent().getChecksum();
    }

    /**
     * @param checksum checksum of visualization class, 'local' for local visualizations
     */
    @JsonIgnore
    public void setChecksum(String checksum) {
        content.setChecksum(checksum);
    }

    /**
     * @return absolute position of icon in list
     */
    @JsonIgnore
    public Float getOrderIndex() {
        return getContent().getOrderIndex();
    }

    /**
     * @param orderIndex determining icon's position in the list
     */
    @JsonIgnore
    public void setOrderIndex(Float orderIndex) {
        content.setOrderIndex(orderIndex);
    }

    /**
     * @return uri to implementation of visualization, for local visualizations in form of 'local:{@link VisualizationType}'
     */
    @JsonIgnore
    public String getUrl() {
        return getContent().getUrl();
    }

    /**
     * @param url pointing to implementation of visualization, for local visualizations in form of 'local:{@link VisualizationType}'
     */
    @JsonIgnore
    public void setUrl(String url) {
        content.setUrl(url);
    }

    /**
     * @return type of visualization, table by default
     */
    @JsonIgnore
    public VisualizationType getVisualizationType() {
        VisualizationType visualizationType = VisualizationType.TABLE;

        String uriParts[] = getContent().getUrl().split(":");

        if (uriParts.length > 0 && isLocal()) {
            String derivedType = uriParts[uriParts.length-1];

            visualizationType = VisualizationType.of(derivedType);
        }

        return visualizationType;
    }

    @JsonIgnore
    private boolean isLocal() {
        return getContent().getChecksum().equals("local");
    }

    private Content getContent()  {
        return content;
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

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIconSelected() {
            return iconSelected;
        }

        public void setIconSelected(String iconSelected) {
            this.iconSelected = iconSelected;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }

        public Float getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(Float orderIndex) {
            this.orderIndex = orderIndex;
        }
    }
}