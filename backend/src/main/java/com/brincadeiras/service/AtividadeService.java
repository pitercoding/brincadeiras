package com.brincadeiras.service;

import com.brincadeiras.model.Atividade;
import com.brincadeiras.repository.AtividadeRepository;
import com.brincadeiras.dto.GerarAtividadeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    @Value("${huggingface.api.key}")
    private String huggingFaceApiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    // Lista de modelos gratuitos para tentar
    private static final String[] MODELOS = {
            "https://api-inference.huggingface.co/models/gpt2",
            "https://api-inference.huggingface.co/models/distilgpt2",
            "https://api-inference.huggingface.co/models/EleutherAI/gpt-neo-125M",
            "https://api-inference.huggingface.co/models/facebook/opt-125m"
    };

    private WebClient buildClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // ========== CRUD ==========

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
        List<Atividade> atividades = atividadeRepository.findAll();
        log.info("Total de atividades encontradas: {}", atividades.size());
        return atividades;
    }

    public Atividade getAtividadeById(String id) {
        log.info("Buscando atividade com id: {}", id);
        return atividadeRepository.findById(id)
                .map(atividade -> {
                    log.info("Atividade encontrada: {}", atividade.getTitulo());
                    return atividade;
                })
                .orElseThrow(() -> {
                    log.warn("Atividade com id {} não encontrada", id);
                    return new NoSuchElementException("Atividade não encontrada");
                });
    }

    public void deleteAtividade(String id) {
        log.info("Deletando atividade com id: {}", id);
        try {
            atividadeRepository.deleteById(id);
            log.info("Atividade deletada com sucesso. ID: {}", id);
        } catch (Exception e) {
            log.error("Erro ao deletar atividade com id {}: {}", id, e.getMessage());
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

        try {
            Atividade atualizada = atividadeRepository.save(existente);
            log.info("Atividade atualizada com sucesso. ID: {}", id);
            return atualizada;
        } catch (Exception e) {
            log.error("Erro ao atualizar atividade com id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    // ========== IA — Geração com Hugging Face ==========

    public Atividade gerarComIA(GerarAtividadeRequest request) {
        log.info("Gerando nova atividade com IA. Parâmetros: faixaEtaria={}, tipo={}, materiais={}",
                request.getFaixaEtaria(), request.getTipo(), request.getMateriais());

        String prompt = String.format(
                "Crie uma brincadeira infantil original, divertida e segura para crianças de %s anos. " +
                        "Tipo de atividade: %s. Materiais disponíveis: %s. " +
                        "Responda SOMENTE em JSON com este formato: " +
                        "{\"titulo\":\"...\", \"descricao\":\"...\", \"materiais\":[\"...\"], \"faixaEtaria\":\"...\"}",
                request.getFaixaEtaria(),
                request.getTipo() != null ? request.getTipo() : "geral",
                String.join(", ", request.getMateriais())
        );

        for (String modelUrl : MODELOS) {
            try {
                Atividade atividade = gerarComModelo(modelUrl, prompt);
                if (atividade != null) {
                    log.info("Atividade gerada com sucesso pelo modelo: {}", modelUrl);
                    return atividade;
                }
                log.warn("Modelo {} falhou, tentando próximo...", modelUrl);
            } catch (Exception e) {
                log.error("Erro no modelo {}: {}", modelUrl, e.getMessage());
            }
        }

        throw new RuntimeException("Falha ao gerar atividade com IA (todos os modelos falharam).");
    }

    private Atividade gerarComModelo(String modelUrl, String prompt) {
        WebClient client = buildClient(modelUrl);

        List<Map<String, Object>> response = client.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + huggingFaceApiKey)
                .bodyValue(Map.of("inputs", prompt))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();

        if (response == null || response.isEmpty() || !response.get(0).containsKey("generated_text")) {
            log.warn("Resposta inválida ou vazia recebida do modelo: {}", modelUrl);
            return null;
        }

        try {
            String content = (String) response.get(0).get("generated_text");
            log.debug("Texto bruto gerado: {}", content);
            return mapper.readValue(content, Atividade.class);
        } catch (Exception e) {
            log.error("Erro ao processar JSON do modelo {}: {}", modelUrl, e.getMessage());
            return null;
        }
    }
}