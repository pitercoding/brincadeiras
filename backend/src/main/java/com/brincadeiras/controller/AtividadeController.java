package com.brincadeiras.controller;

import com.brincadeiras.dto.CadastrarAtividadeRequest;
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

    // ========== CADASTRAR ATIVIDADE MANUAL ==========
    @PostMapping
    public ResponseEntity<Atividade> postAtividade(@RequestBody @Valid CadastrarAtividadeRequest request) {
        Atividade atividade = new Atividade(
                request.getTitulo(),
                request.getDescricao(),
                request.getMateriais(),
                request.getFaixaEtaria(),
                "Manual"
        );

        Atividade criada = atividadeService.postAtividade(atividade);
        return ResponseEntity.status(201).body(criada);
    }

    // ========== LISTAR TODAS ==========
    @GetMapping
    public List<Atividade> getAllAtividade() {
        return atividadeService.getAllAtividades();
    }

    // ========== BUSCAR POR ID ==========
    @GetMapping("/{id}")
    public ResponseEntity<Atividade> getAtividadeById(@PathVariable String id) {
        Atividade atividade = atividadeService.getAtividadeById(id);
        return ResponseEntity.ok(atividade);
    }

    // ========== DELETAR ==========
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAtividade(@PathVariable String id) {
        atividadeService.deleteAtividade(id);
        return ResponseEntity.noContent().build();
    }

    // ========== ATUALIZAR ==========
    @PutMapping("/{id}")
    public ResponseEntity<Atividade> updateAtividade(@PathVariable String id,
                                                     @RequestBody @Valid Atividade atividade) {
        Atividade atualizada = atividadeService.updateAtividade(id, atividade);
        return ResponseEntity.ok(atualizada);
    }

    // ========== IA — GERAÇÃO ==========
    @PostMapping("/gerar")
    public ResponseEntity<Atividade> gerarAtividadeComIA(@RequestBody @Valid GerarAtividadeRequest request) {
        log.info("Requisição recebida para gerar atividade com IA: {}", request);
        Atividade atividadeGerada = atividadeService.gerarAtividadeComIA(request);
        return ResponseEntity.ok(atividadeGerada);
    }
}