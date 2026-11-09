package com.GestBens.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(max = 80) String nome,
        @NotBlank @Email @Size(max = 80) String email,
        @Size(min = 8, max = 72) String senha
) {
}
