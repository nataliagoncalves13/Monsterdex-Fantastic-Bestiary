package com.monsterdex.monsterdex.repository;

import com.monsterdex.monsterdex.model.EntradaDiario;
import com.monsterdex.monsterdex.model.Usuario; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntradaDiarioRepository extends JpaRepository<EntradaDiario, Long> {

    List<EntradaDiario> findByUsuario(Usuario usuario);

    List<EntradaDiario> findByCriaturaId(Long criaturaId);
}