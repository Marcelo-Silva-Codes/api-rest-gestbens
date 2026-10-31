package com.GestBens.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ManutencaoResponse(
        Long id,
        LocalDateTime dataEntrada,
        LocalDateTime dataRetorno,
        String descricao,
        BigDecimal custo,
        String usuario,
        Long bemId,
        String bem) {
}
