package com.brincadeiras.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Document(collection = "atividades")
public class Atividade {

    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String titulo;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String descricao;

    @NotEmpty
    private List<String> materiais;

    @Pattern(regexp = "\\d-\\d anos", message = "Formato deve ser 'X-Y anos'")
    private String faixaEtaria;

    @NotBlank
    private String tipo;

    public Atividade() {}

    public Atividade(String titulo, String descricao,
                     List<String> materiais, String faixaEtaria, String tipo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.materiais = materiais;
        this.faixaEtaria = faixaEtaria;
        this.tipo = tipo;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<String> getMateriais() { return materiais; }
    public void setMateriais(List<String> materiais) { this.materiais = materiais; }
    public String getFaixaEtaria() { return faixaEtaria; }
    public void setFaixaEtaria(String faixaEtaria) { this.faixaEtaria = faixaEtaria; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}