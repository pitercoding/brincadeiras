package com.brincadeiras.service;

import com.brincadeiras.dto.GerarAtividadeRequest;
import com.brincadeiras.model.Atividade;
import com.brincadeiras.repository.AtividadeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    @Value("${huggingface.api.key}")
    private String huggingFaceApiKey;

    @Value("${huggingface.model.url}")
    private String huggingFaceModelUrl;

    // ========================= CRUD ========================= //
    public Atividade postAtividade(Atividade atividade) {
        log.info("Salvando nova atividade: {}", atividade.getTitulo());
        try {
            Atividade salva = atividadeRepository.save(atividade);
            log.info("Atividade salva com sucesso. ID: {}", salva.getId());
            return salva;
        } catch (Exception e) {
            log.error("Erro ao salvar atividade '{}': {}", atividade.getTitulo(), e.getMessage());
            throw e;
        }
    }

    public List<Atividade> getAllAtividades() {
        log.info("Buscando todas as atividades no banco de dados");
        return atividadeRepository.findAll();
    }

    public Atividade getAtividadeById(String id) {
        log.info("Buscando atividade com id: {}", id);
        return atividadeRepository.findById(id)
                .map(a -> { log.info("Atividade encontrada: {}", a.getTitulo()); return a; })
                .orElseThrow(() -> {
                    log.warn("Atividade com id {} não encontrada", id);
                    return new NoSuchElementException("Atividade não encontrada");
                });
    }

    public void deleteAtividade(String id) {
        log.info("Deletando atividade com id: {}", id);
        try {
            atividadeRepository.deleteById(id);
            log.info("Atividade deletada com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao deletar atividade: {}", e.getMessage());
            throw e;
        }
    }

    public Atividade updateAtividade(String id, Atividade novaAtividade) {
        log.info("Atualizando atividade com id: {}", id);
        Atividade existente = getAtividadeById(id);

        existente.setTitulo(novaAtividade.getTitulo());
        existente.setDescricao(novaAtividade.getDescricao());
        existente.setMateriais(novaAtividade.getMateriais());
        existente.setFaixaEtaria(novaAtividade.getFaixaEtaria());
        existente.setTipo(novaAtividade.getTipo());

        try {
            return atividadeRepository.save(existente);
        } catch (Exception e) {
            log.error("Erro ao atualizar atividade: {}", e.getMessage());
            throw e;
        }
    }

    // ========================= IA — GERAÇÃO DE TEXTO ========================= //
    public String gerarTextoComIA(GerarAtividadeRequest request) {
        log.info("Gerando texto de atividade com IA — HF na URL: {}", huggingFaceModelUrl);

        String promptBase = String.format(
                "Você é um especialista em atividades infantis. Crie uma única brincadeira infantil divertida, simples e segura para crianças de %s anos. ",
                request.getFaixaEtaria()
        );

        String userExtra = (request.getDescricaoUsuario() != null && !request.getDescricaoUsuario().isBlank())
                ? "Restrição: " + request.getDescricaoUsuario().trim() + ". "
                : "Seja criativo e use materiais comuns encontrados em casa. ";

        String prompt = promptBase + userExtra +
                "Estrutura da Resposta: Título, Materiais Necessários e Instruções Simples. Responda apenas com o texto da atividade.";

        WebClient client = WebClient.builder()
                .baseUrl(huggingFaceModelUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + huggingFaceApiKey)
                .build();

        Map<String, Object> params = Map.of(
                "max_new_tokens", 250,
                "temperature", 0.8,
                "do_sample", true
        );

        Map<String, Object> requestBody = Map.of(
                "inputs", prompt,
                "parameters", params,
                "options", Map.of("wait_for_model", true)
        );

        List<Map<String, Object>> responseList;
        try {
            responseList = client.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro ao chamar a API da Hugging Face: {}", e.getMessage());
            return "Erro ao gerar atividade com IA.";
        }

        if (responseList == null || responseList.isEmpty() || !responseList.get(0).containsKey("generated_text")) {
            log.warn("Resposta inválida da Hugging Face: {}", responseList);
            return "Não foi possível gerar uma atividade no momento.";
        }

        String rawGeneratedText = responseList.get(0).get("generated_text").toString();
        if (rawGeneratedText.startsWith(prompt)) {
            return rawGeneratedText.substring(prompt.length()).trim();
        }
        return rawGeneratedText.trim();
    }

    // ========================= IA — RETORNAR OBJETO COMPLETO ========================= //
    public Atividade gerarAtividadeComIA(GerarAtividadeRequest request) {
        log.info("Gerando atividade completa com IA.");

        String textoGerado = gerarTextoComIA(request);

        // Extrair título
        String titulo = extrairTitulo(textoGerado);

        // Valores padrão
        List<String> materiais = new ArrayList<>();
        materiais.add("Materiais comuns em casa");
        String tipo = "IA";

        Atividade atividade = new Atividade();
        atividade.setId(UUID.randomUUID().toString()); // gerar ID temporário
        atividade.setTitulo(titulo);
        atividade.setDescricao(textoGerado);
        atividade.setFaixaEtaria(request.getFaixaEtaria());
        atividade.setMateriais(materiais);
        atividade.setTipo(tipo);

        log.info("Atividade gerada: {}", atividade.getTitulo());
        return atividade;
    }

    private String extrairTitulo(String texto) {
        if (texto == null || texto.isBlank()) return "Brincadeira Gerada";

        String[] linhas = texto.split("\\r?\\n");
        String primeira = linhas[0];
        if (primeira.toLowerCase().contains("título:")) {
            return primeira.replace("Título:", "").trim();
        }
        return primeira.trim();
    }
}