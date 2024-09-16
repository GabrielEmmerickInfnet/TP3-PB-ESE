package com.simplespdv.controller;

import com.simplespdv.model.Produto;
import com.simplespdv.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Listar todos os produtos
    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

    // Salvar um novo produto (com verificação de estoque)
    @PostMapping
    public ResponseEntity<?> salvarProduto(@RequestBody Produto produto) {
        // Verifica o estoque antes de salvar o produto
        int estoqueDisponivel = produtoService.verificarEstoque(produto.getId());

        if (estoqueDisponivel > 0) {
            // Produto pode ser salvo
            Produto produtoSalvo = produtoService.salvarProduto(produto);
            return ResponseEntity.ok(produtoSalvo);
        } else {
            // Não há estoque disponível
            return ResponseEntity.badRequest().body("Estoque insuficiente para o produto.");
        }
    }

    // Buscar um produto por ID
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    // Atualizar um produto existente (com verificação e redução de estoque)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        Produto produtoExistente = produtoService.buscarPorId(id);

        if (produtoExistente != null) {
            // Atualiza as informações do produto
            produtoExistente.setNome(produtoAtualizado.getNome());
            produtoExistente.setCodigoBarras(produtoAtualizado.getCodigoBarras());
            produtoExistente.setPreco(produtoAtualizado.getPreco());

            // Tenta reduzir o estoque antes de atualizar o produto
            boolean estoqueReduzido = produtoService.reduzirEstoque(produtoExistente.getId(), 1);  // Aqui estamos reduzindo 1 como exemplo
            if (estoqueReduzido) {
                Produto produtoAtualizadoFinal = produtoService.salvarProduto(produtoExistente);
                return ResponseEntity.ok(produtoAtualizadoFinal);
            } else {
                return ResponseEntity.badRequest().body("Falha ao atualizar: estoque insuficiente.");
            }
        }

        return ResponseEntity.notFound().build(); // Retorna 404 caso o produto não seja encontrado
    }

    // Deletar um produto
    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
    }

    // Endpoint adicional para verificar o estoque de um produto
    @GetMapping("/{produtoId}/estoque")
    public ResponseEntity<?> verificarEstoqueProduto(@PathVariable Long produtoId) {
        int estoqueDisponivel = produtoService.verificarEstoque(produtoId);
        return ResponseEntity.ok("Estoque disponível: " + estoqueDisponivel);
    }
}
