package com.GestBens.api.controller;

import com.GestBens.api.Security.UsuarioAutenticado;
import com.GestBens.api.dto.UsuarioRequest;
import com.GestBens.api.dto.UsuarioResponse;
import com.GestBens.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id,
                                          @AuthenticationPrincipal UsuarioAutenticado logado) {
        if (logado.getUsuario().getId().equals(id)) {
            throw new IllegalStateException("Você não pode desativar a si mesmo");
        }
        usuarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/promover")
    public ResponseEntity<UsuarioResponse> promover(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.promoverAdmin(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/rebaixar")
    public ResponseEntity<UsuarioResponse> rebaixar(@PathVariable Long id,
                                                    @AuthenticationPrincipal UsuarioAutenticado logado) {
        if (logado.getUsuario().getId().equals(id)) {
            throw new IllegalStateException("Você não pode remover seu próprio acesso de administrador");
        }
        return ResponseEntity.ok(usuarioService.rebaixarAdmin(id));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        usuarioService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
