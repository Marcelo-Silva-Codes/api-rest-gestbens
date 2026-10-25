package com.GestBens.api.dto;

import java.math.BigDecimal;

public record ManutencaoUpdateRequest(String descricao, BigDecimal custo) {
}
