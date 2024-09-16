package com.simplespdv.estoque.service;

import com.simplespdv.estoque.model.Estoque;
import com.simplespdv.estoque.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    // Construtor para injeção de dependência
    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    // Criar um novo estoque
    public Estoque create(Estoque estoque) {
        return estoqueRepository.save(estoque);
    }

    // Listar todos os estoques
    public List<Estoque> findAll() {
        return estoqueRepository.findAll();
    }

    // Buscar estoque por ID
    public Optional<Estoque> findById(Long id) {
        return estoqueRepository.findById(id);
    }

    // Deletar estoque por ID
    public void deleteById(Long id) {
        estoqueRepository.deleteById(id);
    }

    // Atualizar estoque
    public Estoque update(Estoque estoque) {
        if (estoqueRepository.existsById(estoque.getId())) {
            return estoqueRepository.save(estoque);
        } else {
            throw new RuntimeException("Estoque not found");
        }
    }
}
