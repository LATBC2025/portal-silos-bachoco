package com.bachoco.utils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UrlConnectionUtil {
	
	private static final ObjectMapper OBJECT_MAPPER = createConfiguredObjectMapper();

	private static ObjectMapper createConfiguredObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return objectMapper;
    }
    
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
    
	public static boolean isValidJsonResponse(String jsonResponse) {
	    if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
	        return false;
	    }
		int endIndex = Math.min(jsonResponse.length(), 15);
	    String prefix = jsonResponse.substring(0, endIndex).trim();
	    if (!prefix.startsWith("{") && !prefix.startsWith("[")) {
	    	 return false;
	    }
	    return true;
	}
}
