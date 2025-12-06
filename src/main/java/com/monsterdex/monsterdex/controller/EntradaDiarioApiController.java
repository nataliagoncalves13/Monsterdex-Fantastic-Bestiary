package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.EntradaDiario;
import com.monsterdex.monsterdex.service.EntradaDiarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/diario")
@Tag(name = "Diário", description = "API REST para gerenciamento das Entradas de Diário")
public class EntradaDiarioApiController {

    private final EntradaDiarioService service;

    public EntradaDiarioApiController(EntradaDiarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas as entradas de diário")
    public List<EntradaDiario> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar entrada de diário por ID")
    public EntradaDiario buscar(@PathVariable("id") long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diário não encontrado"));
    }

    @PostMapping
    @Operation(summary = "Criar nova entrada de diário")
    @SuppressWarnings("null") // suprime o aviso da análise estática apenas neste método
    public ResponseEntity<EntradaDiario> criar(@Valid @RequestBody EntradaDiario entrada) {
        // prova explícita e uso da variável salvo (IDE costuma aceitar isso)
        EntradaDiario salvo = Objects.requireNonNull(service.salvar(entrada), "Entrada salva é nula");

        Long salvoId = Objects.requireNonNull(salvo.getId(), "ID da entrada salva é nulo");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvoId)
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar entrada de diário")
    public ResponseEntity<EntradaDiario> atualizar(@PathVariable("id") long id, @Valid @RequestBody EntradaDiario entrada) {
        service.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diário não encontrado"));

        entrada.setId(id);
        EntradaDiario atualizado = Objects.requireNonNull(service.salvar(entrada), "Entrada atualizada é nula");
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover entrada de diário")
    public void remover(@PathVariable("id") long id) {
        service.remover(id);
    }
}