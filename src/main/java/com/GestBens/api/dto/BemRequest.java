package com.GestBens.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BemRequest(
       @NotBlank @Size(max = 150) String nome,
        @Size(max = 1000) String descricao,
        @PastOrPresent LocalDate dataCompra,
        @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal valorCompra,
        @NotNull Long categoriaId,
        @NotNull Long localidadeId)
{}
