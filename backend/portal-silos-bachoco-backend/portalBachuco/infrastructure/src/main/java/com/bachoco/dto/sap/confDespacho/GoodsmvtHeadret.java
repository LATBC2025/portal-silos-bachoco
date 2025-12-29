package com.bachoco.dto.sap.confDespacho;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoodsmvtHeadret {

    @JsonProperty("MATERIALDOCUMENT")
    private Long materialdocument;

    @JsonProperty("MATDOCUMENTYEAR")
    private String matdocumentyear;

    public String getMatdocumentyear() {
        return matdocumentyear;
    }

    public void setMatdocumentyear(String matdocumentyear) {
        this.matdocumentyear = matdocumentyear;
    }
}
