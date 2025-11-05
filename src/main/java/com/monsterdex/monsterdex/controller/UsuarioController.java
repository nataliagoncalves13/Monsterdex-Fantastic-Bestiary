package com.monsterdex.monsterdex.controller;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("usuarios", service.listar());
        return LIST_VIEW;
    }

    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return FORM_VIEW;
    }

   
    @PostMapping
    public String salvar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {

            return FORM_VIEW;
        }
        
        service.salvar(usuario);
        ra.addFlashAttribute("msg", "Usuário cadastrado com sucesso! (Senha ainda não criptografada!)");
        return "redirect:/usuarios/login"; 
    }
    

    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model, @RequestParam(required = false) String error, RedirectAttributes ra) {
        if (error != null) {
            model.addAttribute("msg", "Erro no Login: Usuário ou senha incorretos.");
        }
 
        return "usuarios/login"; 
    }

    @PostMapping("/{id}/remover")
    public String remover(@PathVariable Long id, RedirectAttributes ra) {
        service.remover(id);
        ra.addFlashAttribute("msg", "Usuário removido.");
        return "redirect:/usuarios";
    }
}