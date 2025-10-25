package com.monsterdex.monsterdex.service;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.repository.CriaturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
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
    public Criatura buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criatura não encontrada"));
    }

    @Transactional
    public Criatura salvar(Criatura c) {
        return repository.save(c);
    }

    @Transactional
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Criatura não encontrada");
        }
        repository.deleteById(id);
    }
}
