package com.ifsp.projeto3bim.controller;

import com.ifsp.projeto3bim.model.Usuario;
import com.ifsp.projeto3bim.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class Controladorlogin {

    private final UsuarioRepository usuarioRepository;

    public Controladorlogin(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login"; 
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar"; 
    }

    @PostMapping("/registrar")
    public String registrarUsuario(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String password) {
        Usuario usuario = new Usuario(name, email, password);
        usuarioRepository.salvar(usuario);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario != null && usuario.getSenha().equals(password)) {
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/tarefas"; // login certo → vai pra tarefas
        }

        return "redirect:/login?erro"; // login errado → volta com erro
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/login";
    }
}
