package com.brincadeiras.controller;

import com.brincadeiras.dto.GerarAtividadeRequest;
import com.brincadeiras.model.Atividade;
import com.brincadeiras.service.AtividadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;

    @PostMapping
    public ResponseEntity<Atividade> postAtividade(@RequestBody @Valid Atividade atividade) {
        log.info("Recebendo requisição para criar nova atividade: {}", atividade.getTitulo());
        Atividade criada = atividadeService.postAtividade(atividade);
        log.info("Atividade criada com sucesso: {}", criada.getId());
        return ResponseEntity.status(201).body(criada);
    }

    @GetMapping()
    public List<Atividade> getAllAtividade(){
        log.info("Buscando todas as atividades");
        List<Atividade> atividades = atividadeService.getAllAtividades();
        log.info("Total de atividades encontradas: {}", atividades.size());
        return atividades;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> getAtividadeById(@PathVariable String id) {
        log.info("Buscando atividade com id: {}", id);
        Atividade atividade = atividadeService.getAtividadeById(id);
        if (atividade == null) {
            log.warn("Atividade com id {} não encontrada", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Atividade encontrada: {}", atividade.getTitulo());
        return ResponseEntity.ok(atividade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAtividade(@PathVariable String id) {
        log.info("Solicitação para deletar atividade com id: {}", id);
        Atividade existingAtividade = atividadeService.getAtividadeById(id);
        if (existingAtividade == null) {
            log.warn("Tentativa de deletar atividade inexistente (id: {})", id);
            return ResponseEntity.notFound().build();
        }
        atividadeService.deleteAtividade(id);
        log.info("Atividade com id {} deletada com sucesso", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atividade> updateAtividade(@PathVariable String id,
                                                     @RequestBody @Valid Atividade atividade) {
        log.info("Solicitação para atualizar atividade com id: {}", id);
        Atividade atualizada = atividadeService.updateAtividade(id, atividade);
        log.info("Atividade com id {} atualizada com sucesso", id);
        return ResponseEntity.ok(atualizada);
    }

    @PostMapping("/gerar")
    public ResponseEntity<Atividade> gerarAtividadeComIA(@RequestBody @Valid GerarAtividadeRequest request) {
        log.info("Recebendo requisição para gerar atividade com IA: faixaEtaria={}, tipo={}, materiais={}",
                request.getFaixaEtaria(), request.getTipo(), request.getMateriais());
        Atividade gerada = atividadeService.gerarComIA(request);
        log.info("Atividade gerada com sucesso: {}", gerada.getTitulo());
        return ResponseEntity.status(201).body(gerada);
    }
}
