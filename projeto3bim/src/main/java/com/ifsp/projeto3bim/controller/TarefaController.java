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

        return "index"; // View do Dashboard principal
    }

    @PostMapping("/add")
    public String adicionar(@RequestParam("texto") String texto,
                            @RequestParam("data") String data,
                            @RequestParam(value = "categoria", defaultValue = "Geral") String categoria, // NOVO PARÂMETRO
                            HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        tarefaRepository.save(new Tarefa(texto, data, "Pending", categoria)); // USA NOVO CONSTRUTOR
        return "redirect:/tarefas";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

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
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

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

    @GetMapping("/edit/{id}")
    public String mostrarFormEdicao(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            model.addAttribute("tarefa", tarefaOptional.get());
            return "editar-tarefa"; // Template para edição
        }

        return "redirect:/tarefas";
    }

    @PostMapping("/update/{id}")
    public String atualizarTarefa(@PathVariable Long id,
                                  @RequestParam("texto") String texto,
                                  @RequestParam("data") String data,
                                  @RequestParam("status") String status,
                                  @RequestParam(value = "categoria", defaultValue = "Geral") String categoria, // NOVO PARÂMETRO
                                  HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            tarefa.setNome(texto);
            tarefa.setData(data);
            tarefa.setStatus(status);
            tarefa.setCategoria(categoria); // NOVA LINHA
            tarefaRepository.save(tarefa);
        }

        return "redirect:/tarefas";
    }

    @GetMapping("/completed")
    public String listarCompletas(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        List<Tarefa> completas = tarefaRepository.findByStatus("Completed");

        adicionarContadoresAoModelo(model);
        model.addAttribute("tarefas", completas);
        model.addAttribute("tituloPagina", "Tarefas Concluídas");

        return "tarefas-status";
    }

    @GetMapping("/processing")
    public String listarProcessando(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        List<Tarefa> processando = tarefaRepository.findByStatus("Processing");

        adicionarContadoresAoModelo(model);
        model.addAttribute("tarefas", processando);
        model.addAttribute("tituloPagina", "Tarefas Processando");

        return "tarefas-status";
    }

    @GetMapping("/pending")
    public String listarPendentes(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        List<Tarefa> pendentes = tarefaRepository.findByStatus("Pending");

        adicionarContadoresAoModelo(model);
        model.addAttribute("tarefas", pendentes);
        model.addAttribute("tituloPagina", "Tarefas Pendentes");

        return "tarefas-status";
    }

    @GetMapping("/categoria/{categoria}")
    public String listarPorCategoria(@PathVariable String categoria, Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        List<Tarefa> todasAsTarefas = (List<Tarefa>) tarefaRepository.findAll();
        List<Tarefa> tarefasFiltradas = todasAsTarefas.stream()
                .filter(t -> categoria.equalsIgnoreCase(t.getCategoria()))
                .toList();

        adicionarContadoresAoModelo(model);
        model.addAttribute("tarefas", tarefasFiltradas);
        model.addAttribute("tituloPagina", "Tarefas - " + categoria);

        return "tarefas-status";
    }
}