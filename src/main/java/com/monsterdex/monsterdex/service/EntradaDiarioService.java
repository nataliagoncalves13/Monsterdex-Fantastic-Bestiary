package com.monsterdex.monsterdex.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monsterdex.monsterdex.model.EntradaDiario;
import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.repository.EntradaDiarioRepository;


@Service
public class EntradaDiarioService {

    private final EntradaDiarioRepository diarioRepository;

    public EntradaDiarioService(EntradaDiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
    }

    @Transactional
    @NonNull
    public EntradaDiario salvar(@NonNull EntradaDiario entrada) {
        Objects.requireNonNull(entrada, "entrada não pode ser null");
        EntradaDiario salvo = diarioRepository.save(entrada);
        Objects.requireNonNull(salvo, "salvo não pode ser null");
        return salvo;
    }

    @Transactional(readOnly = true)
    @NonNull
    public List<EntradaDiario> listarTodas() {
        List<EntradaDiario> list = diarioRepository.findAll();
        Objects.requireNonNull(list, "lista de entradas não pode ser null");
        return list;
    }

    @Transactional(readOnly = true)
    @NonNull
    public List<EntradaDiario> listarPorUsuario(@NonNull Usuario usuario) {
        Objects.requireNonNull(usuario, "usuario não pode ser null");
        List<EntradaDiario> list = diarioRepository.findByUsuario(usuario);
        Objects.requireNonNull(list, "lista por usuário não pode ser null");
        return list;
    }

    @Transactional(readOnly = true)
    public Optional<EntradaDiario> buscarPorId(@NonNull Long id) {
        Objects.requireNonNull(id, "id não pode ser null");
        return diarioRepository.findById(id);
    }

    @Transactional
    public void remover(@NonNull Long id) {
        Objects.requireNonNull(id, "id não pode ser null");
        diarioRepository.deleteById(id);
    }
}