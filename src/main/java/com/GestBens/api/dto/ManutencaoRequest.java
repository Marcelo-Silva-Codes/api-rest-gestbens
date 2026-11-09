package com.GestBens.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ManutencaoRequest(
        @NotNull Long bemId,
        @NotBlank @Size(max = 1000) String descricao) {
}
