package com.GestBens.api.repository;

import com.GestBens.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
    List<Categoria> findByNome(String nome);
}
