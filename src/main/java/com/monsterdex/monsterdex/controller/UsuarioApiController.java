package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "API REST para gerenciamento de usuários")
public class UsuarioApiController {

    private final UsuarioService service;

    public UsuarioApiController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public List<Usuario> listar() {
        return service.listar();
    }


    @PostMapping
    @Operation(summary = "Cadastrar um novo usuário")
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        Usuario salvo = service.salvar(usuario);
        return ResponseEntity.created(URI.create("/api/usuarios/" + salvo.getId())).body(salvo);
    }

    // Rota para Remover
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário por ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
    

    @PostMapping("/login")
    @Operation(summary = "Tentar fazer login")
    public ResponseEntity<?> login(@RequestBody Usuario credenciais) {
        Usuario usuarioEncontrado = service.buscarPorUsername(credenciais.getUsername());

        if (usuarioEncontrado != null && credenciais.getPassword().equals(usuarioEncontrado.getPassword())) {
        
            return ResponseEntity.ok().body("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }
    }
}