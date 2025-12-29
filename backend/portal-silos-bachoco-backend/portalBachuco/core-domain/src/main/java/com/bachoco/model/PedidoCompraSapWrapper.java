package com.bachoco.model;

import java.util.List;

public class PedidoCompraSapWrapper {

	 private List<PedidoSapResponseDTO> Items;

	 public List<PedidoSapResponseDTO> getItems() {
		 return Items;
	 }

	 public void setItems(List<PedidoSapResponseDTO> items) {
		 Items = items;
	 }
}
