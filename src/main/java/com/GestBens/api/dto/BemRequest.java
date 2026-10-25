package com.GestBens.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BemRequest(
        String nome,
        String descricao,
        LocalDate dataCompra,
        BigDecimal valorCompra,
        Long categoriaId,
        Long localidadeId)
{}
