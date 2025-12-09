package com.monsterdex.monsterdex.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.monsterdex.monsterdex.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}