package com.GestBens.api.controller;

import com.GestBens.api.dto.BemRequest;
import com.GestBens.api.dto.BemResponse;
import com.GestBens.api.model.StatusBem;
import com.GestBens.api.service.BemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bens")
public class BemController {
    private final BemService bemService;

    public BemController(BemService bemService) {

        this.bemService = bemService;

    }

    // 1. GET: Listar todos os bens -> GET http://localhost:8080/api/bens
    @Transactional(readOnly = true)
    @GetMapping
    public ResponseEntity<List<BemResponse>> listarTodos() {
//        List<Bem> bens = bemService.listarTodos();
        return ResponseEntity.ok(bemService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BemResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bemService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BemResponse>> listarPorStatus(@PathVariable StatusBem status) {
        return ResponseEntity.ok(bemService.listarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<BemResponse> criar(@Valid @RequestBody BemRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bemService.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BemResponse> atualizar(@PathVariable Long id,@Valid @RequestBody BemRequest dto) {
        return ResponseEntity.ok(bemService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/baixa")
    public ResponseEntity<Void> baixar(@PathVariable Long id) {
        bemService.baixar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<BemResponse> reativar(@PathVariable Long id) {
        return ResponseEntity.ok(bemService.reativar(id));
    }
}