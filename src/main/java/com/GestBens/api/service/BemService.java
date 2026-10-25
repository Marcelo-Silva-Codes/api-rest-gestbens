package com.GestBens.api.service;

import com.GestBens.api.dto.BemRequest;
import com.GestBens.api.dto.BemResponse;
import com.GestBens.api.model.Bem;
import com.GestBens.api.model.Categoria;
import com.GestBens.api.model.Localidade;
import com.GestBens.api.model.StatusBem;
import com.GestBens.api.repository.BemRepository;
import com.GestBens.api.repository.CategoriaRepository;
import com.GestBens.api.repository.LocalidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BemService {
    private final BemRepository bemRepository;
    private final LocalidadeRepository localidadeRepository;
    private final CategoriaRepository categoriaRepository;

    public BemService(BemRepository bemRepository,
                      LocalidadeRepository localidadeRepository,
                      CategoriaRepository categoriaRepository){
        this.bemRepository = bemRepository;
        this.localidadeRepository = localidadeRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<BemResponse> listarTodos(){
        return bemRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BemResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public List<BemResponse> listarPorStatus(StatusBem status){
        return bemRepository.findByStatusBem(status).stream().map(this::toResponse).toList();
    }

    @Transactional
    public BemResponse salvar(BemRequest dto){
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));
        Localidade localidade = localidadeRepository.findById(dto.localidadeId())
                .orElseThrow(() -> new EntityNotFoundException("Localidade não encontrada!"));

        Bem bem = new Bem();
        bem.setNome(dto.nome());
        bem.setDescricao(dto.descricao());
        bem.setValorCompra(dto.valorCompra());
        bem.setDataCompra(dto.dataCompra());
        bem.setCategoria(categoria);
        bem.setLocalidade(localidade);   // local inicial
        // status não vem do cliente: o bem nasce ATIVO (valor padrão da entidade)

        return toResponse(bemRepository.save(bem));
    }

    @Transactional
    public BemResponse atualizar(Long id, BemRequest dto){
        Bem bem = buscarEntidade(id);
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada!"));

        bem.setNome(dto.nome());
        bem.setDescricao(dto.descricao());
        bem.setDataCompra(dto.dataCompra());
        bem.setValorCompra(dto.valorCompra());
        bem.setCategoria(categoria);
        // status e localidade NÃO mudam aqui:
        // status -> manutenção ou baixa; localidade -> movimentação

        return toResponse(bem);
    }

    @Transactional
    public void baixar(Long id) {
        Bem bem = buscarEntidade(id);
        if (bem.getStatusBem() == StatusBem.EM_MANUTENCAO) {
            throw new IllegalStateException("Finalize a manutenção antes de dar baixa no bem");
        }
        bem.setStatusBem(StatusBem.INATIVO);
    }

    private Bem buscarEntidade(Long id) {
        return bemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bem com ID " + id + " não encontrado!"));
    }

    private BemResponse toResponse(Bem bem) {
        return new BemResponse(
                bem.getId(),
                bem.getNome(),
                bem.getStatusBem().name(),
                bem.getValorCompra(),
                bem.getCategoria().getNome(),
                bem.getLocalidade().getNome()
        );
    }
}