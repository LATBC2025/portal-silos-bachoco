package com.bachoco.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoCompraSapClientWrapper {
  private List<PedidoSapResponseClientDTO> Items;
  
  public List<PedidoSapResponseClientDTO> getItems() {
    return this.Items;
  }
  
  public void setItems(List<PedidoSapResponseClientDTO> items) {
    this.Items = items;
  }
}
