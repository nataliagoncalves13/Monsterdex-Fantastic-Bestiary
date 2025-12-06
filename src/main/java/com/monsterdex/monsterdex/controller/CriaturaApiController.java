package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.service.CriaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/criaturas")
@Tag(name = "Criaturas", description = "API REST para gerenciamento de criaturas")
public class CriaturaApiController {

    private final CriaturaService service;

    public CriaturaApiController(CriaturaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar criaturas")
    public List<Criatura> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar criatura por id")
    public Criatura buscar(@PathVariable("id") long id) {
        // Use primitive long to avoid null-safety warnings from the IDE/analyzers.
        // If service.buscarPorId accepts Long, autoboxing will convert 'long' -> Long safely.
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Criar criatura")
    public ResponseEntity<Criatura> criar(@Valid @RequestBody Criatura criatura) {
        Criatura salvo = service.salvar(criatura);

        // Garante que o id salvo não seja nulo antes de passar para buildAndExpand,
        // evitando warnings de "Null type safety" e possíveis NPEs em tempo de execução.
        Long salvoId = Objects.requireNonNull(salvo.getId(), "ID do recurso salvo é nulo");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvoId)
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar criatura")
    public Criatura atualizar(@PathVariable("id") long id, @Valid @RequestBody Criatura criatura) {
        // Garante que estamos atualizando o recurso correto (lança 404 se não existir)
        service.buscarPorId(id);
        criatura.setId(id);
        return service.salvar(criatura);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover criatura")
    public ResponseEntity<Void> remover(@PathVariable("id") long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}