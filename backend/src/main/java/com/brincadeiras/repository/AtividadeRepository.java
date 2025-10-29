package com.brincadeiras.repository;

import com.brincadeiras.model.Atividade;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AtividadeRepository extends MongoRepository<Atividade, String> {
}
