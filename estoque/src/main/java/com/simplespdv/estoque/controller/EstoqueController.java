package com.simplespdv.estoque.controller;

import com.simplespdv.estoque.model.Estoque;
import com.simplespdv.estoque.service.EstoqueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private static final Logger log = LoggerFactory.getLogger(EstoqueController.class);
    private final EstoqueService estoqueService;

    // Construtor para injeção de dependência
    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    // Criar um novo estoque
    @PostMapping
    public ResponseEntity<Estoque> createEstoque(@RequestBody Estoque estoque) {
        Estoque saved = estoqueService.create(estoque);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Buscar todos os estoques
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(estoqueService.findAll());
    }

    // Buscar estoque por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        log.info("Find estoque by id: {}", id);
        Optional<Estoque> optional = estoqueService.findById(id);
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar estoque por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        estoqueService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Atualizar um estoque
    @PutMapping("/{id}")
    public ResponseEntity<Estoque> updateEstoque(@PathVariable Long id, @RequestBody Estoque estoque) {
        Optional<Estoque> optionalEstoque = estoqueService.findById(id);
        if (optionalEstoque.isPresent()) {
            Estoque estoqueExistente = optionalEstoque.get();
            estoqueExistente.setProdutoId(estoque.getProdutoId());
            estoqueExistente.setQuantidade(estoque.getQuantidade());
            Estoque atualizado = estoqueService.update(estoqueExistente);
            return ResponseEntity.ok(atualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }
}
