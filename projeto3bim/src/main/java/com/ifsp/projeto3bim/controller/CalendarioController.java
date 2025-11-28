package com.ifsp.projeto3bim.controller;

import com.ifsp.projeto3bim.model.Tarefa;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
public class CalendarioController {

    public static List<Tarefa> tarefasRef;

    public CalendarioController(TarefaController tarefaController) {
        tarefasRef = tarefaController.getTarefasList();
    }

    @GetMapping("/calendario")
    public String calendario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Map<String, List<Tarefa>> tarefasPorData = new HashMap<>();

        for (Tarefa t : tarefasRef) {
            String dataStr = t.getData().toString();
            tarefasPorData
                    .computeIfAbsent(dataStr, k -> new ArrayList<>())
                    .add(t);
        }

        model.addAttribute("tarefasPorData", tarefasPorData);

        return "calendario";
    }
}
