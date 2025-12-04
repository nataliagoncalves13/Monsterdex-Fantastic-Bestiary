package com.monsterdex.monsterdex.service;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.repository.CriaturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CriaturaService {

    private final CriaturaRepository repository;
    private final UnsplashService unsplashService;
    private final WeatherService weatherService;

    public CriaturaService(CriaturaRepository repository, UnsplashService unsplashService, WeatherService weatherService) {
        this.repository = repository;
        this.unsplashService = unsplashService;
        this.weatherService = weatherService;
    }

    @Transactional(readOnly = true)
    public List<Criatura> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Criatura buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Criatura não encontrada"));
    }

    @Transactional
    public Criatura salvar(Criatura c) {
        // Busca imagem automaticamente se não tiver uma definida
        if (c.getImagemUrl() == null || c.getImagemUrl().isEmpty()) {
            String imagemUrl = unsplashService.buscarImagemPorNomeETipo(c.getNome(), c.getTipo());
            if (imagemUrl != null) {
                c.setImagemUrl(imagemUrl);
            }
        }
        
        // Busca informações de clima automaticamente se não tiver definidas
        if (c.getClima() == null || c.getClima().isEmpty()) {
            if (c.getHabitat() != null && !c.getHabitat().isEmpty()) {
                String clima = weatherService.buscarClimaDoHabitat(c.getHabitat());
                if (clima != null) {
                    c.setClima(clima);
                }
            }
        }
        
        return repository.save(c);
    }

    @Transactional
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Criatura não encontrada");
        }
        repository.deleteById(id);
    }
}
