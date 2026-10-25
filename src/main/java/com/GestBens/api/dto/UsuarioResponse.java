package com.GestBens.api.dto;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        boolean admin,
        boolean ativo
) {
}
