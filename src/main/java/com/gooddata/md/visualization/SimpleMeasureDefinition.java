package com.gooddata.md.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.gooddata.executeafm.ObjQualifier;
import com.gooddata.executeafm.afm.FilterItem;

import java.util.List;

import static com.gooddata.md.visualization.SimpleMeasureDefinition.NAME;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(NAME)
public class SimpleMeasureDefinition extends com.gooddata.executeafm.afm.SimpleMeasureDefinition {
    public static final String NAME = "measureDefinition";

    @JsonCreator
    public SimpleMeasureDefinition(@JsonProperty("item") final ObjQualifier item,
                                   @JsonProperty("aggregation") final String aggregation,
                                   @JsonProperty("computeRatio") final Boolean computeRatio,
                                   @JsonProperty("filters") final List<FilterItem> filters) {
        super(item, aggregation, computeRatio, filters);
    }
}
