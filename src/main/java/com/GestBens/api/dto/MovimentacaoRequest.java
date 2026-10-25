package com.GestBens.api.dto;

import java.time.LocalDate;

public record MovimentacaoRequest(
        Long bemId,
        Long localidadeDestinoId,
        String observacao){
}
