package com.GestBens.api.controller;

import com.GestBens.api.Security.UsuarioAutenticado;
import com.GestBens.api.dto.MovimentacaoRequest;
import com.GestBens.api.dto.MovimentacaoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.GestBens.api.service.MovimentacaoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    // usuarioId como parâmetro é TEMPORÁRIO, até a autenticação ficar pronta
    @PostMapping
    public ResponseEntity<MovimentacaoResponse> registrar(
            @RequestBody MovimentacaoRequest dto,
            @AuthenticationPrincipal UsuarioAutenticado logado) {
        Long usuarioId = logado.getUsuario().getId();
        MovimentacaoResponse response = movimentacaoService.registrar(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<MovimentacaoResponse> listarTodas() {
        return movimentacaoService.listarTodas();
    }

    @GetMapping("/{id}")
    public MovimentacaoResponse buscarPorId(@PathVariable Long id) {
        return movimentacaoService.buscarPorId(id);
    }

    @GetMapping("/bem/{bemId}")
    public List<MovimentacaoResponse> historicoDoBem(@PathVariable Long bemId) {
        return movimentacaoService.historicoDoBem(bemId);
    }
}