package com.brincadeiras.dto;

import jakarta.validation.constraints.NotBlank;

public class GerarAtividadeRequest {

    @NotBlank(message = "Faixa etária é obrigatória")
    private String faixaEtaria;

    private String descricaoUsuario;

    // Getters e Setters
    public String getFaixaEtaria() { return faixaEtaria; }
    public void setFaixaEtaria(String faixaEtaria) { this.faixaEtaria = faixaEtaria; }

    public String getDescricaoUsuario() { return descricaoUsuario; }
    public void setDescricaoUsuario(String descricaoUsuario) { this.descricaoUsuario = descricaoUsuario; }
}
