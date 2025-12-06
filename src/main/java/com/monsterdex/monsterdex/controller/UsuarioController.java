package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    private static final String FORM_VIEW = "usuarios/form";
    private static final String LIST_VIEW = "usuarios/list";

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", Objects.requireNonNull(service.listar(), "Lista de usuários é nula"));
        return LIST_VIEW;
    }

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return FORM_VIEW;
    }

    @PostMapping
    @SuppressWarnings("null")
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            return FORM_VIEW;
        }

        // prova explícita que a entrada não é nula
        Objects.requireNonNull(usuario, "Objeto usuario não pode ser nulo");

        // capture o retorno e verifique, mas NÃO crie variável não utilizada
        Usuario possivelSalvo = service.salvar(usuario);
        Objects.requireNonNull(possivelSalvo, "Falha ao salvar usuário");

        ra.addFlashAttribute("msg", "Usuário cadastrado com sucesso! (Senha ainda não criptografada!)");
        return "redirect:/login";
    }

    @PostMapping("/{id}/remover")
    public String remover(@PathVariable("id") long id, RedirectAttributes ra) {
        service.remover(id);
        ra.addFlashAttribute("msg", "Usuário removido.");
        return "redirect:/usuarios";
    }
}