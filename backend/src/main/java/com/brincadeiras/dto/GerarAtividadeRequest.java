package com.brincadeiras.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GerarAtividadeRequest {

    @NotBlank(message = "Faixa etária é obrigatória")
    private String faixaEtaria;

    private String descricaoUsuario;
}