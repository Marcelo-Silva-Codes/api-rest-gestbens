package com.GestBens.api.controller;

import com.GestBens.api.dto.CategoriaRequest;
import com.GestBens.api.dto.CategoriaResponse;
import com.GestBens.api.model.Categoria;
import com.GestBens.api.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    // GET: Listar todas
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas(){
        return ResponseEntity.ok(categoriaService.listarTodas());
    }
    // GET: Buscar por ID -> GET http://localhost:8080/api/categorias/1
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id){
            CategoriaResponse dto = categoriaService.buscarPorId(id);
            return ResponseEntity.ok(dto);

    }
    // POST: Criar categoria -> POST http://localhost:8080/api/categorias
    @PostMapping
    public ResponseEntity<CategoriaResponse> criar( @Valid @RequestBody CategoriaRequest dto){
        CategoriaResponse novoCategoria = categoriaService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCategoria);
    }

    // PUT: Atualizar categoria -> PUT http://localhost:8080/api/categorias/1
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(@PathVariable Long id,@Valid @RequestBody CategoriaRequest dto){
        CategoriaResponse categoriaAtualizada = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(categoriaAtualizada);

    }

    // DELETE: Deletar categoria -> DELETE http://localhost:8080/api/categorias/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
            categoriaService.deletar(id);
            return ResponseEntity.noContent().build();
    }

}