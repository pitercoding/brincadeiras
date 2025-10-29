package com.brincadeiras.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "atividades")
public class Atividade {

    @Id
    private String id;

    private String titulo;

    private String descricao;

    private List<String> materiais;

    private String faixaEtaria;

    public Atividade() {

    }

    public Atividade(String titulo, String descricao, List<String> materiais, String faixaEtaria) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.materiais = materiais;
        this.faixaEtaria = faixaEtaria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<String> materiais) {
        this.materiais = materiais;
    }

    public String getFaixaEtaria() {
        return faixaEtaria;
    }

    public void setFaixaEtaria(String faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }
}
