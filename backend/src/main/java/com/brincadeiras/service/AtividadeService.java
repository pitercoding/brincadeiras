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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    // Modelo público da Hugging Face
    private static final String HUGGINGFACE_MODEL_URL =
            "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.2";

    private final WebClient webClient = WebClient.builder()
            .baseUrl(HUGGINGFACE_MODEL_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

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

    // IA — Gerar atividade criativa com Hugging Face
    public Atividade gerarComIA(GerarAtividadeRequest request) {
        log.info("Gerando nova atividade com IA (Hugging Face). Parâmetros: faixaEtaria={}, tipo={}, materiais={}",
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

        try {
            WebClient.RequestBodySpec requestSpec = webClient.post()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> response = requestSpec
                    .bodyValue(Map.of("inputs", prompt))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            log.info("Resposta bruta da IA: {}", response);

            String content;
            if (response.containsKey("generated_text")) {
                content = (String) response.get("generated_text");
            } else if (response instanceof List<?> list && !list.isEmpty()) {
                Object item = ((Map<?, ?>) list.get(0)).get("generated_text");
                content = item != null ? item.toString() : "";
            } else {
                throw new RuntimeException("Resposta inválida da IA");
            }

            Atividade atividadeGerada = mapper.readValue(content, Atividade.class);
            log.info("Atividade gerada com sucesso: {}", atividadeGerada.getTitulo());
            return atividadeGerada;

        } catch (Exception e) {
            log.error("Erro ao gerar atividade com IA: {}", e.getMessage());
            throw new RuntimeException("Falha ao gerar atividade com IA", e);
        }
    }
}