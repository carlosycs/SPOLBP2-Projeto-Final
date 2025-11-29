package com.ifsp.projeto3bim.controller;

import com.ifsp.projeto3bim.model.Tarefa;
import com.ifsp.projeto3bim.repository.TarefaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tarefas")
public class TarefaStatusController { // Renomeado para evitar conflito ou use o mesmo Controller principal

    @Autowired 
    private TarefaRepository tarefaRepository;

    // Método para exibir tarefas por Status
    @GetMapping("/{status}")
    public String listarPorStatus(@PathVariable String status, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; 
        }

        List<Tarefa> todasAsTarefas = (List<Tarefa>) tarefaRepository.findAll();
        
        // 1. Logica de filtro
        List<Tarefa> tarefasFiltradas = todasAsTarefas.stream()
                .filter(t -> status.equalsIgnoreCase(t.getStatus()))
                .collect(Collectors.toList());

        // 2. Define o título da página
        String titulo;
        if ("completed".equalsIgnoreCase(status)) {
            titulo = "Tarefas Concluídas";
        } else if ("pending".equalsIgnoreCase(status)) {
            titulo = "Tarefas Pendentes";
        } else if ("processing".equalsIgnoreCase(status)) {
            titulo = "Tarefas Processando";
        } else {
            return "redirect:/tarefas"; // Redireciona para o dashboard se o status for inválido/desconhecido
        }

        model.addAttribute("tarefas", tarefasFiltradas);
        model.addAttribute("tituloPagina", titulo);
        
        // 3. Retorna o novo template
        return "tarefas-status"; // Nome do novo arquivo HTML
    }
}