package com.GestBens.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BemResponse(
        Long id,
        String nome,
        String status,
        BigDecimal valorCompra,
        String nomeCategoria,
        String nomeLocalidade,
        LocalDate dataCompra
) {
}
