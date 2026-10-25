package com.GestBens.api.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequest(
        @NotBlank(message = "o token do Google é obrigatório!")
        String credential) {
}
