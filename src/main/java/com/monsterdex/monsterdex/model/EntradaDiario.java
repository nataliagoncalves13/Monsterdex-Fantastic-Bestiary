package com.monsterdex.monsterdex.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "entradas_diario")
public class EntradaDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O texto da entrada é obrigatório")
    @Column(columnDefinition = "TEXT", nullable = false) // Para textos longos
    private String texto;

    @PastOrPresent(message = "A data deve ser no presente ou passado")
    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criatura_id", nullable = false) 
    private Criatura criatura;

    public EntradaDiario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public Criatura getCriatura() { return criatura; }
    public void setCriatura(Criatura criatura) { this.criatura = criatura; }
}