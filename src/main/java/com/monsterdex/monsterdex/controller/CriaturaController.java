package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.service.CriaturaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/criaturas")
public class CriaturaController {

    private final CriaturaService service;

    private static final String LIST_VIEW = "criaturas/list";
    private static final String FORM_VIEW = "criaturas/form";

    public CriaturaController(CriaturaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("criaturas", service.listar());
        return LIST_VIEW;
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("criatura", new Criatura());
        return FORM_VIEW;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        model.addAttribute("criatura", service.buscarPorId(id));
        return FORM_VIEW;
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("criatura") Criatura criatura, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            return FORM_VIEW;
        }
        boolean edicao = criatura.getId() != null;
        service.salvar(criatura);
        ra.addFlashAttribute("msg", edicao ? "Criatura atualizada com sucesso." : "Criatura criada com sucesso.");
        return "redirect:/criaturas";
    }

    @PostMapping("/{id}/remover")
    public String remover(@PathVariable("id") Long id, RedirectAttributes ra) {
        service.remover(id);
        ra.addFlashAttribute("msg", "Criatura removida com sucesso.");
        return "redirect:/criaturas";
    }

    // Fallback para quem acessar via GET (ex.: link antigo). Mantemos apenas um redirect amigável.
    @GetMapping("/{id}/remover")
    public String removerGet(@PathVariable("id") Long id, RedirectAttributes ra) {
        ra.addFlashAttribute("msg", "Remoção deve ser feita pelo botão (POST). Ação ignorada.");
        return "redirect:/criaturas";
    }
}
