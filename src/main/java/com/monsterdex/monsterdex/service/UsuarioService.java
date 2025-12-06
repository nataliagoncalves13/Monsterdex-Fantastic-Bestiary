package com.monsterdex.monsterdex.service;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.repository.UsuarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "usuarioRepository não pode ser null");
    }

    /**
     * Salva o usuário e garante para o analisador que o retorno não será null.
     */
    @NonNull
    public Usuario salvar(@NonNull Usuario usuario) {
        Objects.requireNonNull(usuario, "usuario não pode ser null");
        Usuario salvo = usuarioRepository.save(usuario);
        return Objects.requireNonNull(salvo, "usuario salvo não pode ser null");
    }

    /**
     * Retorna a lista de usuários (não-null).
     */
    @NonNull
    public List<Usuario> listar() {
        List<Usuario> list = usuarioRepository.findAll();
        return Objects.requireNonNull(list, "lista de usuários não pode ser null");
    }

    /**
     * Busca por username. Pode retornar null se não encontrado; anotado como @Nullable para informar o analisador.
     */
    @Nullable
    public Usuario buscarPorUsername(@NonNull String username) {
        Objects.requireNonNull(username, "username não pode ser null");
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    /**
     * Remove usuário por id.
     */
    public void remover(@NonNull Long id) {
        Objects.requireNonNull(id, "id não pode ser null");
        usuarioRepository.deleteById(id);
    }
}