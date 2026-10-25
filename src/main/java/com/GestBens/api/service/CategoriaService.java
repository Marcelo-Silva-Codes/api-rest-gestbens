package com.GestBens.api.service;

import com.GestBens.api.dto.CategoriaRequest;
import com.GestBens.api.dto.CategoriaResponse;
import com.GestBens.api.model.Categoria;
import com.GestBens.api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Listar todas as categorias
    public List<CategoriaResponse> listarTodas(){
        return categoriaRepository.findAll().stream()
                .map(cat -> new CategoriaResponse(
                        cat.getId(),
                        cat.getNome(),
                        cat.getDescricao()
                ))
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public CategoriaResponse buscarPorId(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria com ID " + id + " não encontrada!"));

        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }

    // Salvar nova categoria
    public CategoriaResponse salvar(CategoriaRequest dto){
        Categoria categoria = new Categoria();
        categoria.setNome((dto.nome()));
        categoria.setDescricao(dto.descricao());

        Categoria salva = categoriaRepository.save(categoria);

        return new CategoriaResponse(
                salva.getId(),
                salva.getNome(),
                salva.getDescricao()
        );
    }

    // Atualizar categoria existente
    public CategoriaResponse atualizar(Long id, CategoriaRequest dto){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria com ID " +id+ " não foi encontrado!"));

        categoria.setNome((dto.nome()));
        categoria.setDescricao(dto.descricao());

        Categoria atualizada = categoriaRepository.save(categoria);

        return new CategoriaResponse(
                atualizada.getId(),
                atualizada.getNome(),
                atualizada.getDescricao()
        );
    }

    // Deletar categoria
    public void deletar(Long id){
        if(!categoriaRepository.existsById(id)){
            throw new RuntimeException("Categoria com ID "+id+ " não encontrada!");
        }
        categoriaRepository.deleteById(id);
    }
}
