package com.simplespdv.service;

import com.simplespdv.model.Produto;
import com.simplespdv.model.ProdutoHistorico;
import com.simplespdv.repository.ProdutoRepository;
import com.simplespdv.repository.ProdutoHistoricoRepository;
import com.simplespdv.client.EstoqueClient; // Adicionando o Feign Client
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoHistoricoRepository produtoHistoricoRepository;

    @Autowired
    private EstoqueClient estoqueClient;  // Adicionando o Feign Client para comunicação com o serviço de estoque

    // Listar todos os produtos
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Salvar um produto (com histórico e verificação de estoque)
    public Produto salvarProduto(Produto produto) {
        // Se for uma atualização (ou seja, se o ID já existir), salvar histórico
        if (produto.getId() != null) {
            Produto produtoOriginal = produtoRepository.findById(produto.getId()).orElse(null);
            if (produtoOriginal != null) {
                ProdutoHistorico historico = new ProdutoHistorico(
                    produtoOriginal.getNome(), produto.getNome(),
                    produtoOriginal.getCodigoBarras(), produto.getCodigoBarras(),
                    produtoOriginal.getPreco(), produto.getPreco(),
                    LocalDateTime.now()
                );
                produtoHistoricoRepository.save(historico); // Salvando o histórico da alteração
            }
        }
        return produtoRepository.save(produto); // Salvando o produto atualizado ou novo
    }

    // Buscar um produto por ID
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    // Deletar um produto por ID
    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }

    // Verificar o estoque de um produto
    public int verificarEstoque(Long produtoId) {
        return estoqueClient.verificarEstoque(produtoId);  // Comunicação com o serviço de estoque para verificar a quantidade disponível
    }

    // Reduzir o estoque de um produto após uma venda
    public boolean reduzirEstoque(Long produtoId, int quantidadeVendida) {
        return estoqueClient.reduzirEstoque(produtoId, quantidadeVendida);  // Comunicação com o serviço de estoque para reduzir a quantidade
    }
}
