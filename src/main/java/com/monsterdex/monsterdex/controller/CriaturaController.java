package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.dto.CriaturaRequest;
import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.service.CriaturaService;
import com.monsterdex.monsterdex.service.UnsplashService;
import com.monsterdex.monsterdex.service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Controller responsável por endpoints relacionados a criaturas.
 * Observação: existe também CriaturaApiController (para testes/REST).
 */
@RestController
@RequestMapping("/criaturas")
public class CriaturaController {

    @Autowired
    private CriaturaService criaturaService;

    @Autowired
    private UnsplashService unsplashService;

    @Autowired
    private WeatherService weatherService;

    private Usuario getUsuarioFake() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNome("Usuário Teste");
        u.setUsername("usuario_teste");
        u.setEmail("teste@exemplo.com");
        return u;
    }

    // ---------- LISTAR TODAS (web) ----------
    @GetMapping("/web")
    public ResponseEntity<List<Criatura>> listar() {
        return ResponseEntity.ok(criaturaService.listarTodas());
    }

    // ---------- BUSCAR POR ID ----------
    @GetMapping("/{id}")
    public ResponseEntity<Criatura> buscarPorId(@PathVariable("id") long id) {
        // Use primitive long to avoid null-safety warnings; autoboxing will convert se necessário.
        return ResponseEntity.ok(criaturaService.buscarPorId(id));
    }

    // ---------- CRIAR ----------
    @PostMapping
    public ResponseEntity<Criatura> criar(@Validated @RequestBody CriaturaRequest dto) {

        Usuario usuario = getUsuarioFake();

        Criatura criatura = new Criatura();
        criatura.setNome(dto.getNome());
        criatura.setTipo(dto.getTipo());
        criatura.setDescricao(dto.getDescricao());
        criatura.setHabitat(dto.getHabitat());
        criatura.setUsuario(usuario);

        // Unsplash
        String imagem = unsplashService.buscarImagemPorNomeETipo(dto.getNome(), dto.getTipo());
        criatura.setImagemUrl(imagem);

        // Clima do habitat
        if (dto.getHabitat() != null && !dto.getHabitat().isBlank()) {
            String clima = weatherService.buscarClimaDoHabitat(dto.getHabitat());
            criatura.setClima(clima);
        }

        Criatura criada = criaturaService.salvar(criatura);

        // Garante que o id salvo não seja nulo antes de passar para buildAndExpand,
        // evitando warnings de "Null type safety" e possíveis NPEs.
        Long criadaId = Objects.requireNonNull(criada.getId(), "ID do recurso salvo é nulo");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criadaId)
                .toUri();

        return ResponseEntity.created(location).body(criada);
    }

    // ---------- ATUALIZAR ----------
    @PutMapping("/{id}")
    public ResponseEntity<Criatura> atualizar(
            @PathVariable("id") long id,
            @Validated @RequestBody CriaturaRequest dto
    ) {
        Usuario usuario = getUsuarioFake();
        Criatura criatura = criaturaService.buscarPorId(id);

        criatura.setNome(dto.getNome());
        criatura.setTipo(dto.getTipo());
        criatura.setDescricao(dto.getDescricao());
        criatura.setHabitat(dto.getHabitat());
        criatura.setUsuario(usuario);

        String imagem = unsplashService.buscarImagemPorNomeETipo(dto.getNome(), dto.getTipo());
        criatura.setImagemUrl(imagem);

        if (dto.getHabitat() != null && !dto.getHabitat().isBlank()) {
            String clima = weatherService.buscarClimaDoHabitat(dto.getHabitat());
            criatura.setClima(clima);
        }

        Criatura atualizada = criaturaService.salvar(criatura);

        return ResponseEntity.ok(atualizada);
    }

    // ---------- DELETAR ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") long id) {
        criaturaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}