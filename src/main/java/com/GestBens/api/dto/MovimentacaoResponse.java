package com.GestBens.api.dto;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        LocalDateTime dataMovimentacao,
        Long bemId,
        String bem,
        String localidadeOrigem,
        String localidadeDestino,
        String usuario,
        String observacao
) {
}
