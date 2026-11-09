package com.GestBens.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovimentacaoRequest(
        @NotNull Long bemId,
        @NotNull Long localidadeDestinoId,
        @Size(max = 500) String observacao){
}
