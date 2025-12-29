package com.bachoco.model;

public class BodegaAndSilo {
	private String bodega;
	private Integer siloId;
	
	public BodegaAndSilo() {
	}

	public BodegaAndSilo(String bodega, Integer siloId) {
		this.bodega = bodega;
		this.siloId = siloId;
	}

	public String getBodega() {
		return bodega;
	}

	public void setBodega(String bodega) {
		this.bodega = bodega;
	}

	public Integer getSiloId() {
		return siloId;
	}

	public void setSiloId(Integer siloId) {
		this.siloId = siloId;
	}
	
}
