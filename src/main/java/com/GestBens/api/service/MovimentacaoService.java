package com.GestBens.api.service;

import com.GestBens.api.dto.MovimentacaoRequest;
import com.GestBens.api.dto.MovimentacaoResponse;
import com.GestBens.api.model.*;
import com.GestBens.api.repository.BemRepository;
import com.GestBens.api.repository.LocalidadeRepository;
import com.GestBens.api.repository.MovimentacaoRepository;
import com.GestBens.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final BemRepository bemRepository;
    private final LocalidadeRepository localidadeRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository,
                               BemRepository bemRepository,
                               LocalidadeRepository localidadeRepository,
                               UsuarioRepository usuarioRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.bemRepository = bemRepository;
        this.localidadeRepository = localidadeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MovimentacaoResponse registrar(MovimentacaoRequest dto, Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        Bem bem = bemRepository.findById(dto.bemId())
                .orElseThrow(() -> new EntityNotFoundException("Bem não encontrado!"));

        Localidade destino = localidadeRepository.findById(dto.localidadeDestinoId())
                .orElseThrow(() -> new EntityNotFoundException("Localidade de destino não encontrada!"));

        // Regra 1: só bem ATIVO pode ser movimentado
        if (bem.getStatusBem() != StatusBem.ATIVO) {
            throw new IllegalStateException("Só bens com status ATIVO podem ser movimentados");
        }

        // Regra 2: a origem é sempre o local atual do bem
        Localidade origem = bem.getLocalidade();

        // Regra 3: não faz sentido mover para o mesmo lugar
        if (origem.getId().equals(destino.getId())) {
            throw new IllegalStateException("O bem já está nessa localidade");
        }

        Movimentacao mov = new Movimentacao();
        mov.setBem(bem);
        mov.setUsuario(usuario);
        mov.setLocalidadeOrigem(origem);
        mov.setLocalidadeDestino(destino);
        mov.setObservacao(dto.observacao());
        mov.setDataMovimentacao(LocalDateTime.now());

        // Regra 4: atualiza o local atual do bem, na mesma transação
        bem.setLocalidade(destino);

        return toResponse(movimentacaoRepository.save(mov));
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> listarTodas() {
        return movimentacaoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimentacaoResponse buscarPorId(Long id) {
        Movimentacao mov = movimentacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação com ID " + id + " não encontrada!"));
        return toResponse(mov);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> historicoDoBem(Long bemId) {
        if (!bemRepository.existsById(bemId)) {
            throw new EntityNotFoundException("Bem com ID " + bemId + " não encontrado!");
        }
        return movimentacaoRepository.findByBemIdOrderByDataMovimentacaoDesc(bemId).stream()
                .map(this::toResponse)
                .toList();
    }

    private MovimentacaoResponse toResponse(Movimentacao m) {
        return new MovimentacaoResponse(
                m.getId(),
                m.getDataMovimentacao(),
                m.getBem().getId(),
                m.getBem().getNome(),
                m.getLocalidadeOrigem().getNome(),
                m.getLocalidadeDestino().getNome(),
                m.getUsuario().getNome(),
                m.getObservacao()
        );
    }
}
