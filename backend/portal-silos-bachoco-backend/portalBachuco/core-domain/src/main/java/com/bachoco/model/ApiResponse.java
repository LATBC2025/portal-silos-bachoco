package com.bachoco.model;

public class ApiResponse<T> {

    private String code;       // Código del resultado (OK, ERR001, etc.)
    private String message;    // Mensaje legible para mostrar al usuario
    private T data;            // Cualquier tipo de objeto o lista de datos

    public ApiResponse() {}

    public ApiResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getters y setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}