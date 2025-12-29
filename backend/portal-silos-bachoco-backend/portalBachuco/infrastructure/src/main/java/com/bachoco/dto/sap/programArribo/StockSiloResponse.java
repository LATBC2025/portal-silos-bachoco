package com.bachoco.dto.sap.programArribo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockSiloResponse {

    @JsonProperty("STOCKSILO")
    private String stockSilo;

    public String getStockSilo() {
        return stockSilo;
    }

    public void setStockSilo(String stockSilo) {
        this.stockSilo = stockSilo;
    }
}