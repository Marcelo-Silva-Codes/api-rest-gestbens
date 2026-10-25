package com.GestBens.api.service;

import com.GestBens.api.dto.UsuarioRequest;
import com.GestBens.api.dto.UsuarioResponse;
import com.GestBens.api.model.Usuario;
import com.GestBens.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    @Transactional
    public UsuarioResponse salvar(UsuarioRequest dto) {
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória");
        }
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("Já existe um usuário com este e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        // ativo = true e admin = false vêm como padrão da entidade

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse promoverAdmin(Long id) {
        Usuario u = buscarEntidade(id);

        if (!u.isAtivo()) {
            throw new IllegalStateException("Reative o usuário antes de torná-lo administrador");
        }
        u.setAdmin(true);
        return toResponse(u);
    }

    @Transactional
    public UsuarioResponse rebaixarAdmin(Long id) {
        Usuario u = buscarEntidade(id);

        if (u.isAdmin() && usuarioRepository.contarAdminsAtivos() <= 1) {
            throw new IllegalStateException("Não é possível remover o último administrador ativo");
        }
        u.setAdmin(false);
        return toResponse(u);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest dto) {
        Usuario usuario = buscarEntidade(id);

        boolean emailMudou = !usuario.getEmail().equalsIgnoreCase(dto.email());
        if (emailMudou && usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("Já existe um usuário com este e-mail");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        return toResponse(usuario);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario u = buscarEntidade(id);

        if (u.isAdmin() && u.isAtivo() && usuarioRepository.contarAdminsAtivos() <= 1) {
            throw new IllegalStateException("Não é possível desativar o último administrador ativo");
        }
        u.setAtivo(false);
    }

    @Transactional
    public void reativar(Long id) {
        buscarEntidade(id).setAtivo(true);
    }

    private Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + id + " não encontrado!"));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.isAdmin(), u.isAtivo());
    }
}