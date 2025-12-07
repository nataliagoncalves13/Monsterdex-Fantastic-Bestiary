package com.monsterdex.monsterdex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.repository.CriaturaRepository;

@Service
@SuppressWarnings("null")
public class CriaturaService {

    private final CriaturaRepository repository;

    public CriaturaService(CriaturaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Criatura> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Criatura> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Criatura salvar(Criatura criatura) {
        return repository.save(criatura);
    }

    @Transactional
    public void remover(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
}