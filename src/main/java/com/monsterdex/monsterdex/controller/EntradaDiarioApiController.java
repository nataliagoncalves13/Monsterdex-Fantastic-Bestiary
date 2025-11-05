package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.EntradaDiario;
import com.monsterdex.monsterdex.service.EntradaDiarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public EntradaDiario buscar(@PathVariable("id") Long id) {
        return service.buscarPorId(id).orElseThrow(() -> new RuntimeException("Diário não encontrado"));
    }

   
    @PostMapping
    @Operation(summary = "Criar nova entrada de diário")
    public ResponseEntity<EntradaDiario> criar(@Valid @RequestBody EntradaDiario entrada) {
    
        
        EntradaDiario salvo = service.salvar(entrada);
        return ResponseEntity.created(URI.create("/api/diario/" + salvo.getId())).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar entrada de diário")
    public EntradaDiario atualizar(@PathVariable("id") Long id, @Valid @RequestBody EntradaDiario entrada) {
   
        service.buscarPorId(id); 
        entrada.setId(id);
        return service.salvar(entrada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover entrada de diário")
    public void remover(@PathVariable("id") Long id) {
        service.remover(id);
    }
}