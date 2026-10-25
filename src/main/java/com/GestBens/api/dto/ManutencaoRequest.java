package com.GestBens.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoRequest(
        Long bemId,
        String descricao) {
}
