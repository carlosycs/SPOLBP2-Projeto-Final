package com.ifsp.projeto3bim.controller;

import com.ifsp.projeto3bim.model.Tarefa;
import com.ifsp.projeto3bim.repository.TarefaRepository; // Importação chave
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tarefas")
public class TarefaController {

   
    @Autowired 
    private TarefaRepository tarefaRepository;



    @GetMapping
    public String listar(Model model, HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; 
        }

        model.addAttribute("tarefas", tarefaRepository.findAll()); 
        return "index"; 
    }

    @PostMapping("/add")
    public String adicionar(@RequestParam("texto") String texto,
                            @RequestParam("data") String data,
                            HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        tarefaRepository.save(new Tarefa(texto, data, "Pending")); 
        return "redirect:/tarefas";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id, HttpSession session) { 
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }

        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);

        if (tarefa != null) {
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
}