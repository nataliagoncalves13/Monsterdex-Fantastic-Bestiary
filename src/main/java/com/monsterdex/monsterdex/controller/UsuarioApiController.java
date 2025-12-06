package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        // prova explícita para análise estática
        return Objects.requireNonNull(service.listar(), "Lista de usuários é nula");
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo usuário")
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuário de entrada não pode ser nulo");

        // solução: primeiro capture o retorno, depois aplique requireNonNull
        Usuario possivelSalvo = service.salvar(usuario);
        Usuario salvo = Objects.requireNonNull(possivelSalvo, "Usuário salvo é nulo");

        Long salvoId = Objects.requireNonNull(salvo.getId(), "ID do usuário salvo é nulo");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvoId)
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário por ID")
    public ResponseEntity<Void> remover(@PathVariable("id") long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Tentar fazer login")
    public ResponseEntity<?> login(@RequestBody Usuario credenciais) {
        Objects.requireNonNull(credenciais, "Credenciais não podem ser nulas");
        String username = Objects.requireNonNull(credenciais.getUsername(), "Username não pode ser nulo");

        Optional<Usuario> usuarioOpt = Optional.ofNullable(service.buscarPorUsername(username));

        String senhaFornecida = Objects.requireNonNullElse(credenciais.getPassword(), "");
        String senhaCadastrada = usuarioOpt.map(u -> Objects.requireNonNullElse(u.getPassword(), "")).orElse("");

        if (usuarioOpt.isPresent() && senhaFornecida.equals(senhaCadastrada)) {
            return ResponseEntity.ok().body("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }
    }
}