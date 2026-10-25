package com.GestBens.api.service;

import com.GestBens.api.dto.ManutencaoRequest;
import com.GestBens.api.dto.ManutencaoResponse;
import com.GestBens.api.dto.ManutencaoRetornoRequest;
import com.GestBens.api.dto.ManutencaoUpdateRequest;
import com.GestBens.api.model.Bem;
import com.GestBens.api.model.Manutencao;
import com.GestBens.api.model.StatusBem;
import com.GestBens.api.model.Usuario;
import com.GestBens.api.repository.BemRepository;
import com.GestBens.api.repository.ManutencaoRepository;
import com.GestBens.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BemRepository bemRepository;

    public ManutencaoService(ManutencaoRepository manutencaoRepository,
                             UsuarioRepository usuarioRepository,
                             BemRepository bemRepository) {
        this.manutencaoRepository = manutencaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.bemRepository = bemRepository;
    }

    @Transactional
    public ManutencaoResponse abrir(ManutencaoRequest dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        Bem bem = bemRepository.findById(dto.bemId())
                .orElseThrow(() -> new EntityNotFoundException("Bem não encontrado!"));

        // Regra 1: só bem ATIVO pode entrar em manutenção
        if (bem.getStatusBem() != StatusBem.ATIVO) {
            throw new IllegalStateException("Só bens com status ATIVO podem entrar em manutenção");
        }

        Manutencao m = new Manutencao();
        m.setBem(bem);
        m.setUsuario(usuario);
        m.setDescricao(dto.descricao());
        m.setDataEntrada(LocalDateTime.now());

        // Regra 2: o status do bem muda na mesma transação
        bem.setStatusBem(StatusBem.EM_MANUTENCAO);

        return toResponse(manutencaoRepository.save(m));
    }

    @Transactional
    public ManutencaoResponse registrarRetorno(Long id, ManutencaoRetornoRequest dto) {
        Manutencao m = buscarEntidade(id);

        // Regra 3: não finalizar duas vezes
        if (m.getDataRetorno() != null) {
            throw new IllegalStateException("Esta manutenção já foi finalizada");
        }

        m.setDataRetorno(LocalDateTime.now());
        m.setCusto(dto.custo());
        m.getBem().setStatusBem(StatusBem.ATIVO);

        return toResponse(m);
    }

    @Transactional
    public ManutencaoResponse atualizar(Long id, ManutencaoUpdateRequest dto) {
        Manutencao m = buscarEntidade(id);
        m.setDescricao(dto.descricao());
        m.setCusto(dto.custo());
        return toResponse(m);
    }

    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarTodas() {
        return manutencaoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarEmAndamento() {
        return manutencaoRepository.findByDataRetornoIsNull().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ManutencaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    private Manutencao buscarEntidade(Long id) {
        return manutencaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manutenção com ID " + id + " não encontrada!"));
    }

    private ManutencaoResponse toResponse(Manutencao m) {
        return new ManutencaoResponse(
                m.getId(),
                m.getDataEntrada(),
                m.getDataRetorno(),
                m.getDescricao(),
                m.getCusto(),
                m.getUsuario().getNome(),
                m.getBem().getNome()
        );
    }
}