package com.ifsp.projeto3bim.repository;

import com.ifsp.projeto3bim.model.Tarefa;
import org.springframework.data.repository.CrudRepository; 
import java.util.List;

public interface TarefaRepository extends CrudRepository<Tarefa, Long> {
    List<Tarefa> findByStatus(String status);
}