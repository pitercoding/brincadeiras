package com.brincadeiras.service;

import com.brincadeiras.dto.GerarAtividadeRequest;
import com.brincadeiras.model.Atividade;
import com.brincadeiras.repository.AtividadeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.model}")
    private String openAiModel;

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

    // ========================= IA — GERAÇÃO DE OBJETO COMPLETO ========================= //
    public Atividade gerarAtividadeComIA(GerarAtividadeRequest request) {
        String modelToUse = "gpt-3.5-turbo";
        if (openAiModel != null && !openAiModel.isBlank() && !openAiModel.equals("gpt-5-nano")) {
            modelToUse = openAiModel;
        }
        log.info("Gerando atividade completa com IA usando modelo: {}", modelToUse);

        OpenAiService service = new OpenAiService(openAiApiKey);

        // Prompt aprimorado para garantir um formato de saída estruturado e fácil de parsear
        String prompt = String.format(
                "Você é um especialista em atividades infantis. Crie uma única brincadeira divertida, simples e segura para crianças de %s anos. %s\n" +
                        "A resposta DEVE seguir estritamente o formato:\n" +
                        "TÍTULO: [Título da Atividade]\n" +
                        "MATERIAIS: [Lista de materiais separados por vírgula]\n" +
                        "DESCRIÇÃO: [Instruções detalhadas da atividade]",
                request.getFaixaEtaria(),
                (request.getDescricaoUsuario() != null && !request.getDescricaoUsuario().isBlank())
                        ? "Restrição: " + request.getDescricaoUsuario().trim() + "."
                        : "Use materiais comuns encontrados em casa."
        );

        ChatMessage message = new ChatMessage("user", prompt);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(modelToUse)
                .messages(List.of(message))
                .temperature(0.8)
                .maxTokens(350)
                .build();

        ChatCompletionResult result = service.createChatCompletion(completionRequest);
        String textoGerado = result.getChoices().get(0).getMessage().getContent().trim();

        log.info("Texto gerado pela IA:\n{}", textoGerado);

        // Extração aprimorada usando Regex para TÍTULO, MATERIAIS e DESCRIÇÃO
        Atividade atividade = parseAtividade(textoGerado);

        // Preenchendo campos restantes
        atividade.setId(UUID.randomUUID().toString());
        atividade.setFaixaEtaria(request.getFaixaEtaria());
        atividade.setTipo("IA");

        // Fallback para materiais se a extração falhar
        if (atividade.getMateriais() == null || atividade.getMateriais().isEmpty()) {
            atividade.setMateriais(Arrays.asList("Materiais comuns em casa"));
        }

        log.info("Atividade gerada: {}", atividade.getTitulo());
        return atividade;
    }

    /**
     * Extrai Título, Materiais e Descrição do texto gerado pela IA usando Regex.
     */
    private Atividade parseAtividade(String texto) {
        Atividade atividade = new Atividade();

        // Regex para capturar os três campos baseados no formato estruturado
        Pattern pattern = Pattern.compile(
                "TÍTULO:\\s*(.*?)\\s*MATERIAIS:\\s*(.*?)\\s*DESCRIÇÃO:\\s*(.*)",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            String titulo = matcher.group(1).trim();
            String materiaisStr = matcher.group(2).trim();
            String descricao = matcher.group(3).trim();

            atividade.setTitulo(titulo);
            atividade.setDescricao(descricao);

            // Converte a string de materiais separada por vírgula em uma lista
            List<String> materiaisList = new ArrayList<>();
            if (!materiaisStr.isEmpty()) {
                // Remove pontuações finais e divide por vírgula
                String cleanedMaterials = materiaisStr.replaceAll("[.;]$", "");
                materiaisList = Arrays.asList(cleanedMaterials.split("\\s*,\\s*"));
            }
            atividade.setMateriais(materiaisList);

        } else {
            // Fallback: Se o regex falhar, usa o texto inteiro como descrição e a primeira linha como título
            String[] linhas = texto.split("\\r?\\n");
            atividade.setTitulo(linhas.length > 0 ? linhas[0].trim() : "Brincadeira Gerada");
            atividade.setDescricao(texto);
            atividade.setMateriais(Arrays.asList("Materiais comuns em casa"));
            log.warn("Falha ao parsear a resposta da IA com Regex. Usando fallback.");
        }

        return atividade;
    }

    // O método extrairTitulo mantido por segurança
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