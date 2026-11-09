package com.GestBens.api.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ManutencaoUpdateRequest(
       @NotBlank @Size(max = 1000) String descricao,
        @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal custo
) {}
