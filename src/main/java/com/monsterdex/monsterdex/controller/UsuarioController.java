package com.monsterdex.monsterdex.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.repository.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

@PostMapping("/cadastro")
    public String salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes) {
        
        System.out.println("--- TENTANDO CADASTRAR USUÁRIO ---");
        System.out.println("Username: " + usuario.getUsername());

        if (result.hasErrors()) {
            System.out.println("❌ ERRO DE VALIDAÇÃO! O usuário NÃO foi salvo.");
            result.getAllErrors().forEach(error -> System.out.println("Erro: " + error.getDefaultMessage()));
            return "usuarios/form";
        }

        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            System.out.println("❌ ERRO: Usuário já existe no banco.");
            result.rejectValue("username", "error.usuario", "Este nome de usuário já está em uso.");
            return "usuarios/form";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRoles(Collections.singleton("USER"));

        usuarioRepository.save(usuario);
        System.out.println("✅ SUCESSO! Usuário salvo com ID: " + usuario.getId());
        
        redirectAttributes.addFlashAttribute("msg", "Guerreiro registrado! Entre com suas credenciais.");
        return "redirect:/login";
    }
}