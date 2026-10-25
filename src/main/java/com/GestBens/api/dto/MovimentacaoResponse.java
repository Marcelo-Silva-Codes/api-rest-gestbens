package com.GestBens.api.dto;

import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        LocalDateTime dataMovimentacao,
        String bem,
        String localidadeOrigem,
        String localidadeDestino,
        String usuario,
        String observacao
) {
}
