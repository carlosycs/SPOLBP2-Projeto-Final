package com.ifsp.projeto3bim.controller;

import com.ifsp.projeto3bim.model.Tarefa;
import com.ifsp.projeto3bim.repository.TarefaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CalendarioController {

    @Autowired
    private TarefaRepository tarefaRepository; 

    @GetMapping("/calendario")
    public String calendario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        
        Iterable<Tarefa> tarefasIterable = tarefaRepository.findAll();
        List<Tarefa> todasAsTarefas = new ArrayList<>();
        tarefasIterable.forEach(todasAsTarefas::add);

        Map<String, List<Tarefa>> tarefasPorData = new HashMap<>();

        for (Tarefa t : todasAsTarefas) {
            String dataStr = t.getData();

            tarefasPorData
                    .computeIfAbsent(dataStr, k -> new ArrayList<>())
                    .add(t);
        }

        model.addAttribute("tarefasPorData", tarefasPorData);

        return "calendario";
    }
}