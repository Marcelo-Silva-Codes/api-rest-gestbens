package com.GestBens.api.repository;

import com.GestBens.api.model.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
        List<Manutencao> findByDataRetornoIsNull();

        List<Manutencao> findByBemIdOrderByDataEntradaDesc(Long bemId);
}
