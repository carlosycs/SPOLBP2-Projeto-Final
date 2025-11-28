package com.ifsp.projeto3bim.model;

import java.time.LocalDate;

public class Tarefa {
    private String nome;
    private LocalDate data;
    private String status;

    public Tarefa(String nome, String data, String status) {
        this.nome = nome;
        this.data = LocalDate.parse(data);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(String data) {
        this.data = LocalDate.parse(data);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void toggleStatus() {
        if ("Completed".equalsIgnoreCase(this.status)) {
            this.status = "Pending";
        } else {
            this.status = "Completed";
        }
    }
}
