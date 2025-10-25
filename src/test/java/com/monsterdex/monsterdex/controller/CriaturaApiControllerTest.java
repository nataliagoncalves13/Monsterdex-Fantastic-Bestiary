package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.service.CriaturaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CriaturaApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class CriaturaApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CriaturaService service;

    private Criatura sample(Long id) {
        Criatura c = new Criatura();
        c.setId(id);
        c.setNome("Goblin");
        c.setTipo("Fada");
        c.setDescricao("Pequena criatura travessa");
        return c;
    }

    @Test
    @DisplayName("GET /api/criaturas deve listar")
    void listar() throws Exception {
        Mockito.when(service.listar()).thenReturn(List.of(sample(1L), sample(2L)));

        mvc.perform(get("/api/criaturas"))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$", hasSize(2)))
           .andExpect(jsonPath("$[0].id", is(1)))
           .andExpect(jsonPath("$[0].nome", is("Goblin")));
    }

    @Test
    @DisplayName("GET /api/criaturas/{id} deve retornar 200 quando existe")
    void buscarOk() throws Exception {
        Mockito.when(service.buscarPorId(1L)).thenReturn(sample(1L));

        mvc.perform(get("/api/criaturas/{id}", 1))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(1)))
           .andExpect(jsonPath("$.nome", is("Goblin")));
    }

    @Test
    @DisplayName("GET /api/criaturas/{id} deve retornar 404 quando não existe")
    void buscarNotFound() throws Exception {
        Mockito.when(service.buscarPorId(999L)).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));

        mvc.perform(get("/api/criaturas/{id}", 999))
           .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/criaturas deve criar e retornar 201 com Location")
    void criar() throws Exception {
        Criatura saved = sample(10L);
        Mockito.when(service.salvar(Mockito.any(Criatura.class))).thenReturn(saved);

        String json = "{\"nome\":\"Goblin\",\"tipo\":\"Fada\",\"descricao\":\"X\"}";

        mvc.perform(post("/api/criaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", endsWith("/api/criaturas/10")))
           .andExpect(jsonPath("$.id", is(10)))
           .andExpect(jsonPath("$.nome", is("Goblin")));
    }

    @Test
    @DisplayName("PUT /api/criaturas/{id} deve atualizar e retornar 200")
    void atualizar() throws Exception {
        Mockito.when(service.buscarPorId(5L)).thenReturn(sample(5L));
        Criatura updated = sample(5L);
        updated.setNome("Goblin Rei");
        Mockito.when(service.salvar(Mockito.any(Criatura.class))).thenReturn(updated);

        String json = "{\"nome\":\"Goblin Rei\",\"tipo\":\"Fada\",\"descricao\":\"Y\"}";

        mvc.perform(put("/api/criaturas/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(5)))
           .andExpect(jsonPath("$.nome", is("Goblin Rei")));
    }

    @Test
    @DisplayName("DELETE /api/criaturas/{id} deve retornar 204")
    void remover() throws Exception {
        mvc.perform(delete("/api/criaturas/{id}", 7))
           .andExpect(status().isNoContent());
        Mockito.verify(service).remover(7L);
    }
}
