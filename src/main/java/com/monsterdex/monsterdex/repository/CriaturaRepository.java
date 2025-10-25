package com.monsterdex.monsterdex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.monsterdex.monsterdex.model.Criatura;

public interface CriaturaRepository extends JpaRepository<Criatura, Long> {
}
