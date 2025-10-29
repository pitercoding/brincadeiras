package com.brincadeiras.service;

import com.brincadeiras.model.Atividade;
import com.brincadeiras.repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return atividadeRepository.findById(id).orElse(null);
    }

    public void deleteAtividade(String id) {
        atividadeRepository.deleteById(id);
    }

    public Atividade updateAtividade(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }
}
