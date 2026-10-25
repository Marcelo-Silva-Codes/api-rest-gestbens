package com.GestBens.api.dto;

public record UsuarioRequest(
        String nome,
        String email,
        String senha
) {
}
