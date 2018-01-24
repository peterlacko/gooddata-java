package com.gooddata.md.visualization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.io.Serializable;

public abstract class ReferenceItems extends JsonNode implements Serializable {
    private static final long serialVersionUID = 5272560604530865694L;

    public String getItemById(final String id) {
        JsonNode uri = this.get(id);

        return uri.getNodeType() == JsonNodeType.STRING ? uri.toString() : null;
    }
}
