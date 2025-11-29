package com.ifsp.projeto3bim.controller;

import com.ifsp.projeto3bim.model.Tarefa;
import com.ifsp.projeto3bim.repository.TarefaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired 
    private TarefaRepository tarefaRepository;

   
    private void adicionarContadoresAoModelo(Model model) {
        List<Tarefa> todasAsTarefas = (List<Tarefa>) tarefaRepository.findAll();
        
        long completas = todasAsTarefas.stream()
            .filter(t -> "Completed".equalsIgnoreCase(t.getStatus()))
            .count();
        
        long pendentes = todasAsTarefas.stream()
            .filter(t -> "Pending".equalsIgnoreCase(t.getStatus()))
            .count();
        
        long processando = todasAsTarefas.stream()
            .filter(t -> "Processing".equalsIgnoreCase(t.getStatus()))
            .count();
        
        model.addAttribute("completas", completas);
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("processando", processando);
    }


    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; 
        }

        List<Tarefa> todasAsTarefas = (List<Tarefa>) tarefaRepository.findAll();
        adicionarContadoresAoModelo(model);
        
        model.addAttribute("tarefas", todasAsTarefas); 
        model.addAttribute("tituloPagina", "Lista de Tarefas"); 
        
        return "index"; 
    }

  
    
    @PostMapping("/add")
    public String adicionar(@RequestParam("texto") String texto, 
                            @RequestParam("data") String data, 
                            HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) { return "redirect:/login"; }
        
        tarefaRepository.save(new Tarefa(texto, data, "Pending")); 
        
        return "redirect:/tarefas";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, HttpSession session) { 
        if (session.getAttribute("usuarioLogado") == null) { return "redirect:/login"; }
        
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.toggleStatus();
            tarefaRepository.save(tarefa); 
        }
        return "redirect:/tarefas";
    }

    @PostMapping("/delete/{id}")
    public String deletar(@PathVariable Long id, HttpSession session) { 
        if (session.getAttribute("usuarioLogado") == null) { return "redirect:/login"; }
        
        tarefaRepository.deleteById(id); 
        return "redirect:/tarefas";
    }
    
  
    @PostMapping("/processing/{id}")
    public String marcarProcessando(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.setStatus("Processing");
            tarefaRepository.save(tarefa);
        }
        
        return "redirect:/tarefas";
    }

    @GetMapping("/completed")
    public String listarCompletas(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) { return "redirect:/login"; }
        
        List<Tarefa> completas = tarefaRepository.findByStatus("Completed");
        
        adicionarContadoresAoModelo(model);
        model.addAttribute("tarefas", completas);
        model.addAttribute("tituloPagina", "Tarefas Concluídas");
        
        return "tarefas-status";
    }

    @GetMapping("/processing")
    public String listarProcessando(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) { return "redirect:/login"; }
        
        List<Tarefa> processando = tarefaRepository.findByStatus("Processing");

        adicionarContadoresAoModelo(model);
        model.addAttribute("tarefas", processando);
        model.addAttribute("tituloPagina", "Tarefas Processando");
        
        return "tarefas-status";
    }

    @GetMapping("/pending")
    public String listarPendentes(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) { return "redirect:/login"; }
        
        List<Tarefa> pendentes = tarefaRepository.findByStatus("Pending");

        adicionarContadoresAoModelo(model);
        model.addAttribute("tarefas", pendentes);
        model.addAttribute("tituloPagina", "Tarefas Pendentes");
        
        return "tarefas-status";
    }

    
    @GetMapping("/calendario")
    public String calendario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        List<Tarefa> todasAsTarefas = (List<Tarefa>) tarefaRepository.findAll();
        Map<LocalDate, List<Tarefa>> tarefasPorData = new HashMap<>();

        for (Tarefa t : todasAsTarefas) {
             LocalDate data = t.getDataAsLocalDate();
             if (data != null) {
                 tarefasPorData
                        .computeIfAbsent(data, k -> new ArrayList<>())
                        .add(t);
             }
        }

        model.addAttribute("tarefasPorData", tarefasPorData);
        model.addAttribute("tituloPagina", "Calendário de Tarefas"); 
        return "calendario";
    }

}
