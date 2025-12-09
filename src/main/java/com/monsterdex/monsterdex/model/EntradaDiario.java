package com.monsterdex.monsterdex.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "entradas_diario") 
public class EntradaDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titulo; 

    @Column(nullable = false)
    private LocalDateTime dataEncontro; 

    @NotBlank
    @Column(length = 2000)
    private String observacoes; 

    @ManyToOne 
    @JoinColumn(name = "usuario_id", nullable = false) 
    private Usuario usuario;

    @ManyToOne // 
    @JoinColumn(name = "criatura_id", nullable = false) 
    private Criatura criatura;

    public EntradaDiario() {
        this.dataEncontro = LocalDateTime.now(); 
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getDataEncontro() { return dataEncontro; }
    public void setDataEncontro(LocalDateTime dataEncontro) { this.dataEncontro = dataEncontro; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Criatura getCriatura() { return criatura; }
    public void setCriatura(Criatura criatura) { this.criatura = criatura; }
}