package com.brincadeiras.controller;

import com.brincadeiras.model.Atividade;
import com.brincadeiras.service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;

    @PostMapping()
    public Atividade postAtividade(@RequestBody Atividade atividade){
        return atividadeService.postAtividade(atividade);
    }

    @GetMapping()
    public List<Atividade> getAllAtividade(){
        return atividadeService.getAllAtividades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> getAtividadeById(@PathVariable String id) {
        Atividade atividade = atividadeService.getAtividadeById(id);
        if (atividade == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atividade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAtividade(@PathVariable String id) {
        Atividade existingAtividade = atividadeService.getAtividadeById(id);
        if (existingAtividade == null) {
            return ResponseEntity.notFound().build();
        }
        atividadeService.deleteAtividade(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atividade> updateAtividade(@PathVariable String id,
                                                     @RequestBody Atividade atividade) {
        Atividade existingAtividade = atividadeService.getAtividadeById(id);
        if (existingAtividade == null) {
            return ResponseEntity.notFound().build();
        }
        existingAtividade.setTitulo(atividade.getTitulo());
        existingAtividade.setDescricao(atividade.getDescricao());
        existingAtividade.setMateriais(atividade.getMateriais());
        existingAtividade.setFaixaEtaria(atividade.getFaixaEtaria());

        Atividade updated = atividadeService.updateAtividade(existingAtividade);
        return ResponseEntity.ok(updated);
    }
}
