package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.service.CriaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public Criatura buscar(@PathVariable("id") Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Criar criatura")
    public ResponseEntity<Criatura> criar(@Valid @RequestBody Criatura criatura) {
        Criatura salvo = service.salvar(criatura);
        return ResponseEntity.created(URI.create("/api/criaturas/" + salvo.getId())).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar criatura")
    public Criatura atualizar(@PathVariable("id") Long id, @Valid @RequestBody Criatura criatura) {
        // Garante que estamos atualizando o recurso correto
        service.buscarPorId(id); // 404 se não existir
        criatura.setId(id);
        return service.salvar(criatura);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover criatura")
    public void remover(@PathVariable("id") Long id) {
        service.remover(id);
    }
}
