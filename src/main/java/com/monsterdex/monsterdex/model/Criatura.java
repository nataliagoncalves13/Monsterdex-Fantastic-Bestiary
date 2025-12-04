package com.monsterdex.monsterdex.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "criaturas")
public class Criatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String nome;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String tipo;

    @Size(max = 2000)
    @Column(length = 2000)
    private String descricao;

    @Size(max = 500)
    @Column(length = 500)
    private String imagemUrl;

    @Size(max = 500)
    @Column(length = 500)
    private String habitat;

    @Size(max = 500)
    @Column(length = 500)
    private String clima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false) // Chave estrangeira
    private Usuario usuario;

    @OneToMany(mappedBy = "criatura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntradaDiario> entradasDiario;

    public Criatura() {}

    public Criatura(Long id, String nome, String tipo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public String getHabitat() { return habitat; }
    public void setHabitat(String habitat) { this.habitat = habitat; }

    public String getClima() { return clima; }
    public void setClima(String clima) { this.clima = clima; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<EntradaDiario> getEntradasDiario() { return entradasDiario; }
    public void setEntradasDiario(List<EntradaDiario> entradasDiario) { this.entradasDiario = entradasDiario; }

}
