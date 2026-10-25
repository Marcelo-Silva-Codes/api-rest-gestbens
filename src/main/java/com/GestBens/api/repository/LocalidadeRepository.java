package com.GestBens.api.repository;

import com.GestBens.api.model.Localidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalidadeRepository extends JpaRepository<Localidade, Long> {

    List<Localidade> findByNome(String nome);
}
