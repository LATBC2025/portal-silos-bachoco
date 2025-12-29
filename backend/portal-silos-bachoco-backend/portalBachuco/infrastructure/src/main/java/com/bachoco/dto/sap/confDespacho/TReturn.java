package com.bachoco.dto.sap.confDespacho;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TReturn {

    @JsonProperty("TYPE")
    private String type;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("NUMBER")
    private int number;

    @JsonProperty("MESSAGE")
    private String message;

    @JsonProperty("LOG_MSG_NO")
    private String log_MSG_NO;

    @JsonProperty("MESSAGE_V1")
    private String message_V1;
    
    @JsonProperty("MESSAGE_V2")
    private String messageV2;
    
    @JsonProperty("MESSAGE_V3")
    private String messageV3;

    @JsonProperty("MESSAGE_V4")
    private String messageV4;

    @JsonProperty("PARAMETER")
    private String parameter;

    @JsonProperty("ROW")
    private String row;

    @JsonProperty("SYSTEM")
    private String system;

    // Getters y setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getLog_MSG_NO() { return log_MSG_NO; }
    public void setLog_MSG_NO(String log_MSG_NO) { this.log_MSG_NO = log_MSG_NO; }

    public Object getMessage_V1() { return message_V1; }
    public void setMessage_V1(String message_V1) { this.message_V1 = message_V1; }

    public String getParameter() { return parameter; }
    public void setParameter(String parameter) { this.parameter = parameter; }

    public String getRow() { return row; }
    public void setRow(String row) { this.row = row; }

    public String getSystem() { return system; }
    public void setSystem(String system) { this.system = system; }
	public Object getMessageV2() {
		return messageV2;
	}
	public void setMessageV2(String messageV2) {
		this.messageV2 = messageV2;
	}
    
    public String getMessageV3() {
        return messageV3;
    }

    public void setMessageV3(String messageV3) {
        this.messageV3 = messageV3;
    }
    
    public String getMessageV4() {
        return messageV4;
    }

    public void setMessageV4(String messageV4) {
        this.messageV4 = messageV4;
    }
    
}