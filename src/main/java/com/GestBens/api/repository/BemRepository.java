package com.GestBens.api.repository;

import com.GestBens.api.model.Bem;
import com.GestBens.api.model.StatusBem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BemRepository extends JpaRepository<Bem, Long> {


    List<Bem> findByLocalidadeId(Long localidadeId);


    List<Bem> findByDataCompra(LocalDate dataCompra);

    List<Bem> findByStatusBem(StatusBem statusBem);
}
