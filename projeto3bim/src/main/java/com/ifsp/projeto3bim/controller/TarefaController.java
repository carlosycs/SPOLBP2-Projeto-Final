package com.ifsp.projeto3bim.controller;

import com.ifsp.projeto3bim.model.Tarefa;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

    private List<Tarefa> tarefas = new ArrayList<>();

    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        long completas = tarefas.stream().filter(t -> "Completed".equalsIgnoreCase(t.getStatus())).count();
        long pendentes = tarefas.stream().filter(t -> "Pending".equalsIgnoreCase(t.getStatus())).count();
        long processando = tarefas.stream().filter(t -> "Processing".equalsIgnoreCase(t.getStatus())).count();

        model.addAttribute("tarefas", tarefas);
        model.addAttribute("completas", completas);
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("processando", processando);

        return "index";
    }

    @PostMapping("/add")
    public String adicionar(@RequestParam("texto") String texto,
                            @RequestParam("data") String data,
                            HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        tarefas.add(new Tarefa(texto, data, "Pending"));
        return "redirect:/tarefas";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        if (id >= 0 && id < tarefas.size()) {
            tarefas.get(id).toggleStatus();
        }
        return "redirect:/tarefas";
    }

    @GetMapping("/processing")
    public String listarProcessando(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        List<Tarefa> processando = tarefas.stream()
                .filter(t -> "Processing".equalsIgnoreCase(t.getStatus()))
                .toList();
        model.addAttribute("tarefas", processando);
        model.addAttribute("completas", tarefas.stream().filter(t -> "Completed".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("pendentes", tarefas.stream().filter(t -> "Pending".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("processando", processando.size());
        return "tarefas-status";
    }

    @GetMapping("/pending")
    public String listarPendentes(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        List<Tarefa> pendentes = tarefas.stream()
                .filter(t -> "Pending".equalsIgnoreCase(t.getStatus()))
                .toList();
        model.addAttribute("tarefas", pendentes);
        model.addAttribute("completas", tarefas.stream().filter(t -> "Completed".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("pendentes", pendentes.size());
        model.addAttribute("processando", tarefas.stream().filter(t -> "Processing".equalsIgnoreCase(t.getStatus())).count());
        return "tarefas-status";
    }

    @GetMapping("/completed")
    public String listarCompletas(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        List<Tarefa> completas = tarefas.stream()
                .filter(t -> "Completed".equalsIgnoreCase(t.getStatus()))
                .toList();
        model.addAttribute("tarefas", completas);
        model.addAttribute("completas", completas.size());
        model.addAttribute("pendentes", tarefas.stream().filter(t -> "Pending".equalsIgnoreCase(t.getStatus())).count());
        model.addAttribute("processando", tarefas.stream().filter(t -> "Processing".equalsIgnoreCase(t.getStatus())).count());
        return "tarefas-status";
    }


    // Processing btn
    @PostMapping("/processing/{id}")
    public String marcarProcessando(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        if (id >= 0 && id < tarefas.size()) {
            tarefas.get(id).setStatus("Processing");
        }
        return "redirect:/tarefas";
    }


    @PostMapping("/delete/{id}")
    public String deletar(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        if (id >= 0 && id < tarefas.size()) {
            tarefas.remove(id);
        }
        return "redirect:/tarefas";
    }

    @GetMapping("/calendario")
    public String calendario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Map<LocalDate, List<Tarefa>> tarefasPorData = new HashMap<>();

        for (Tarefa t : tarefas) {
            LocalDate data = t.getData(); // LocalDate correto

            tarefasPorData
                    .computeIfAbsent(data, k -> new ArrayList<>())
                    .add(t);
        }

        model.addAttribute("tarefasPorData", tarefasPorData);
        return "calendario";
    }

    public List<Tarefa> getTarefasList() {
        return tarefas;
    }


}
