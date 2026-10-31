package com.GestBens.api.controller;

import com.GestBens.api.Security.UsuarioAutenticado;
import com.GestBens.api.dto.ManutencaoRequest;
import com.GestBens.api.dto.ManutencaoResponse;
import com.GestBens.api.dto.ManutencaoRetornoRequest;
import com.GestBens.api.dto.ManutencaoUpdateRequest;
import com.GestBens.api.service.ManutencaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController {
    private final ManutencaoService manutencaoService;

    public ManutencaoController(ManutencaoService manutencaoService) {
        this.manutencaoService = manutencaoService;
    }

    // o usuário vem do token (usuário logado), não do cliente
    @PostMapping
    public ResponseEntity<ManutencaoResponse> abrir(
            @RequestBody ManutencaoRequest dto,
            @AuthenticationPrincipal UsuarioAutenticado logado) {
        Long usuarioId = logado.getUsuario().getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(manutencaoService.abrir(dto, usuarioId));
    }

    @PatchMapping("/{id}/retorno")
    public ManutencaoResponse registrarRetorno(
            @PathVariable Long id,
            @RequestBody ManutencaoRetornoRequest dto) {
        return manutencaoService.registrarRetorno(id, dto);
    }

    @PutMapping("/{id}")
    public ManutencaoResponse atualizar(
            @PathVariable Long id,
            @RequestBody ManutencaoUpdateRequest dto) {
        return manutencaoService.atualizar(id, dto);
    }

    @GetMapping
    public List<ManutencaoResponse> listarTodas() {
        return manutencaoService.listarTodas();
    }

    @GetMapping("/em-andamento")
    public List<ManutencaoResponse> listarEmAndamento() {
        return manutencaoService.listarEmAndamento();
    }

    @GetMapping("/{id}")
    public ManutencaoResponse buscarPorId(@PathVariable Long id) {
        return manutencaoService.buscarPorId(id);
    }

    @GetMapping("/bem/{bemId}")
    public List<ManutencaoResponse> historicoDoBem(@PathVariable Long bemId) {
        return manutencaoService.historicoDoBem(bemId);
    }
}
