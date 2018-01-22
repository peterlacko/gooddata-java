package com.gooddata.visualizationObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.gooddata.executeafm.ObjQualifier;

import static com.gooddata.visualizationObject.PopMeasureDefinition.NAME;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(NAME)
public class PopMeasureDefinition extends com.gooddata.executeafm.afm.PopMeasureDefinition {
    public static final String NAME = "popMeasureDefinition";

    @JsonCreator
    public PopMeasureDefinition(@JsonProperty("measureIdentifier") final String measureIdentifier,
                                @JsonProperty("popAttribute") final ObjQualifier popAttribute) {
        super(measureIdentifier, popAttribute);
    }
}
