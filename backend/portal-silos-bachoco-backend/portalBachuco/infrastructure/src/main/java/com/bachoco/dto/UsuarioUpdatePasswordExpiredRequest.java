package com.bachoco.dto;

public record UsuarioUpdatePasswordExpiredRequest(String username,String passwordActual,String nuevoPassword) {

}
