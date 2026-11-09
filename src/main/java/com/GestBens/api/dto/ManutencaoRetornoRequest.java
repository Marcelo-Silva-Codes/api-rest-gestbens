package com.GestBens.api.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ManutencaoRetornoRequest(
        @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal custo) {
}
