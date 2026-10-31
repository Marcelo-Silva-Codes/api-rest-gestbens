package com.GestBens.api.controller;

import com.GestBens.api.dto.LocalidadeRequest;
import com.GestBens.api.dto.LocalidadeResponse;
import com.GestBens.api.service.LocalidadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadeController {

    private final LocalidadeService localidadeService;

    public LocalidadeController(LocalidadeService localidadeService) {
        this.localidadeService = localidadeService;
    }
    // GET: Listar todas
    @GetMapping
    public ResponseEntity<List<LocalidadeResponse>> listarTodas(){
        return ResponseEntity.ok(localidadeService.listarTodas());
    }
    // GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<LocalidadeResponse> buscarPorId(@PathVariable Long id) {
        try {
            LocalidadeResponse dto = localidadeService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // POST: Criar localidade
    @PostMapping
    public ResponseEntity<LocalidadeResponse> criar(@RequestBody LocalidadeRequest dto) {
        LocalidadeResponse novaLocalidade = localidadeService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaLocalidade);
    }

    // PUT: Atualizar localidade
    @PutMapping("/{id}")
    public ResponseEntity<LocalidadeResponse> atualizar(@PathVariable Long id, @RequestBody LocalidadeRequest dto) {
        try {
            LocalidadeResponse localidadeAtualizada = localidadeService.atualizar(id, dto);
            return ResponseEntity.ok(localidadeAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE: Deletar localidade -> DELETE http://localhost:8080/api/localidades/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        localidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
}
