package com.monsterdex.monsterdex.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.repository.UsuarioRepository;
import com.monsterdex.monsterdex.service.CriaturaService;
import com.monsterdex.monsterdex.service.UnsplashService;
import com.monsterdex.monsterdex.service.WeatherService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/criaturas")
public class CriaturaController {

    private final CriaturaService criaturaService;
    private final UnsplashService unsplashService;
    private final WeatherService weatherService;
    private final UsuarioRepository usuarioRepository;

    public CriaturaController(CriaturaService criaturaService, UnsplashService unsplashService, WeatherService weatherService, UsuarioRepository usuarioRepository) {
        this.criaturaService = criaturaService;
        this.unsplashService = unsplashService;
        this.weatherService = weatherService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("criaturas", criaturaService.listar());
        return "criaturas/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("criatura", new Criatura());
        return "criaturas/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Long id, Model model) {
        Criatura criatura = criaturaService.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        
        model.addAttribute("criatura", criatura);
        return "criaturas/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("criatura") Criatura criatura, BindingResult result, RedirectAttributes redirectAttributes, Principal principal) {
        
        if (result.hasErrors()) {
            return "criaturas/form";
        }

        if (principal != null) {
            String username = principal.getName();
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado no banco"));
            criatura.setUsuario(usuario);
        } else {
            Usuario u = new Usuario(); u.setId(1L); 
            criatura.setUsuario(u);
        }

        try {
            if (criatura.getImagemUrl() == null || criatura.getImagemUrl().isEmpty()) {
                String imagem = unsplashService.buscarImagemPorNomeETipo(criatura.getNome(), criatura.getTipo());
                criatura.setImagemUrl(imagem);
            }
        } catch (Exception e) {
            System.out.println("Aviso: Não foi possível buscar imagem na API externa.");
        }

        try {
            if (criatura.getHabitat() != null && !criatura.getHabitat().isBlank()) {
                String clima = weatherService.buscarClimaDoHabitat(criatura.getHabitat());
                criatura.setClima(clima);
            }
        } catch (Exception e) {
            System.out.println("Aviso: Não foi possível buscar clima na API externa.");
        }

        criaturaService.salvar(criatura);
        
        redirectAttributes.addFlashAttribute("msg", "Criatura catalogada com sucesso!");
        return "redirect:/criaturas";
    }

    @PostMapping("/{id}/remover")
    public String remover(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        criaturaService.remover(id);
        redirectAttributes.addFlashAttribute("msg", "Criatura removida do bestiário.");
        return "redirect:/criaturas";
    }
}