package com.bachoco.dto.sap.confDespacho;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SapResponse {
    
	@JsonProperty("GOODSMVT_HEADRET")
	private GoodsmvtHeadret goodsmvt_HEADRET;

	@JsonProperty("T_RETURN")
	private List<TReturn> t_RETURN;

	public GoodsmvtHeadret getGoodsmvt_HEADRET() {
		return goodsmvt_HEADRET;
	}

	public void setGoodsmvt_HEADRET(GoodsmvtHeadret goodsmvt_HEADRET) {
		this.goodsmvt_HEADRET = goodsmvt_HEADRET;
	}

	public List<TReturn> getT_RETURN() {
		return t_RETURN;
	}

	public void setT_RETURN(List<TReturn> t_RETURN) {
		this.t_RETURN = t_RETURN;
	}

}
