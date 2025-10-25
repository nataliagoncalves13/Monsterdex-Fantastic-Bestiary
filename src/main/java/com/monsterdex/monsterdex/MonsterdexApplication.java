package com.monsterdex.monsterdex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.monsterdex.monsterdex.model.Criatura;
import com.monsterdex.monsterdex.repository.CriaturaRepository;

import java.util.List;

@SpringBootApplication
public class MonsterdexApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonsterdexApplication.class, args);
	}

	// Seed de dados simples quando rodando no perfil 'dev'
	@Bean
	@Profile("dev")
	CommandLineRunner seedDevData(CriaturaRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				List<Criatura> seeds = List.of(
					new Criatura(null, "Goblin", "Fada", "Criatura pequena e astuta, comum em florestas sombrias."),
					new Criatura(null, "Dragão Rubro", "Dracônico", "Lendário, sopro de fogo e vastos tesouros."),
					new Criatura(null, "Troll das Cavernas", "Gigante", "Forte, regeneração lenta, teme a luz do sol.")
				);
				repo.saveAll(seeds);
			}
		};
	}
}
