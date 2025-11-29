package com.ifsp.projeto3bim.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String data;
    private String status;
    private String categoria;

    public Tarefa() {
    }

    public Tarefa(String nome, String data, String status) {
        this(nome, data, status, "Geral");
    }

    public Tarefa(String nome, String data, String status, String categoria) {
        this.nome = nome;
        this.data = data;
        this.status = status;
        this.categoria = categoria;
    }

    public void toggleStatus() {
        if ("Completed".equalsIgnoreCase(this.status)) {
            this.status = "Pending";
        } else if ("Pending".equalsIgnoreCase(this.status)) {
            this.status = "Completed";
        } else if ("Processing".equalsIgnoreCase(this.status)) {
            this.status = "Completed";
        } else {
            this.status = "Pending";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDate getDataAsLocalDate() {
        try {
            return LocalDate.parse(this.data, DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            return null;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}