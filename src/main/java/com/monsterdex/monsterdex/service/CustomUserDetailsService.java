package com.monsterdex.monsterdex.service;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("--- TENTATIVA DE LOGIN ---");
        System.out.println("Buscando usuário: " + username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("ERRO: Usuário não encontrado no banco!");
                    return new UsernameNotFoundException("Usuário não encontrado: " + username);
                });

        System.out.println("Usuário encontrado! ID: " + usuario.getId());
        System.out.println("Senha salva no banco: " + usuario.getPassword());

        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList())
        );
    }
}