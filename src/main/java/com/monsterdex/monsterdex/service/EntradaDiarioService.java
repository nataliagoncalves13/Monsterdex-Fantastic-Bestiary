package com.monsterdex.monsterdex.service;

import com.monsterdex.monsterdex.model.EntradaDiario;
import com.monsterdex.monsterdex.model.Usuario;
import com.monsterdex.monsterdex.repository.EntradaDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; 

@Service
public class EntradaDiarioService {

    @Autowired
    private EntradaDiarioRepository diarioRepository;
    


    public EntradaDiario salvar(EntradaDiario entrada) {
       
        return diarioRepository.save(entrada);
    }

   
    public List<EntradaDiario> listarTodas() {
        return diarioRepository.findAll();
    }

 
    public List<EntradaDiario> listarPorUsuario(Usuario usuario) {
        return diarioRepository.findByUsuario(usuario);
    }
    
    
    public Optional<EntradaDiario> buscarPorId(Long id) {
        return diarioRepository.findById(id);
    }


    public void remover(Long id) {
        diarioRepository.deleteById(id);
    }
}