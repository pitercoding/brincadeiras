package com.brincadeiras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CadastrarAtividadeRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String titulo;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String descricao;

    @NotEmpty
    private List<String> materiais;

    @NotBlank
    @Pattern(regexp = "\\d-\\d anos", message = "Formato deve ser 'X-Y anos'")
    private String faixaEtaria;
}
