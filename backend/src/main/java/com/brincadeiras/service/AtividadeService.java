package com.brincadeiras.service;

import com.brincadeiras.model.Atividade;
import com.brincadeiras.repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

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
            log.info("Atividade com id {} deletada com sucesso", id);
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
            log.info("Atividade com id {} atualizada com sucesso", id);
            return atualizada;
        } catch (Exception e) {
            log.error("Erro ao atualizar atividade com id {}: {}", id, e.getMessage());
            throw e;
        }
    }
}