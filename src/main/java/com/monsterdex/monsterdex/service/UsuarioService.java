package com.monsterdex.monsterdex.service;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Usuario salvar(Usuario usuario) {
       
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorUsername(String username) {
     
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    // 4. Remover
    public void remover(Long id) {
        usuarioRepository.deleteById(id);
    }
}