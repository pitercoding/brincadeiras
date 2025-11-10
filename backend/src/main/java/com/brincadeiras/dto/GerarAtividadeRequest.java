package com.brincadeiras.dto;

import java.util.List;

public class GerarAtividadeRequest {
    private String faixaEtaria;
    private List<String> materiais;
    private String tipo; // opcional (ex: artes, sensorial)

    // Getters e Setters
    public String getFaixaEtaria() {
        return faixaEtaria;
    }
    public void setFaixaEtaria(String faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }

    public List<String> getMateriais() {
        return materiais;
    }
    public void setMateriais(List<String> materiais) {
        this.materiais = materiais;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
