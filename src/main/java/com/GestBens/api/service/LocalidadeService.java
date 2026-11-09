package com.GestBens.api.service;
import com.GestBens.api.dto.LocalidadeRequest;
import com.GestBens.api.dto.LocalidadeResponse;
import com.GestBens.api.model.Localidade;
import com.GestBens.api.repository.LocalidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalidadeService {
    private final LocalidadeRepository localidadeRepository;

    public LocalidadeService(LocalidadeRepository localidadeRepository) {
        this.localidadeRepository = localidadeRepository;
    }

    public List<LocalidadeResponse> listarTodas(){
        return localidadeRepository.findAll().stream()
                .map(loc -> new LocalidadeResponse(
                        loc.getId(),
                        loc.getNome(),
                        loc.getDescricao()
                ))
                .collect(Collectors.toList());
    }

    public LocalidadeResponse buscarPorId(Long id){
        Localidade localidade = localidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Localidade com ID " + id+ " não foi encontrada."));

        return new LocalidadeResponse(
                localidade.getId(),
                localidade.getNome(),
                localidade.getDescricao()
        );
    }

    public LocalidadeResponse salvar(LocalidadeRequest dto){
        Localidade localidade = new Localidade();
        localidade.setNome(dto.nome());
        localidade.setDescricao(dto.descricao());

        Localidade salva = localidadeRepository.save(localidade);

        return new LocalidadeResponse(
                salva.getId(),
                salva.getNome(),
                salva.getDescricao()
        );
    }

    public LocalidadeResponse atualizar(Long id, LocalidadeRequest dto){
        Localidade localidade = localidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localidade com ID " + id + " não encontrada!"));

        localidade.setNome(dto.nome());
        localidade.setDescricao(dto.descricao());

        Localidade atualizado = localidadeRepository.save(localidade);

        return new LocalidadeResponse(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getDescricao()
        );
    }

    public void deletar(Long id){
        if(!localidadeRepository.existsById(id)){
            throw new EntityNotFoundException("Localidade com ID " + id + " não encontrada para exclusão.");
        }
        try {
            localidadeRepository.deleteById(id);
            localidadeRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Não é possível excluir: esta localidade está sendo usada por bens ou aparece no histórico de movimentações.");
        }
    }
}
