package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.EntradaDiario;
import com.monsterdex.monsterdex.service.EntradaDiarioService;
import com.monsterdex.monsterdex.service.CriaturaService;
import com.monsterdex.monsterdex.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/diario")
public class EntradaDiarioController {

    private final EntradaDiarioService diarioService;
    private final CriaturaService criaturaService;
    private final UsuarioService usuarioService;

    private static final String LIST_VIEW = "diario/list";
    private static final String FORM_VIEW = "diario/form";

    public EntradaDiarioController(EntradaDiarioService diarioService, CriaturaService criaturaService, UsuarioService usuarioService) {
        this.diarioService = diarioService;
        this.criaturaService = criaturaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("entradas", diarioService.listarTodas());
        return LIST_VIEW;
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("entradaDiario", new EntradaDiario());
        model.addAttribute("criaturas", criaturaService.listar());
        model.addAttribute("usuarios", usuarioService.listar());
        return FORM_VIEW;
    }

    @PostMapping
    @SuppressWarnings("null")
    public String salvar(@Valid @ModelAttribute("entradaDiario") EntradaDiario entrada, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("criaturas", criaturaService.listar());
            ra.addFlashAttribute("usuarios", usuarioService.listar());
            return FORM_VIEW;
        }

        Objects.requireNonNull(diarioService.salvar(entrada), "Entrada de diário salva é nula");

        ra.addFlashAttribute("msg", "Entrada de diário registrada com sucesso.");
        return "redirect:/diario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") long id, Model model) {
        EntradaDiario entrada = diarioService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Diário não encontrado"));

        model.addAttribute("entradaDiario", entrada);
        model.addAttribute("criaturas", criaturaService.listar());
        model.addAttribute("usuarios", usuarioService.listar());
        return FORM_VIEW;
    }

    @PostMapping("/{id}/remover")
    public String remover(@PathVariable("id") long id, RedirectAttributes ra) {
        diarioService.remover(id);
        ra.addFlashAttribute("msg", "Entrada de diário removida com sucesso.");
        return "redirect:/diario";
    }
}