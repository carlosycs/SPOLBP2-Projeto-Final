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
public class TarefaStatusController {

    @Autowired 
    private TarefaRepository tarefaRepository;

    @GetMapping("/{status}")
    public String listarPorStatus(@PathVariable String status, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; 
        }

        List<Tarefa> todasAsTarefas = (List<Tarefa>) tarefaRepository.findAll();

        List<Tarefa> tarefasFiltradas = todasAsTarefas.stream()
                .filter(t -> status.equalsIgnoreCase(t.getStatus()))
                .collect(Collectors.toList());

        String titulo;
        if ("completed".equalsIgnoreCase(status)) {
            titulo = "Tarefas Concluídas";
        } else if ("pending".equalsIgnoreCase(status)) {
            titulo = "Tarefas Pendentes";
        } else if ("processing".equalsIgnoreCase(status)) {
            titulo = "Tarefas Processando";
        } else {
            return "redirect:/tarefas";
        }

        model.addAttribute("tarefas", tarefasFiltradas);
        model.addAttribute("tituloPagina", titulo);

        return "tarefas-status";
    }
}