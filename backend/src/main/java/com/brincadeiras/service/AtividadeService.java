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
        return  atividadeRepository.save(atividade);
    }

    public List<Atividade> getAllAtividades() {
        return atividadeRepository.findAll();
    }

    public Atividade getAtividadeById(String id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Atividade não encontrada"));
    }

    public void deleteAtividade(String id) {
        atividadeRepository.deleteById(id);
    }

    public Atividade updateAtividade(String id, Atividade novaAtividade) {
        Atividade existente = getAtividadeById(id);
        if (existente == null) throw new NoSuchElementException("Atividade não encontrada");
        existente.setTitulo(novaAtividade.getTitulo());
        existente.setDescricao(novaAtividade.getDescricao());
        existente.setMateriais(novaAtividade.getMateriais());
        existente.setFaixaEtaria(novaAtividade.getFaixaEtaria());
        return atividadeRepository.save(existente);
    }
}
