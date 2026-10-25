package com.GestBens.api.repository;

import com.GestBens.api.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    List<Movimentacao> findByBemIdOrderByDataMovimentacaoDesc(Long bemId);
}